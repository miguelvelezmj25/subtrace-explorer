package edu.cmu.cs.mvelezce.explorer.idta.partition;

import com.google.common.base.Objects;

import java.util.HashSet;
import java.util.Set;

public abstract class Partitioning {

  private static final boolean USE_TOTAL_PARTITION = true;
  private static final boolean MERGE_CROSS_PRODUCT = true;

  private final Set<Partition> partitions = new HashSet<>();

  Partitioning(Set<Partition> partitions) {
    this.partitions.addAll(partitions);
  }

  Partitioning() {
    this.partitions.add(Partition.UNIVERSE);
  }

  public static Partitioning getPartitioning() {
    if (USE_TOTAL_PARTITION) {
      return new TotalPartition();
    }

    return new PartialPartition();
  }

  public static Partitioning getPartitioning(Set<Partition> partitions) {
    if (USE_TOTAL_PARTITION) {
      return new TotalPartition(partitions);
    }

    return new PartialPartition(partitions);
  }

  public abstract TotalPartition merge(Partitioning partitioning);

  public abstract boolean canMerge();

  public boolean arePartitionsMutex() {
    for (Partition p1 : this.getPartitions()) {
      for (Partition p2 : this.getPartitions()) {
        if (p1.equals(p2)) {
          continue;
        }

        //        throw new UnsupportedOperationException("Check if we need to do mex with
        // tautology");
        if (!p1.getFeatureExpr().mex(p2.getFeatureExpr()).isTautology()) {
          return true;
        }
      }
    }

    return false;
  }

  public boolean isTotalPartition() {
    //    throw new UnsupportedOperationException("Check if we need to do some operation");
    //    if (this.partitions.size() == 1) {
    //      if (this.partitions.iterator().next().getFeatureExpr().isTautology()) {
    //        return true;
    //      }
    //
    //      throw new RuntimeException(
    //          "There is only one partition in this partitioning, but it does not correspond to a
    // total partition. It is "
    //              + this.partitions);
    //    }
    //
    //    if (this.arePartitionsMutex()) {
    //      throw new RuntimeException("There are overlapping partitions in this partitioning");
    //    }
    //
    //    FeatureExpr formula = SATFeatureExprFactory.False();
    //
    //    for (Partition partition : this.getPartitions()) {
    //      formula = formula.or(partition.getFeatureExpr());
    //    }
    //
    //    return formula.isTautology();

    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Partitioning that = (Partitioning) o;
    return Objects.equal(partitions, that.partitions);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(partitions);
  }

  public Set<Partition> getPartitions() {
    return partitions;
  }
}
