package edu.cmu.cs.mvelezce.explorer.idta.partition;

import com.google.common.base.Objects;
import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.explorer.idta.IDTA;
import edu.cmu.cs.mvelezce.explorer.utils.ConstraintUtils;
import edu.cmu.cs.mvelezce.explorer.utils.FeatureExprUtils;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class Partition {

  public static final Partition UNIVERSE = new Partition(FeatureExprUtils.getTrue(IDTA.USE_BDD));

  private final FeatureExpr featureExpr;
  private final boolean isRemaining;

  public Partition(FeatureExpr featureExpr) {
    this(featureExpr, true);
  }

  private Partition(FeatureExpr featureExpr, boolean isRemaining) {
    this.featureExpr = featureExpr;
    this.isRemaining = isRemaining;
  }

  public static Set<Partition> getPartitions(Set<String> prettyPartitions) {
    Set<Partition> partitions = new HashSet<>();

    for (String prettyPartition : prettyPartitions) {
      Partition partition =
          new Partition(FeatureExprUtils.parseAsFeatureExpr(IDTA.USE_BDD, prettyPartition));
      partitions.add(partition);
    }

    return partitions;
  }

  @Nullable
  public static Partition getRemainingPartition(Set<Partition> partitions) {
    FeatureExpr formula = FeatureExprUtils.getFalse(IDTA.USE_BDD);

    for (Partition partition : partitions) {
      formula = formula.or(partition.getFeatureExpr());
    }

    if (formula.isTautology()) {
      return null;
    }

    return new Partition(formula.not(), true);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Partition partition = (Partition) o;
    return Objects.equal(featureExpr, partition.featureExpr);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(featureExpr);
  }

  public FeatureExpr getFeatureExpr() {
    return featureExpr;
  }

  @Override
  public String toString() {
    return ConstraintUtils.prettyPrintFeatureExpr(this.featureExpr);
  }
}
