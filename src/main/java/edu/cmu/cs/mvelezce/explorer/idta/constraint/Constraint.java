package edu.cmu.cs.mvelezce.explorer.idta.constraint;

import com.google.common.base.Objects;
import de.fosd.typechef.featureexpr.FeatureExpr;

public class Constraint {

  private final FeatureExpr featureExpr;

  public Constraint(FeatureExpr featureExpr) {
    this.featureExpr = featureExpr;
  }

  public FeatureExpr getFeatureExpr() {
    return featureExpr;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Constraint that = (Constraint) o;
    return Objects.equal(featureExpr, that.featureExpr);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(featureExpr);
  }

  @Override
  public String toString() {
    return this.featureExpr.toString();
  }
}
