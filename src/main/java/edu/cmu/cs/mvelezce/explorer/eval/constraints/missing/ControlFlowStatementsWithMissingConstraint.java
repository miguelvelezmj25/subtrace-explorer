package edu.cmu.cs.mvelezce.explorer.eval.constraints.missing;

import com.google.common.base.Objects;
import edu.cmu.cs.mvelezce.explorer.gt.labeler.subtrace.ControlFlowStatement;

import java.util.HashSet;
import java.util.Set;

public class ControlFlowStatementsWithMissingConstraint {
  private final String missingConstraint;
  private final Set<ControlFlowStatement> controlFlowStatements;

  // Dummy constructor for faster xml
  private ControlFlowStatementsWithMissingConstraint() {
    this.missingConstraint = "";
    this.controlFlowStatements = new HashSet<>();
  }

  ControlFlowStatementsWithMissingConstraint(
      String missingConstraint, Set<ControlFlowStatement> controlFlowStatements) {
    this.missingConstraint = missingConstraint;
    this.controlFlowStatements = controlFlowStatements;
  }

  public String getMissingConstraint() {
    return missingConstraint;
  }

  public Set<ControlFlowStatement> getControlFlowStatements() {
    return controlFlowStatements;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ControlFlowStatementsWithMissingConstraint controlFlowStatementsWithMissingConstraint =
        (ControlFlowStatementsWithMissingConstraint) o;
    return Objects.equal(
            missingConstraint, controlFlowStatementsWithMissingConstraint.missingConstraint)
        && Objects.equal(
            controlFlowStatements,
            controlFlowStatementsWithMissingConstraint.controlFlowStatements);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(missingConstraint, controlFlowStatements);
  }
}
