package edu.cmu.cs.mvelezce.explorer.idta.partition;

import com.google.common.base.Objects;
import de.fosd.typechef.featureexpr.FeatureExpr;
import de.fosd.typechef.featureexpr.sat.SATFeatureExprFactory;
import edu.cmu.cs.mvelezce.explorer.utils.ConstraintUtils;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Set;

public class Partition {

  public static final FeatureExpr TRUE = SATFeatureExprFactory.True();
  public static final Partition UNIVERSE = new Partition(TRUE, TRUE.toString());

  private final FeatureExpr featureExpr;
  private final String prettyPartition;

  public Partition(FeatureExpr featureExpr, String prettyPartition) {
    this.featureExpr = featureExpr;
    this.prettyPartition = prettyPartition;
  }

  @Nullable
  public static Partition getRemainingPartition(
      Set<Partition> partitions, Collection<String> options) {
    FeatureExpr formula = SATFeatureExprFactory.False();

    for (Partition partition : partitions) {
      formula = formula.or(partition.getFeatureExpr());
    }

    if (formula.isTautology()) {
      return null;
    }

    FeatureExpr remainingPartition = formula.not();
    String prettyRemainingPartition =
        ConstraintUtils.prettyPrintFeatureExpr(remainingPartition, options);

    return new Partition(remainingPartition, prettyRemainingPartition);
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

  public String getPrettyPartition() {
    return prettyPartition;
  }

  @Override
  public String toString() {
    return prettyPartition;
  }
}
