package edu.cmu.cs.mvelezce.explorer.idta.partition;

import de.fosd.typechef.featureexpr.FeatureExpr;

import java.util.HashSet;
import java.util.Set;

/** Covers the entire configuration space */
public class TotalPartition extends Partitioning {

  public TotalPartition(Set<Partition> partitions) {
    super(partitions);

    if (!this.isTotalPartition()) {
      throw new RuntimeException("Expected a total partition");
    }
  }

  public TotalPartition() {
    super();
  }

  @Override
  public TotalPartition merge(Partitioning partitioning) {
    if (this.equals(partitioning)) {
      return new TotalPartition(this.getPartitions());
    }

    Set<Partition> partitions = new HashSet<>();

    for (Partition p1 : partitioning.getPartitions()) {
      for (Partition p2 : this.getPartitions()) {
        FeatureExpr formula = p1.getFeatureExpr().and(p2.getFeatureExpr());

        if (formula.isContradiction()) {
          continue;
        }

        partitions.add(new Partition(formula));
      }
    }

    TotalPartition newPartition = new TotalPartition(partitions);
    //    System.out.println(partitions.size());

    //    if (!newPartition.isTotalPartition()) {
    //      throw new RuntimeException("The final partitioning is not a total partition");
    //    }

    return newPartition;
  }

  @Override
  public boolean canMerge() {
    throw new UnsupportedOperationException("implement");
  }
}
