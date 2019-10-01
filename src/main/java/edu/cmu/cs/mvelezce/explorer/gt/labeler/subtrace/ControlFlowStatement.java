package edu.cmu.cs.mvelezce.explorer.gt.labeler.subtrace;

public class ControlFlowStatement {

  private final String statement;

  // Dummy constructor for faster xml
  private ControlFlowStatement() {
    this.statement = "";
  }

  ControlFlowStatement(String statement) {
    this.statement = statement;
  }

  public String getStatement() {
    return statement;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ControlFlowStatement that = (ControlFlowStatement) o;

    return statement.equals(that.statement);
  }

  @Override
  public int hashCode() {
    return statement.hashCode();
  }

  @Override
  public String toString() {
    return statement;
  }
}
