package edu.cmu.cs.mvelezce.explorer.idta.partition;

import com.google.common.base.Objects;
import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.explorer.idta.IDTA;
import edu.cmu.cs.mvelezce.explorer.utils.FeatureExprUtils;

import javax.annotation.Nullable;
import java.util.Set;

public class Partition {

  public static final Partition UNIVERSE = new Partition(FeatureExprUtils.getTrue(IDTA.USE_BDD));

  private final FeatureExpr featureExpr;

  public Partition(FeatureExpr featureExpr) {
    this.featureExpr = featureExpr;
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

    return new Partition(formula.not());
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
}
