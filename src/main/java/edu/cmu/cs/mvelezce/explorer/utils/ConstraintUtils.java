package edu.cmu.cs.mvelezce.explorer.utils;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.utils.ConfigHelper;

import java.util.*;

public final class ConstraintUtils {

  private ConstraintUtils() {}

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

  public static List<String> getStringConstraints(
      Set<String> context, Set<String> condition, Set<String> config) {
    Set<String> activatedContextOptions = new HashSet<>(context);
    activatedContextOptions.retainAll(config);

    Set<String> constraintOptions = new HashSet<>(context);
    constraintOptions.addAll(condition);

    List<String> stringConstraints = new ArrayList<>();
    Set<Set<String>> combos = ConfigHelper.getConfigurations(condition);

    for (Set<String> combo : combos) {
      Set<String> activatedOptions = new HashSet<>(combo);
      activatedOptions.addAll(activatedContextOptions);

      String stringConstraint =
          ConstraintUtils.parseAsConstraint(activatedOptions, constraintOptions);
      stringConstraints.add(stringConstraint);
    }

    return stringConstraints;
  }

  public static String prettyPrintFeatureExpr(FeatureExpr featureExpr, Set<String> options) {
    String stringInteraction = featureExpr.toTextExpr().replaceAll("definedEx\\(", "");

    for (String option : options) {
      stringInteraction = stringInteraction.replaceAll(option + "\\)", option);
    }

    return stringInteraction;
  }
}