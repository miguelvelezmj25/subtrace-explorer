package edu.cmu.cs.mvelezce.explorer.idta.results.statement.info;

import com.google.common.base.Objects;

import java.util.HashSet;
import java.util.Set;

public class ControlFlowStatementConstraintsPretty extends ControlFlowStatementConstraints {
  private final Set<String> prettyConstraints;

  public ControlFlowStatementConstraintsPretty(
      String packageName,
      String className,
      String methodSignature,
      int decisionIndex,
      Set<String> prettyConstraints) {
    super(packageName, className, methodSignature, decisionIndex, new HashSet<>());

    this.prettyConstraints = prettyConstraints;
  }

  public Set<String> getPrettyConstraints() {
    return prettyConstraints;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    ControlFlowStatementConstraintsPretty that = (ControlFlowStatementConstraintsPretty) o;
    return Objects.equal(prettyConstraints, that.prettyConstraints);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(super.hashCode(), prettyConstraints);
  }
}
