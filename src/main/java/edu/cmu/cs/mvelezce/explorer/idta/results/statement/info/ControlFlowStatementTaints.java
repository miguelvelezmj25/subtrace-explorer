package edu.cmu.cs.mvelezce.explorer.idta.results.statement.info;

import edu.cmu.cs.mvelezce.explorer.idta.taint.InfluencingTaints;

import java.util.Set;

public class ControlFlowStatementTaints extends ControlFlowStatementInfo<InfluencingTaints> {

  public ControlFlowStatementTaints(
      String packageName,
      String className,
      String methodSignature,
      int decisionIndex,
      Set<InfluencingTaints> info) {
    super(packageName, className, methodSignature, decisionIndex, info);
  }
}
