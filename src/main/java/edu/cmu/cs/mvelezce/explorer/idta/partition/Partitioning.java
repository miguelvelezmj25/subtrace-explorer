package edu.cmu.cs.mvelezce.explorer.idta.partition;

import com.google.common.base.Objects;
import de.fosd.typechef.featureexpr.FeatureExpr;
import de.fosd.typechef.featureexpr.sat.SATFeatureExprFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class Partitioning {

  private final Collection<String> options;
  private final Set<Partition> partitions = new HashSet<>();

  Partitioning(Collection<String> options, Set<Partition> partitions) {
    this.options = options;
    this.partitions.addAll(partitions);
  }

  Partitioning(Collection<String> options) {
    this.options = options;
    this.partitions.add(Partition.UNIVERSE);
  }

  public abstract TotalPartition merge(Partitioning partitioning);

  public abstract boolean canMerge();

  public boolean isTotalPartition() {
    if (this.partitions.size() == 1) {
      if (this.partitions.iterator().next().getFeatureExpr().isTautology()) {
        return true;
      }

      throw new RuntimeException(
          "There is only one partition in this partitioning, but it does not correspond to a total partition. It is "
              + this.partitions);
    }

    for (Partition p1 : this.getPartitions()) {
      for (Partition p2 : this.getPartitions()) {
        if (p1.equals(p2)) {
          continue;
        }

        if (!p1.getFeatureExpr().mex(p2.getFeatureExpr()).isTautology()) {
          throw new RuntimeException(
              "There are overlapping partitions in this partitioning "
                  + p1.getFeatureExpr()
                  + " - "
                  + p2.getFeatureExpr());
        }
      }
    }

    FeatureExpr formula = SATFeatureExprFactory.False();

    for (Partition partition : this.getPartitions()) {
      formula = formula.or(partition.getFeatureExpr());
    }

    return formula.isTautology();
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

  public Collection<String> getOptions() {
    return options;
  }
}
