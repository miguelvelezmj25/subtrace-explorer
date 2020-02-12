package edu.cmu.cs.mvelezce.explorer.idta.partition;

import de.fosd.typechef.featureexpr.FeatureExpr;

import java.util.HashSet;
import java.util.Set;

/** Covers the entire configuration space */
public class TotalPartition extends Partitioning {

  private static final Set<Partitioning> TOTAL_PARTITIONS = new HashSet<>();

  private static final Set<FeatureExpr> SAT = new HashSet<>();
  private static final Set<FeatureExpr> UNSAT = new HashSet<>();

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
  public boolean isTotalPartition() {
    if (Partitioning.CHECK_TOTAL_PARTITIONS) {
      if (TOTAL_PARTITIONS.contains(this)) {
        return true;
      }

      if (super.isTotalPartition()) {
        TOTAL_PARTITIONS.add(this);
        return true;
      }

      return false;
    }

    return true;
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

        if (!this.isSat(formula)) {
          continue;
        }

        partitions.add(new Partition(formula));
      }
    }

    TotalPartition newPartition = new TotalPartition(partitions);

    if (!newPartition.isTotalPartition()) {
      throw new RuntimeException("The final partitioning is not a total partition");
    }

    return newPartition;
  }

  private boolean isSat(FeatureExpr formula) {
    if (SAT.contains(formula)) {
      return true;
    }

    if (UNSAT.contains(formula)) {
      return false;
    }

    if (formula.isSatisfiable()) {
      SAT.add(formula);
      return true;
    }

    UNSAT.add(formula);
    return false;
  }

  @Override
  public boolean canMerge() {
    throw new UnsupportedOperationException("implement");
  }
}
