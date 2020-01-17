package edu.cmu.cs.mvelezce.explorer.idta.results.statement.info;

import com.google.common.base.Objects;
import edu.cmu.cs.mvelezce.explorer.idta.partition.PartialPartition;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ControlFlowStmtPartitioningPretty extends ControlFlowStmtPartitioning {

  private final Set<String> prettyPartitions;

  public ControlFlowStmtPartitioningPretty(
      String packageName,
      String className,
      String methodSignature,
      int decisionIndex,
      Set<String> prettyPartitions) {
    super(
        packageName,
        className,
        methodSignature,
        decisionIndex,
        new PartialPartition(new ArrayList<>(), new HashSet<>()));

    this.prettyPartitions = prettyPartitions;
  }

  public Set<String> getPrettyPartitions() {
    return prettyPartitions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    ControlFlowStmtPartitioningPretty that = (ControlFlowStmtPartitioningPretty) o;
    return Objects.equal(prettyPartitions, that.prettyPartitions);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(super.hashCode(), prettyPartitions);
  }
}
