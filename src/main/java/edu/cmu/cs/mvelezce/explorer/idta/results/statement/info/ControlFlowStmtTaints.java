package edu.cmu.cs.mvelezce.explorer.idta.results.statement.info;

import edu.cmu.cs.mvelezce.explorer.idta.taint.InfluencingTaints;

import java.util.HashSet;
import java.util.Set;

public class ControlFlowStmtTaints extends ControlFlowStmtInfo<Set<InfluencingTaints>> {

  public ControlFlowStmtTaints(
      String packageName,
      String className,
      String methodSignature,
      int decisionIndex,
      Set<InfluencingTaints> info) {
    super(packageName, className, methodSignature, decisionIndex, info);
  }

  private ControlFlowStmtTaints() {
    super("", "", "", -1, new HashSet<>());
  }
}
