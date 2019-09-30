package edu.cmu.cs.mvelezce.explorer.labeler.subtrace;

import com.google.common.base.Objects;
import com.sun.istack.internal.Nullable;

import java.util.UUID;

public class SubtraceLabel {

  // Helper field for IDing, not part of equals and hashcode
  private final UUID uuid;
  @Nullable private final UUID context;
  private final ControlFlowStatement controlFlowStatement;
  private final int execCount;

  SubtraceLabel(
      UUID uuid, @Nullable UUID context, ControlFlowStatement controlFlowStatement, int execCount) {
    this.uuid = uuid;
    this.context = context;
    this.controlFlowStatement = controlFlowStatement;
    this.execCount = execCount;
  }

  SubtraceLabel(@Nullable UUID context, ControlFlowStatement controlFlowStatement, int execCount) {
    this(UUID.randomUUID(), context, controlFlowStatement, execCount);
  }

  public UUID getUUID() {
    return uuid;
  }

  public UUID getContext() {
    return context;
  }

  public int getExecCount() {
    return execCount;
  }

  public ControlFlowStatement getControlFlowStatement() {
    return controlFlowStatement;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SubtraceLabel that = (SubtraceLabel) o;
    return execCount == that.execCount
        && Objects.equal(context, that.context)
        && Objects.equal(controlFlowStatement, that.controlFlowStatement);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(context, controlFlowStatement, execCount);
  }

  @Override
  public String toString() {
    String contextString = context == null ? "" : context.toString();

    return uuid + " - " + contextString + " - " + controlFlowStatement + " - " + execCount;
  }
}
