package edu.cmu.cs.mvelezce.explorer.idta.results.statement.info;

import com.google.common.base.Objects;

public class ControlFlowStmtInfo<T> {

  private final String packageName;
  private final String className;
  private final String methodSignature;
  private final int decisionIndex;
  private final T info;

  // Dummy constructor for fasterxml
  private ControlFlowStmtInfo() {
    this.packageName = null;
    this.className = null;
    this.methodSignature = null;
    this.decisionIndex = -1;
    this.info = null;
  }

  public ControlFlowStmtInfo(
      String packageName, String className, String methodSignature, int decisionIndex, T info) {
    this.packageName = packageName;
    this.className = className;
    this.methodSignature = methodSignature;
    this.decisionIndex = decisionIndex;
    this.info = info;
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

  public T getInfo() {
    return info;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ControlFlowStmtInfo<?> that = (ControlFlowStmtInfo<?>) o;
    return decisionIndex == that.decisionIndex
        && Objects.equal(packageName, that.packageName)
        && Objects.equal(className, that.className)
        && Objects.equal(methodSignature, that.methodSignature)
        && Objects.equal(info, that.info);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(packageName, className, methodSignature, decisionIndex, info);
  }
}
