package edu.cmu.cs.mvelezce.explorer.idta.other;

import edu.cmu.cs.mvelezce.explorer.idta.taint.InfluencingTaints;

import java.util.Set;

public class PhosphorControlFlowStatementInfo {

  private final String packageName;
  private final String className;
  private final String methodSignature;
  private final int decisionIndex;
  private final Set<InfluencingTaints> influencingTaints;

  private PhosphorControlFlowStatementInfo() {
    this.packageName = null;
    this.className = null;
    this.methodSignature = null;
    this.decisionIndex = -1;
    this.influencingTaints = null;
  }

  public PhosphorControlFlowStatementInfo(
      String packageName,
      String className,
      String methodSignature,
      int decisionIndex,
      Set<InfluencingTaints> influencingTaints) {
    this.packageName = packageName;
    this.className = className;
    this.methodSignature = methodSignature;
    this.decisionIndex = decisionIndex;
    this.influencingTaints = influencingTaints;
  }

  public String getPackageName() {
    return packageName;
  }

  public String getClassName() {
    return className;
  }

  public String getMethodSignature() {
    return methodSignature;
  }

  public int getDecisionIndex() {
    return decisionIndex;
  }

  public Set<InfluencingTaints> getInfluencingTaints() {
    return influencingTaints;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PhosphorControlFlowStatementInfo that = (PhosphorControlFlowStatementInfo) o;

    if (decisionIndex != that.decisionIndex) {
      return false;
    }
    if (!packageName.equals(that.packageName)) {
      return false;
    }
    if (!className.equals(that.className)) {
      return false;
    }
    if (!methodSignature.equals(that.methodSignature)) {
      return false;
    }
    return influencingTaints.equals(that.influencingTaints);
  }

  @Override
  public int hashCode() {
    int result = packageName.hashCode();
    result = 31 * result + className.hashCode();
    result = 31 * result + methodSignature.hashCode();
    result = 31 * result + decisionIndex;
    result = 31 * result + influencingTaints.hashCode();
    return result;
  }
}
