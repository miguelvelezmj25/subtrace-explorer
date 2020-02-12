package edu.cmu.cs.mvelezce.explorer.idta.partition;

import com.google.common.base.Objects;
import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.explorer.idta.IDTA;
import edu.cmu.cs.mvelezce.explorer.utils.FeatureExprUtils;

import java.util.HashSet;
import java.util.Set;

public abstract class Partitioning {

  public static final boolean CHECK_TOTAL_PARTITIONS = false;
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
    if (CHECK_TOTAL_PARTITIONS) {
      for (Partition p1 : this.getPartitions()) {
        for (Partition p2 : this.getPartitions()) {
          if (p1.equals(p2)) {
            continue;
          }

          if (!p1.getFeatureExpr().mex(p2.getFeatureExpr()).isTautology()) {
            return false;
          }
        }
      }

      return true;
    }

    return true;
  }

  public boolean isTotalPartition() {
    if (CHECK_TOTAL_PARTITIONS) {
      if (this.partitions.size() == 1) {
        if (this.partitions.iterator().next().getFeatureExpr().isTautology()) {
          return true;
        }

        throw new RuntimeException(
            "There is only one partition in this partitioning, but it does not correspond to a total partition. It is "
                + this.partitions);
      }

      if (!this.arePartitionsMutex()) {
        throw new RuntimeException(
            "There are overlapping partitions in this partitioning " + this.partitions);
      }

      FeatureExpr formula = FeatureExprUtils.getFalse(IDTA.USE_BDD);

      for (Partition partition : this.getPartitions()) {
        formula = formula.or(partition.getFeatureExpr());
      }

      return formula.isTautology();
    }

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
