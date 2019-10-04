package edu.cmu.cs.mvelezce.explorer.idta.results.statement.info;

import de.fosd.typechef.featureexpr.FeatureExpr;

import java.util.Set;

public class ControlFlowStatementConstraints extends ControlFlowStatementInfo<FeatureExpr> {
  public ControlFlowStatementConstraints(
      String packageName,
      String className,
      String methodSignature,
      int decisionIndex,
      Set<FeatureExpr> info) {
    super(packageName, className, methodSignature, decisionIndex, info);
  }
}
