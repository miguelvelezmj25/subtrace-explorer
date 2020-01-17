package edu.cmu.cs.mvelezce.explorer.idta.partition;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.MinConfigsGenerator;
import edu.cmu.cs.mvelezce.explorer.utils.ConstraintUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/** Covers the entire configuration space */
public class TotalPartition extends Partitioning {

  public TotalPartition(Collection<String> options, Set<Partition> partitions) {
    super(options, partitions);

    if (!this.isTotalPartition()) {
      throw new RuntimeException("Expected a total partition");
    }
  }

  public TotalPartition(Collection<String> options) {
    super(options);
  }

  @Override
  public TotalPartition merge(Partitioning partitioning) {
    Set<Partition> partitions = new HashSet<>();

    for (Partition p1 : partitioning.getPartitions()) {
      for (Partition p2 : this.getPartitions()) {
        FeatureExpr formula = p1.getFeatureExpr().and(p2.getFeatureExpr());

        if (formula.isContradiction()) {
          continue;
        }

        // The and op can cause the formula to look ugly
        String prettyPartition = ConstraintUtils.prettyPrintFeatureExpr(formula, this.getOptions());
        formula = MinConfigsGenerator.parseAsFeatureExpr(prettyPartition);
        prettyPartition = ConstraintUtils.prettyPrintFeatureExpr(formula, this.getOptions());
        partitions.add(new Partition(formula, prettyPartition));
      }
    }

    TotalPartition newPartition = new TotalPartition(this.getOptions(), partitions);

    if (!newPartition.isTotalPartition()) {
      throw new RuntimeException("The final partitioning is not a total partition");
    }

    return newPartition;
  }

  @Override
  public boolean canMerge() {
    throw new UnsupportedOperationException("implement");
  }
}
