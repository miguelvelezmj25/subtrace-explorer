package edu.cmu.cs.mvelezce.explorer.idta.results.statement;

import edu.cmu.cs.mvelezce.analysis.dynamic.BaseDynamicAnalysis;
import edu.cmu.cs.mvelezce.explorer.idta.results.parser.DecisionTaints;

import java.util.*;

/**
 * @param <T> The resulting data of the analysis
 * @param <E> The data that is saved for each control flow statement
 */
abstract class ControlFlowStmtAnalysis<T, E> extends BaseDynamicAnalysis<T> {

  private final String workloadSize;
  private final Map<String, E> statementsToData = new HashMap<>();
  private final List<String> options;

  ControlFlowStmtAnalysis(String programName, String workloadSize, List<String> options) {
    super(programName, new HashSet<>(options), new HashSet<>());

    this.workloadSize = workloadSize;
    this.options = options;
  }

  static String getClassName(String statementComponent) {
    int indexOfLastSlash = statementComponent.lastIndexOf("/");
    return statementComponent.substring(indexOfLastSlash + 1);
  }

  static String getPackageName(String statementComponent) {
    int indexOfLastSlash = statementComponent.lastIndexOf("/");
    statementComponent = statementComponent.substring(0, indexOfLastSlash);

    return statementComponent.replaceAll("/", ".");
  }

  abstract void addData(Set<String> config, Set<DecisionTaints> results);

  Map<String, E> getStatementsToData() {
    return statementsToData;
  }

  void addStatements(Set<DecisionTaints> results, E defaultData) {
    for (DecisionTaints decisionTaints : results) {
      String statement = decisionTaints.getDecision();
      this.statementsToData.putIfAbsent(statement, defaultData);
    }
  }

  public String getWorkloadSize() {
    return workloadSize;
  }

  public List<String> getOptionsList() {
    return options;
  }
}
