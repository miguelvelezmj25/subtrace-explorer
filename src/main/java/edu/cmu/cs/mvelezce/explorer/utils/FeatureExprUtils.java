package edu.cmu.cs.mvelezce.explorer.utils;

import de.fosd.typechef.featureexpr.FeatureExpr;
import de.fosd.typechef.featureexpr.FeatureModel;
import de.fosd.typechef.featureexpr.SingleFeatureExpr;
import de.fosd.typechef.featureexpr.bdd.BDDFeatureExprFactory;
import de.fosd.typechef.featureexpr.bdd.BDDFeatureModel;
import de.fosd.typechef.featureexpr.sat.SATFeatureExprFactory;
import de.fosd.typechef.featureexpr.sat.SATFeatureModel;
import edu.cmu.cs.mvelezce.MinConfigsGenerator;

public final class FeatureExprUtils {

  private FeatureExprUtils() {}

  public static FeatureModel getFeatureModel(boolean useBDD) {
    if (useBDD) {
      return BDDFeatureModel.empty();
    }

    return SATFeatureModel.empty();
  }

  public static SingleFeatureExpr createDefinedExternal(boolean useBDD, String option) {
    if (useBDD) {
      return BDDFeatureExprFactory.createDefinedExternal(option);
    }

    return SATFeatureExprFactory.createDefinedExternal(option);
  }

  public static FeatureExpr getTrue(boolean useBDD) {
    if (useBDD) {
      return BDDFeatureExprFactory.True();
    }

    return SATFeatureExprFactory.True();
  }

  public static FeatureExpr getFalse(boolean useBDD) {
    if (useBDD) {
      return BDDFeatureExprFactory.False();
    }

    return SATFeatureExprFactory.False();
  }

  public static FeatureExpr parseAsFeatureExpr(boolean useBDD, String stringPartition) {
    if (useBDD) {
      return MinConfigsGenerator.parseAsBDDFeatureExpr(stringPartition);
    }

    return MinConfigsGenerator.parseAsSATFeatureExpr(stringPartition);
  }
}
