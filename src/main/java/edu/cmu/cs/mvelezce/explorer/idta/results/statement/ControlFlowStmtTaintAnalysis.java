package edu.cmu.cs.mvelezce.explorer.idta.results.statement;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.explorer.idta.IDTA;
import edu.cmu.cs.mvelezce.explorer.idta.results.parser.DecisionTaints;
import edu.cmu.cs.mvelezce.explorer.idta.results.statement.info.ControlFlowStmtInfo;
import edu.cmu.cs.mvelezce.explorer.idta.results.statement.info.ControlFlowStmtTaints;
import edu.cmu.cs.mvelezce.explorer.idta.taint.InfluencingTaints;
import edu.cmu.cs.mvelezce.explorer.idta.taint.TaintHelper;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ControlFlowStmtTaintAnalysis
    extends ControlFlowStmtAnalysis<Set<ControlFlowStmtTaints>, Set<InfluencingTaints>> {

  public ControlFlowStmtTaintAnalysis(
      String programName, String workloadSize, List<String> options) {
    super(programName, workloadSize, options);
  }

  ControlFlowStmtTaintAnalysis(String programName) {
    this(programName, "", new ArrayList<>());
  }

  @Override
  public Set<ControlFlowStmtTaints> analyze() {
    Set<ControlFlowStmtTaints> results = new HashSet<>();
    Map<String, Set<InfluencingTaints>> statementsToTaints = this.getStatementsToData();

    for (Map.Entry<String, Set<InfluencingTaints>> entry : statementsToTaints.entrySet()) {
      String statement = entry.getKey();
      String[] statementComponents = statement.split("\\.");

      String packageName = getPackageName(statementComponents[0]);
      String className = getClassName(statementComponents[0]);
      String methodSignature = statementComponents[1];
      int decisionIndex = Integer.parseInt(statementComponents[2]);

      ControlFlowStmtTaints controlFlowStatementConstraint =
          new ControlFlowStmtTaints(
              packageName, className, methodSignature, decisionIndex, entry.getValue());
      results.add(controlFlowStatementConstraint);
    }

    return results;
  }

  public void saveTaints(Set<String> config, Set<DecisionTaints> decisionTaints) {
    this.addStatements(decisionTaints);
    this.addData(config, decisionTaints);
  }

  @Override
  void addData(Set<String> config, Set<DecisionTaints> results) {
    for (DecisionTaints decisionTaints : results) {
      Set<String> dataTaints = TaintHelper.getDataTaints(decisionTaints, this.getOptionsList());
      Set<String> controlTaints =
          TaintHelper.getControlTaints(decisionTaints, this.getOptionsList());
      InfluencingTaints influencingTaints =
          new InfluencingTaints(config, controlTaints, dataTaints);

      String statement = decisionTaints.getDecision();
      this.getStatementsToData().get(statement).add(influencingTaints);
    }
  }

  @Override
  void addStatements(Set<DecisionTaints> results) {
    for (DecisionTaints decisionTaints : results) {
      String statement = decisionTaints.getDecision();
      this.getStatementsToData().putIfAbsent(statement, new HashSet<>());
    }
  }

  @Override
  public void writeToFile(Set<ControlFlowStmtTaints> controlFlowInfos) throws IOException {
    String outputFile = this.outputDir() + "/" + this.getProgramName() + Options.DOT_JSON;
    File file = new File(outputFile);
    file.getParentFile().mkdirs();

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(file, controlFlowInfos);
  }

  @Override
  public Set<ControlFlowStmtTaints> readFromFile(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    return mapper.readValue(
        file, new TypeReference<Set<ControlFlowStmtInfo<ControlFlowStmtTaints>>>() {});
  }

  @Override
  public String outputDir() {
    return IDTA.OUTPUT_DIR
        + "/analysis/"
        + this.getProgramName()
        + "/cc/"
        + this.getWorkloadSize()
        + "/taints";
  }
}
