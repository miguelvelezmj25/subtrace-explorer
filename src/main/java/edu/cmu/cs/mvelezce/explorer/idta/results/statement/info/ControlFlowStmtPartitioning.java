package edu.cmu.cs.mvelezce.explorer.idta.results.statement.info;

import edu.cmu.cs.mvelezce.explorer.idta.partition.Partitioning;

public class ControlFlowStmtPartitioning extends ControlFlowStmtInfo<Partitioning> {
  public ControlFlowStmtPartitioning(
      String packageName,
      String className,
      String methodSignature,
      int decisionIndex,
      Partitioning info) {
    super(packageName, className, methodSignature, decisionIndex, info);
  }
}
