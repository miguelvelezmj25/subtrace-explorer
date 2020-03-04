package edu.cmu.cs.mvelezce.explorer.eval.stmt;

public class ControlFlowStmt {

  private final String packageName;
  private final String className;
  private final String methodSignature;
  private final int decisionIndex;

  public ControlFlowStmt(
      String packageName, String className, String methodSignature, int decisionIndex) {
    this.packageName = packageName;
    this.className = className;
    this.methodSignature = methodSignature;
    this.decisionIndex = decisionIndex;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ControlFlowStmt that = (ControlFlowStmt) o;

    if (decisionIndex != that.decisionIndex) return false;
    if (!packageName.equals(that.packageName)) return false;
    if (!className.equals(that.className)) return false;
    return methodSignature.equals(that.methodSignature);
  }

  @Override
  public int hashCode() {
    int result = packageName.hashCode();
    result = 31 * result + className.hashCode();
    result = 31 * result + methodSignature.hashCode();
    result = 31 * result + decisionIndex;
    return result;
  }
}
