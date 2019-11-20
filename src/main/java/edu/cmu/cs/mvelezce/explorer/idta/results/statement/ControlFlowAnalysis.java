package edu.cmu.cs.mvelezce.explorer.idta.results.statement;

import edu.cmu.cs.mvelezce.analysis.dynamic.BaseDynamicAnalysis;
import edu.cmu.cs.mvelezce.cc.DecisionTaints;
import edu.cmu.cs.mvelezce.explorer.idta.taint.InfluencingTaints;
import edu.cmu.cs.mvelezce.explorer.idta.taint.TaintHelper;

import java.util.*;

abstract class ControlFlowAnalysis<T, E> extends BaseDynamicAnalysis<T> {

  static {
    System.err.println("Is this a dynamic analysis");
  }

  private final String workloadSize;
  private final Map<String, Set<E>> statementsToData = new HashMap<>();
  private final Map<String, Set<InfluencingTaints>> statementsToInfluencingTaints = new HashMap<>();
  private final List<String> options;

  ControlFlowAnalysis(String programName, String workloadSize, List<String> options) {
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

  Map<String, Set<E>> getStatementsToData() {
    return statementsToData;
  }

  void addStatements(Set<DecisionTaints> results) {
    this.statementsToInfluencingTaints.clear();

    for (DecisionTaints decisionTaints : results) {
      String statement = decisionTaints.getDecision();
      this.statementsToData.putIfAbsent(statement, new HashSet<>());
      this.statementsToInfluencingTaints.putIfAbsent(statement, new HashSet<>());
    }
  }

  Map<String, Set<InfluencingTaints>> addInfluencingTaints(Set<DecisionTaints> results) {
    for (DecisionTaints decisionTaints : results) {
      Set<String> conditionTaints = TaintHelper.getConditionTaints(decisionTaints, this.options);
      Set<String> contextTaints = TaintHelper.getContextTaints(decisionTaints, this.options);
      InfluencingTaints influencingTaints = new InfluencingTaints(contextTaints, conditionTaints);

      String statement = decisionTaints.getDecision();
      Set<InfluencingTaints> currentTaints = this.statementsToInfluencingTaints.get(statement);
      currentTaints.add(influencingTaints);
    }

    return this.statementsToInfluencingTaints;
  }

  public String getWorkloadSize() {
    return workloadSize;
  }
}
