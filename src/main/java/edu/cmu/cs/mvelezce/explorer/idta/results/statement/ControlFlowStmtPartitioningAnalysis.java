package edu.cmu.cs.mvelezce.explorer.idta.results.statement;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    this.addStatements(decisionTaints, new TotalPartition(this.getOptions()));
    this.addData(config, decisionTaints);
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
      Partitioning partitioning =
          this.parseStringPartitionsAsPartitioning(stringPartitions, this.getOptions());

      String statement = decisionTaints.getDecision();
      Partitioning currentPartitioning = this.getStatementsToData().get(statement);
      partitioning = currentPartitioning.merge(partitioning);
      this.getStatementsToData().put(statement, partitioning);
    }
  }

  private Partitioning parseStringPartitionsAsPartitioning(
      Set<String> stringPartitions, Collection<String> options) {
    Set<Partition> partitions = new HashSet<>();

    for (String stringPartition : stringPartitions) {
      Partition partition =
          new Partition(MinConfigsGenerator.parseAsFeatureExpr(stringPartition), stringPartition);
      partitions.add(partition);
    }

    Partition remainingPartition = Partition.getRemainingPartition(partitions, options);

    if (remainingPartition != null) {
      partitions.add(remainingPartition);
    }

    return new TotalPartition(this.getOptions(), partitions);
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
