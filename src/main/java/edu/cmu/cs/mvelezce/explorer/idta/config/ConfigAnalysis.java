package edu.cmu.cs.mvelezce.explorer.idta.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.fosd.typechef.featureexpr.FeatureExpr;
import de.fosd.typechef.featureexpr.FeatureModel;
import de.fosd.typechef.featureexpr.SingleFeatureExpr;
import edu.cmu.cs.mvelezce.analysis.BaseAnalysis;
import edu.cmu.cs.mvelezce.explorer.idta.IDTA;
import edu.cmu.cs.mvelezce.explorer.idta.constraint.Constraint;
import edu.cmu.cs.mvelezce.explorer.utils.ConstraintUtils;
import edu.cmu.cs.mvelezce.explorer.utils.FeatureExprUtils;
import edu.cmu.cs.mvelezce.utils.config.Options;
import scala.Option;
import scala.Tuple2;
import scala.collection.JavaConverters;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConfigAnalysis extends BaseAnalysis<Set<Set<String>>> {

  private static final FeatureModel EMPTY_FM = FeatureExprUtils.getFeatureModel(IDTA.USE_BDD);

  private final String workloadSize;
  private final List<String> options;
  private final Set<SingleFeatureExpr> featureExprs = new HashSet<>();
  private final Set<Set<String>> executedConfigs = new HashSet<>();

  public ConfigAnalysis(String programName, String workloadSize, List<String> options) {
    super(programName);

    this.workloadSize = workloadSize;
    this.options = options;

    for (String option : this.options) {
      featureExprs.add(FeatureExprUtils.createDefinedExternal(IDTA.USE_BDD, option));
    }
  }

  public void saveExecutedConfig(Set<String> config) {
    this.executedConfigs.add(config);
  }

  @Nullable
  public Set<String> getNextGreedyConfig(Set<Constraint> constraintsToExplore) {
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
        JavaConverters.asJavaCollection(solution.get()._1), this.options);
  }

  @Override
  public Set<Set<String>> analyze() {
    return this.executedConfigs;
  }

  @Override
  public void writeToFile(Set<Set<String>> configs) throws IOException {
    String outputFile = this.outputDir() + "/" + this.getProgramName() + Options.DOT_JSON;
    File file = new File(outputFile);
    file.getParentFile().mkdirs();

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(file, configs);
  }

  @Override
  public Set<Set<String>> readFromFile(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    return mapper.readValue(file, new TypeReference<Set<Set<String>>>() {});
  }

  @Override
  public String outputDir() {
    return IDTA.OUTPUT_DIR
        + "/analysis/"
        + this.getProgramName()
        + "/cc/"
        + this.workloadSize
        + "/configs";
  }
}
