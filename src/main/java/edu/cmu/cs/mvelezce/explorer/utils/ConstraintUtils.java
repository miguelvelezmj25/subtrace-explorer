package edu.cmu.cs.mvelezce.explorer.utils;

import de.fosd.typechef.featureexpr.FeatureExpr;
import de.fosd.typechef.featureexpr.SingleFeatureExpr;
import edu.cmu.cs.mvelezce.utils.configurations.ConfigHelper;
import scala.collection.JavaConverters;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public final class ConstraintUtils {

  static {
    System.err.println("This class should be with the FeatureExpr lib");
  }

  private ConstraintUtils() {}

  public static String toStringConstraints(Set<Set<String>> configs, Set<String> options) {
    StringBuilder orConstraints = new StringBuilder();

    Iterator<Set<String>> configsIter = configs.iterator();

    while (configsIter.hasNext()) {
      Set<String> config = configsIter.next();
      String andConstraint = ConstraintUtils.parseAsConstraint(config, options);
      orConstraints.append(andConstraint);

      if (configsIter.hasNext()) {
        orConstraints.append(" || ");
      }
    }

    return orConstraints.toString();
  }

  public static String parseAsConstraint(Set<String> config, Collection<String> options) {
    Iterator<String> optionsIter = options.iterator();
    StringBuilder stringBuilder = new StringBuilder("(");

    while (optionsIter.hasNext()) {
      String option = optionsIter.next();

      if (!config.contains(option)) {
        stringBuilder.append("!");
      }

      stringBuilder.append(option);

      if (optionsIter.hasNext()) {
        stringBuilder.append(" && ");
      }
    }

    stringBuilder.append(")");

    return stringBuilder.toString();
  }

  public static Set<String> getStringConstraints(
      Set<String> control, Set<String> data, Set<String> config) {
    Set<String> activatedControlOptions = new HashSet<>(control);
    activatedControlOptions.retainAll(config);

    Set<String> constraintOptions = new HashSet<>(control);
    constraintOptions.addAll(data);

    Set<String> stringConstraints = new HashSet<>();
    Set<Set<String>> combos = ConfigHelper.getConfigurations(data);

    for (Set<String> combo : combos) {
      Set<String> activatedOptions = new HashSet<>(combo);
      activatedOptions.addAll(activatedControlOptions);

      String stringConstraint =
          ConstraintUtils.parseAsConstraint(activatedOptions, constraintOptions);
      stringConstraints.add(stringConstraint);
    }

    return stringConstraints;
  }

  public static String prettyPrintFeatureExpr(FeatureExpr featureExpr, Collection<String> options) {
    String stringRep = featureExpr.toString();

    if (stringRep.equals("True") || stringRep.equals("False")) {
      return stringRep;
    }

    String stringInteraction = featureExpr.toTextExpr().replaceAll("definedEx\\(", "");

    for (String option : options) {
      stringInteraction = stringInteraction.replaceAll(option + "\\)", option);
    }

    return stringInteraction;
  }

  public static Set<String> toConfig(
      Collection<SingleFeatureExpr> enabledFeatures, Collection<String> options) {
    Set<String> config = new HashSet<>();

    for (SingleFeatureExpr featureExpr : enabledFeatures) {
      config.add(featureExpr.feature());
    }

    return config;
  }

  public static Set<String> toConfig(FeatureExpr featureExpr, Collection<String> options) {
    String prettyConstraint = ConstraintUtils.prettyPrintFeatureExpr(featureExpr, options);
    prettyConstraint = prettyConstraint.replaceAll("\\(", "");
    prettyConstraint = prettyConstraint.replaceAll("\\)", "");
    String[] optionsValues = prettyConstraint.split("&&");

    Set<String> config = new HashSet<>();

    for (String optionsValue : optionsValues) {
      optionsValue = optionsValue.trim();

      if (!optionsValue.startsWith("!")) {
        config.add(optionsValue);
      }
    }

    return config;
  }

  public static String prettyPrintFeatureExpr(FeatureExpr featureExpr) {
    Set<SingleFeatureExpr> features =
        JavaConverters.setAsJavaSet(featureExpr.collectDistinctFeatureObjects());
    Set<String> options = new HashSet<>();

    for (SingleFeatureExpr feature : features) {
      options.add(feature.feature());
    }

    return prettyPrintFeatureExpr(featureExpr, options);
  }

  public static Set<String> getEntries(String prettyConstraint) {
    prettyConstraint = prettyConstraint.replaceAll("\\(", "");
    prettyConstraint = prettyConstraint.replaceAll("\\)", "");
    String[] x = prettyConstraint.split("&&");

    Set<String> entries = new HashSet<>();

    for (String y : x) {
      entries.add(y.trim());
    }

    return entries;
  }
}
