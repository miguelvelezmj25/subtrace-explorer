package edu.cmu.cs.mvelezce.explorer.idta.results.statement.info;

import java.util.HashSet;
import java.util.Set;

public class ControlFlowStmtPartitioningPretty extends ControlFlowStmtInfo<Set<String>> {

  public ControlFlowStmtPartitioningPretty(
      String packageName,
      String className,
      String methodSignature,
      int decisionIndex,
      Set<String> prettyPartitions) {
    super(packageName, className, methodSignature, decisionIndex, prettyPartitions);
  }

  private ControlFlowStmtPartitioningPretty() {
    this("", "", "", -1, new HashSet<>());
  }
}
