package edu.cmu.cs.mvelezce.explorer.idta.results.statement;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Objects;
import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.explorer.idta.IDTA;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partitioning;
import edu.cmu.cs.mvelezce.explorer.idta.results.parser.DecisionTaints;
import edu.cmu.cs.mvelezce.explorer.idta.results.statement.info.ControlFlowStmtPartitioning;
import edu.cmu.cs.mvelezce.explorer.idta.results.statement.info.ControlFlowStmtPartitioningPretty;
import edu.cmu.cs.mvelezce.explorer.idta.taint.TaintHelper;
import edu.cmu.cs.mvelezce.explorer.utils.ConstraintUtils;
import edu.cmu.cs.mvelezce.explorer.utils.FeatureExprUtils;
import edu.cmu.cs.mvelezce.utils.config.Options;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ControlFlowStmtPartitioningAnalysis
    extends ControlFlowStmtAnalysis<Set<ControlFlowStmtPartitioning>, Partitioning> {

  private static final Map<PartitionsToMerge, Partitioning> MERGED_PARTITIONS = new HashMap<>();
  private static final Map<String, FeatureExpr> PARSED_FEATURE_EXPR = new HashMap<>();

  public ControlFlowStmtPartitioningAnalysis(
      String programName, String workloadSize, List<String> options) {
    super(programName, workloadSize, options);
  }

  public ControlFlowStmtPartitioningAnalysis(String programName, String workloadSize) {
    this(programName, workloadSize, new ArrayList<>());
  }

  @Override
  public Set<ControlFlowStmtPartitioning> analyze() {
    this.removeTruePartitions();

    Set<ControlFlowStmtPartitioning> results = new HashSet<>();
    Map<String, Partitioning> statementsToPartitions = this.getStatementsToData();

    for (Map.Entry<String, Partitioning> entry : statementsToPartitions.entrySet()) {
      String statement = entry.getKey();
      String[] statementComponents = statement.split("\\.");

      String packageName = getPackageName(statementComponents[0]);
      String className = getClassName(statementComponents[0]);
      String methodSignature = statementComponents[1];
      int decisionIndex = Integer.parseInt(statementComponents[2]);

      ControlFlowStmtPartitioning controlFlowStatementPartitioning =
          new ControlFlowStmtPartitioning(
              packageName, className, methodSignature, decisionIndex, entry.getValue());
      results.add(controlFlowStatementPartitioning);
    }

    return results;
  }

  private void removeTruePartitions() {
    FeatureExpr trueFeatureExpr = FeatureExprUtils.getTrue(IDTA.USE_BDD);

    for (Partitioning partitioning : this.getStatementsToData().values()) {
      Set<Partition> partitionsToRemove = new HashSet<>();

      for (Partition partition : partitioning.getPartitions()) {
        if (partition.getFeatureExpr().equals(trueFeatureExpr)) {
          partitionsToRemove.add(partition);
        }
      }

      partitioning.getPartitions().removeAll(partitionsToRemove);
    }
  }

  public void savePartitions(Set<String> config, Set<DecisionTaints> decisionTaints) {
    long start = System.nanoTime();
    this.addStatements(decisionTaints);
    this.addData(config, decisionTaints);
    long end = System.nanoTime();
    System.out.println("Save partitions: " + (end - start) / 1E9);
  }

  @Override
  void addData(Set<String> config, Set<DecisionTaints> results) {
    for (DecisionTaints decisionTaints : results) {
      Set<String> controlTaints =
          TaintHelper.getControlTaints(decisionTaints, this.getOptionsList());
      Set<String> dataTaints = TaintHelper.getDataTaints(decisionTaints, this.getOptionsList());
      dataTaints.removeAll(controlTaints);

      Set<String> stringPartitions =
          ConstraintUtils.getStringConstraints(controlTaints, dataTaints, config);
      Partitioning newPartitioning = this.parseStringPartitionsAsPartitioning(stringPartitions);

      String statement = decisionTaints.getDecision();
      Partitioning currentPartitioning = this.getStatementsToData().get(statement);
      newPartitioning = this.merge(currentPartitioning, newPartitioning);
      this.getStatementsToData().put(statement, newPartitioning);
    }
  }

  private Partitioning merge(Partitioning currentPartitioning, Partitioning newPartitioning) {
    PartitionsToMerge partitionsToMerge =
        new PartitionsToMerge(currentPartitioning, newPartitioning);

    Partitioning mergedPartition = MERGED_PARTITIONS.get(partitionsToMerge);

    if (mergedPartition != null) {
      return mergedPartition;
    }

    mergedPartition = currentPartitioning.merge(newPartitioning);
    MERGED_PARTITIONS.put(partitionsToMerge, mergedPartition);

    return mergedPartition;
  }

  @Override
  void addStatements(Set<DecisionTaints> results) {
    for (DecisionTaints decisionTaints : results) {
      String statement = decisionTaints.getDecision();
      this.getStatementsToData().putIfAbsent(statement, Partitioning.getPartitioning());
    }
  }

  private Partitioning parseStringPartitionsAsPartitioning(Set<String> stringPartitions) {
    Set<Partition> partitions = new HashSet<>();

    for (String stringPartition : stringPartitions) {
      Partition partition = new Partition(this.parseStringPartition(stringPartition));
      partitions.add(partition);
    }

    Partition remainingPartition = Partition.getRemainingPartition(partitions);

    if (remainingPartition != null) {
      partitions.add(remainingPartition);
    }

    return Partitioning.getPartitioning(partitions);
  }

  private FeatureExpr parseStringPartition(String stringPartition) {
    FeatureExpr parsedString = PARSED_FEATURE_EXPR.get(stringPartition);

    if (parsedString != null) {
      return parsedString;
    }

    parsedString = FeatureExprUtils.parseAsFeatureExpr(IDTA.USE_BDD, stringPartition);
    PARSED_FEATURE_EXPR.put(stringPartition, parsedString);

    return parsedString;
  }

  @Override
  public void writeToFile(Set<ControlFlowStmtPartitioning> results) throws IOException {
    File file = new File(this.outputDir());

    if (file.exists()) {
      FileUtils.forceDelete(file);
    }

    int savedStmts = 0;
    Iterator<ControlFlowStmtPartitioning> controlFlowStmtPartitioningIter = results.iterator();

    for (int i = 0; savedStmts != results.size(); i++) {
      Set<ControlFlowStmtPartitioning> controlFlowStmtsToSave = new HashSet<>();

      for (int j = 0; controlFlowStmtPartitioningIter.hasNext() && j < 100; j++) {
        controlFlowStmtsToSave.add(controlFlowStmtPartitioningIter.next());
        savedStmts++;
      }

      String outputFile =
          this.outputDir() + "/" + this.getProgramName() + "_" + i + Options.DOT_JSON;
      file = new File(outputFile);
      file.getParentFile().mkdirs();

      Set<ControlFlowStmtPartitioningPretty> prettyResults = new HashSet<>();

      for (ControlFlowStmtPartitioning entry : controlFlowStmtsToSave) {
        Set<String> prettyPartitions = new HashSet<>();
        Partitioning partitions = entry.getInfo();

        for (Partition partition : partitions.getPartitions()) {
          String prettyPartition =
              ConstraintUtils.prettyPrintFeatureExpr(partition.getFeatureExpr(), this.getOptions());
          FeatureExpr featureExpr =
              FeatureExprUtils.parseAsFeatureExpr(IDTA.USE_BDD, prettyPartition);
          prettyPartition = ConstraintUtils.prettyPrintFeatureExpr(featureExpr, this.getOptions());
          prettyPartitions.add(prettyPartition);
        }

        ControlFlowStmtPartitioningPretty prettyResult =
            new ControlFlowStmtPartitioningPretty(
                entry.getPackageName(),
                entry.getClassName(),
                entry.getMethodSignature(),
                entry.getDecisionIndex(),
                prettyPartitions);
        prettyResults.add(prettyResult);
      }

      ObjectMapper mapper = new ObjectMapper();
      mapper.writeValue(file, prettyResults);
    }
  }

  @Override
  public Set<ControlFlowStmtPartitioning> readFromFile(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    List<ControlFlowStmtPartitioningPretty> prettyResults =
        mapper.readValue(file, new TypeReference<List<ControlFlowStmtPartitioningPretty>>() {});

    Set<ControlFlowStmtPartitioning> results = new HashSet<>();

    for (ControlFlowStmtPartitioningPretty prettyResult : prettyResults) {
      ControlFlowStmtPartitioning stmtPartitioning =
          new ControlFlowStmtPartitioning(
              prettyResult.getPackageName(),
              prettyResult.getClassName(),
              prettyResult.getMethodSignature(),
              prettyResult.getDecisionIndex(),
              Partitioning.getPartitioning(Partition.getPartitions(prettyResult.getInfo())));
      results.add(stmtPartitioning);
    }

    return results;
  }

  @Override
  public String outputDir() {
    return IDTA.OUTPUT_DIR
        + "/analysis/"
        + this.getProgramName()
        + "/cc/"
        + this.getWorkloadSize()
        + "/partitions";
  }

  private static class PartitionsToMerge {

    private final Partitioning partitioningOne;
    private final Partitioning partitioningTwo;

    PartitionsToMerge(Partitioning partitioningOne, Partitioning partitioningTwo) {
      this.partitioningOne = partitioningOne;
      this.partitioningTwo = partitioningTwo;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      PartitionsToMerge partitionsToMerge = (PartitionsToMerge) o;
      return Objects.equal(partitioningOne, partitionsToMerge.partitioningOne)
          && Objects.equal(partitioningTwo, partitionsToMerge.partitioningTwo);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(partitioningOne, partitioningTwo);
    }
  }
}
