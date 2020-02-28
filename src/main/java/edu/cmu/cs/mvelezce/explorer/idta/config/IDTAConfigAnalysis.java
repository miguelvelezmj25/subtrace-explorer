package edu.cmu.cs.mvelezce.explorer.idta.config;

import de.fosd.typechef.featureexpr.FeatureExpr;
import de.fosd.typechef.featureexpr.FeatureModel;
import de.fosd.typechef.featureexpr.SingleFeatureExpr;
import edu.cmu.cs.mvelezce.explorer.idta.IDTA;
import edu.cmu.cs.mvelezce.explorer.idta.constraint.Constraint;
import edu.cmu.cs.mvelezce.explorer.utils.ConstraintUtils;
import edu.cmu.cs.mvelezce.explorer.utils.FeatureExprUtils;
import scala.Option;
import scala.Tuple2;
import scala.collection.JavaConverters;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IDTAConfigAnalysis extends ConfigAnalysis {

  private static final FeatureModel EMPTY_FM = FeatureExprUtils.getFeatureModel(IDTA.USE_BDD);

  private final Set<SingleFeatureExpr> featureExprs = new HashSet<>();
  private final Set<String> initialConfig;

  public IDTAConfigAnalysis(String programName, String workloadSize) {
    this(programName, workloadSize, new ArrayList<>(), new HashSet<>());
  }

  public IDTAConfigAnalysis(
      String programName, String workloadSize, List<String> options, Set<String> initialConfig) {
    super(programName, workloadSize, options);

    for (String option : options) {
      this.featureExprs.add(FeatureExprUtils.createDefinedExternal(IDTA.USE_BDD, option));
    }

    this.initialConfig = initialConfig;
  }

  @Nullable
  public Set<String> getNextConfig(Set<Constraint> constraintsToExplore) {
    if (constraintsToExplore.isEmpty()) {
      return null;
    }

    FeatureExpr formula = FeatureExprUtils.getTrue(IDTA.USE_BDD);

    for (Constraint constraint : constraintsToExplore) {
      FeatureExpr andedFormula = formula.and(constraint.getFeatureExpr());

      if (andedFormula.isContradiction()) {
        continue;
      }

      formula = andedFormula;
    }

    Option<
            Tuple2<
                scala.collection.immutable.List<SingleFeatureExpr>,
                scala.collection.immutable.List<SingleFeatureExpr>>>
        solution =
            formula.getSatisfiableAssignment(
                EMPTY_FM, JavaConverters.asScalaSet(this.featureExprs).toSet(), true);

    return ConstraintUtils.toConfig(
        JavaConverters.asJavaCollection(solution.get()._1), this.getOptions());
  }

  @Override
  public Set<String> getInitialConfig() {
    return this.initialConfig;
  }
}
