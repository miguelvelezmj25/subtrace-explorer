package edu.cmu.cs.mvelezce.explorer.eval.constraints.idta;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.MinConfigsGenerator;
import edu.cmu.cs.mvelezce.explorer.eval.constraints.idta.constraint.ConfigConstraint;

import java.util.*;
import java.util.Map.Entry;

public class IDTAConstraintsAnalyzer extends ConstraintAnalyzer {

  private final Set<ConfigConstraint> configConstraints;

  IDTAConstraintsAnalyzer(
      String programName, Set<ConfigConstraint> configConstraints, Set<String> options) {
    super(programName, options);

    this.configConstraints = configConstraints;
  }

  public IDTAConstraintsAnalyzer(String programName) {
    this(programName, new HashSet<>(), new HashSet<>());
  }

  @Override
  public Set<FeatureExpr> analyze() {
    List<String> stringConstraints = this.getStringConstraints();

    return new HashSet<>(MinConfigsGenerator.getFeatureExprs(stringConstraints));
  }

  private List<String> getStringConstraints() {
    System.err.println("This code was copied and pasted");
    List<String> stringConstraints = new ArrayList<>();

    for (ConfigConstraint configConstraint : this.configConstraints) {
      String constraint = this.getConstraint(configConstraint);
      stringConstraints.add(constraint);
    }

    return stringConstraints;
  }

  private String getConstraint(ConfigConstraint configConstraint) {
    StringBuilder stringBuilder = new StringBuilder();
    Map<String, Boolean> partialConfig = configConstraint.getPartialConfig();
    stringBuilder.append("(");

    Iterator<Entry<String, Boolean>> partialConfigIter = partialConfig.entrySet().iterator();

    while (partialConfigIter.hasNext()) {
      Entry<String, Boolean> entry = partialConfigIter.next();

      if (!entry.getValue()) {
        stringBuilder.append("!");
      }

      stringBuilder.append(entry.getKey());

      if (partialConfigIter.hasNext()) {
        stringBuilder.append(" && ");
      }
    }

    stringBuilder.append(")");

    return stringBuilder.toString();
  }

  @Override
  public String outputDir() {
    return super.outputDir() + "/idta/" + this.getProgramName();
  }
}
