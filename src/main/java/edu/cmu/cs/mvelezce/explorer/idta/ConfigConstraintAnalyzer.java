package edu.cmu.cs.mvelezce.explorer.idta;

import edu.cmu.cs.mvelezce.explorer.eval.constraints.idta.constraint.ConfigConstraint;
import edu.cmu.cs.mvelezce.utils.ConfigHelper;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ConfigConstraintAnalyzer {

  private final Set<String> options;

  public ConfigConstraintAnalyzer(Set<String> options) {
    this.options = options;
  }

  public Set<ConfigConstraint> getConstraintsSatisfiedByConfig(
      ConfigConstraint executedConfigConstraint) {
    Set<ConfigConstraint> satisfiedConfigConstraints = new HashSet<>();

    Map<String, Boolean> executedPartialConfig = executedConfigConstraint.getPartialConfig();
    Set<String> options = executedPartialConfig.keySet();
    Set<Set<String>> configs = ConfigHelper.getConfigurations(options);
    configs.remove(new HashSet<String>());

    for (Set<String> config : configs) {
      ConfigConstraint configConstraint = new ConfigConstraint();

      for (String option : config) {
        configConstraint.addEntry(option, executedPartialConfig.get(option));
      }

      satisfiedConfigConstraints.add(configConstraint);
    }

    return satisfiedConfigConstraints;
  }

  public Set<Set<String>> getConfigsThatSatisfyConfigConstraints(
      Set<ConfigConstraint> configConstraintsToSatisfy,
      Set<ConfigConstraint> satisfiedConfigConstraints) {
    if (configConstraintsToSatisfy.isEmpty()) {
      return new HashSet<>();
    }

    Set<Set<String>> configs = new HashSet<>();
    Set<String> config = configConstraintsToSatisfy.iterator().next().toConfig();
    configs.add(config);

    //    List<String> constraintsToSatisfy = this.getStringConstraints(configConstraintsToSatisfy);
    //    List<String> executedConfigConstraints =
    // this.getStringConstraints(satisfiedConfigConstraints);
    //    executedConfigConstraints = this.negateConstraints(executedConfigConstraints);
    //
    //    List<String> constraints = new ArrayList<>(constraintsToSatisfy);
    //    constraints.addAll(executedConfigConstraints);
    //
    //    Set<Set<Set<String>>> satisfiableConfigsSets = MinConfigsGenerator
    //        .getSatConfigs(this.options, constraints);
    //
    //    Set<Set<String>> configs = new HashSet<>();
    //
    //    for (Set<Set<String>> satisfiableConfigs : satisfiableConfigsSets) {
    //      configs.addAll(satisfiableConfigs);
    //    }
    //
    return configs;
  }

  //  Set<Set<String>> getConfigsThatSatisfyConfigConstraints(
  //      Set<ConfigConstraint> configConstraintsToSatisfy) {
  //    return this.getConfigsThatSatisfyConfigConstraints(configConstraintsToSatisfy, new
  // HashSet<>());
  //  }
  //
  //  private List<String> getStringConstraints(Set<ConfigConstraint> configConstraints) {
  //    List<String> stringConstraints = new ArrayList<>();
  //
  //    for (ConfigConstraint configConstraint : configConstraints) {
  //      String constraint = this.getConstraint(configConstraint);
  //      stringConstraints.add(constraint);
  //    }
  //
  //    return stringConstraints;
  //  }
  //
  //  private String getConstraint(ConfigConstraint configConstraint) {
  //    StringBuilder stringBuilder = new StringBuilder();
  //    Map<String, Boolean> partialConfig = configConstraint.getPartialConfig();
  //    stringBuilder.append("(");
  //
  //    Iterator<Entry<String, Boolean>> partialConfigIter = partialConfig.entrySet().iterator();
  //
  //    while (partialConfigIter.hasNext()) {
  //      Entry<String, Boolean> entry = partialConfigIter.next();
  //
  //      if (!entry.getValue()) {
  //        stringBuilder.append("!");
  //      }
  //
  //      stringBuilder.append(entry.getKey());
  //
  //      if (partialConfigIter.hasNext()) {
  //        stringBuilder.append(" && ");
  //      }
  //    }
  //
  //    stringBuilder.append(")");
  //
  //    return stringBuilder.toString();
  //  }
  //
  //  private List<String> negateConstraints(List<String> constraints) {
  //    List<String> negatedConstraints = new ArrayList<>();
  //
  //    for (String constraint : constraints) {
  //      constraint = "!" + constraint;
  //      negatedConstraints.add(constraint);
  //    }
  //
  //    return negatedConstraints;
  //  }

}
