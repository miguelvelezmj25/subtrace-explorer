package edu.cmu.cs.mvelezce.explorer.idta.results.statement;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.MinConfigsGenerator;
import edu.cmu.cs.mvelezce.explorer.idta.IDTA;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partitioning;
import edu.cmu.cs.mvelezce.explorer.idta.partition.TotalPartition;
import edu.cmu.cs.mvelezce.explorer.idta.results.parser.DecisionTaints;
import edu.cmu.cs.mvelezce.explorer.idta.results.statement.info.ControlFlowStmtPartitioning;
import edu.cmu.cs.mvelezce.explorer.idta.results.statement.info.ControlFlowStmtPartitioningPretty;
import edu.cmu.cs.mvelezce.explorer.idta.taint.TaintHelper;
import edu.cmu.cs.mvelezce.explorer.utils.ConstraintUtils;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ControlFlowStmtPartitioningAnalysis
    extends ControlFlowStmtAnalysis<Set<ControlFlowStmtPartitioning>, Partitioning> {

  private static final Map<String, FeatureExpr> PARSED_FEATURE_EXPR = new HashMap<>();

  public ControlFlowStmtPartitioningAnalysis(
      String programName, String workloadSize, List<String> options) {
    super(programName, workloadSize, options);
  }

  @Override
  public Set<ControlFlowStmtPartitioning> analyze() {
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

  public void savePartitions(Set<String> config, Set<DecisionTaints> decisionTaints) {
    this.addStatements(decisionTaints);
    this.addData(config, decisionTaints);
  }

  @Override
  void addData(Set<String> config, Set<DecisionTaints> results) {
    long time = 0;

    for (DecisionTaints decisionTaints : results) {
      Set<String> controlTaints =
          TaintHelper.getControlTaints(decisionTaints, this.getOptionsList());
      Set<String> dataTaints = TaintHelper.getDataTaints(decisionTaints, this.getOptionsList());
      dataTaints.removeAll(controlTaints);

      Set<String> stringPartitions =
          ConstraintUtils.getStringConstraints(controlTaints, dataTaints, config);
      long start = System.nanoTime();
      Partitioning partitioning = this.parseStringPartitionsAsPartitioning(stringPartitions);
      long end = System.nanoTime();
      time += (end - start);

      String statement = decisionTaints.getDecision();
      Partitioning currentPartitioning = this.getStatementsToData().get(statement);
      partitioning = currentPartitioning.merge(partitioning);
      this.getStatementsToData().put(statement, partitioning);
    }

    System.out.println("Inner loop: " + (time / 1E9));
  }

  @Override
  void addStatements(Set<DecisionTaints> results) {
    for (DecisionTaints decisionTaints : results) {
      String statement = decisionTaints.getDecision();
      this.getStatementsToData().putIfAbsent(statement, new TotalPartition());
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

    return new TotalPartition(partitions);
  }

  private FeatureExpr parseStringPartition(String stringPartition) {
    FeatureExpr parsedString = PARSED_FEATURE_EXPR.get(stringPartition);

    if (parsedString != null) {
      return parsedString;
    }

    parsedString = MinConfigsGenerator.parseAsFeatureExpr(stringPartition);
    PARSED_FEATURE_EXPR.put(stringPartition, parsedString);

    return parsedString;
  }

  @Override
  public void writeToFile(Set<ControlFlowStmtPartitioning> results) throws IOException {
    String outputFile = this.outputDir() + "/" + this.getProgramName() + Options.DOT_JSON;
    File file = new File(outputFile);
    file.getParentFile().mkdirs();

    Set<ControlFlowStmtPartitioningPretty> prettyResults = new HashSet<>();

    for (ControlFlowStmtPartitioning entry : results) {
      Set<String> prettyPartitions = new HashSet<>();
      Partitioning partitions = entry.getInfo();

      for (Partition partition : partitions.getPartitions()) {
        String prettyPartition =
            ConstraintUtils.prettyPrintFeatureExpr(partition.getFeatureExpr(), this.getOptions());
        FeatureExpr featureExpr = MinConfigsGenerator.parseAsFeatureExpr(prettyPartition);
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

  @Override
  public Set<ControlFlowStmtPartitioning> readFromFile(File file) throws IOException {
    //    ObjectMapper mapper = new ObjectMapper();
    //
    //    return mapper.readValue(file, new TypeReference<Set<ControlFlowStmtInfo>>() {});
    throw new UnsupportedOperationException("implement");
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
}
