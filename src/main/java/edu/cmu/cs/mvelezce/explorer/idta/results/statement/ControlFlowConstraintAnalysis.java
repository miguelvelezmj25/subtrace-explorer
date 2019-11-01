package edu.cmu.cs.mvelezce.explorer.idta.results.statement;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.MinConfigsGenerator;
import edu.cmu.cs.mvelezce.cc.DecisionTaints;
import edu.cmu.cs.mvelezce.explorer.idta.IDTA;
import edu.cmu.cs.mvelezce.explorer.idta.results.statement.info.ControlFlowStatementConstraints;
import edu.cmu.cs.mvelezce.explorer.idta.results.statement.info.ControlFlowStatementConstraintsPretty;
import edu.cmu.cs.mvelezce.explorer.idta.taint.InfluencingTaints;
import edu.cmu.cs.mvelezce.explorer.utils.ConstraintUtils;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ControlFlowConstraintAnalysis
    extends ControlFlowAnalysis<Set<ControlFlowStatementConstraints>, FeatureExpr> {

  public ControlFlowConstraintAnalysis(String programName, List<String> options) {
    super(programName, options);
  }

  @Override
  public Set<ControlFlowStatementConstraints> analyze() {
    Set<ControlFlowStatementConstraints> results = new HashSet<>();
    Map<String, Set<FeatureExpr>> statementsToConstraints = this.getStatementsToData();

    for (Map.Entry<String, Set<FeatureExpr>> entry : statementsToConstraints.entrySet()) {
      if (entry.getValue().isEmpty()) {
        continue;
      }

      String statement = entry.getKey();
      String[] statementComponents = statement.split("\\.");

      String packageName = getPackageName(statementComponents[0]);
      String className = getClassName(statementComponents[0]);
      String methodSignature = statementComponents[1];
      int decisionIndex = Integer.parseInt(statementComponents[2]);

      ControlFlowStatementConstraints controlFlowStatementConstraint =
          new ControlFlowStatementConstraints(
              packageName, className, methodSignature, decisionIndex, entry.getValue());
      results.add(controlFlowStatementConstraint);
    }

    return results;
  }

  public void saveConstraints(Set<DecisionTaints> decisionTaints, Set<String> config) {
    this.addStatements(decisionTaints);
    this.addConstraints(decisionTaints, config);
  }

  private void addConstraints(Set<DecisionTaints> decisionTaints, Set<String> config) {
    Map<String, Set<InfluencingTaints>> statementsToInfluencingTaints =
        this.addInfluencingTaints(decisionTaints);
    this.deriveConstraints(statementsToInfluencingTaints, config);
  }

  private void deriveConstraints(
      Map<String, Set<InfluencingTaints>> statementsToInfluencingTaints, Set<String> config) {
    Map<String, Set<FeatureExpr>> statementsToConstraints = this.getStatementsToData();

    for (Map.Entry<String, Set<InfluencingTaints>> entry :
        statementsToInfluencingTaints.entrySet()) {
      Set<InfluencingTaints> influencingTaints = entry.getValue();
      List<String> stringConstraints = this.getStringConstraints(influencingTaints, config);
      Set<FeatureExpr> constraints = this.parseStringConstraintsAsConstraints(stringConstraints);
      statementsToConstraints.get(entry.getKey()).addAll(constraints);
    }
  }

  private Set<FeatureExpr> parseStringConstraintsAsConstraints(List<String> stringConstraints) {
    Set<FeatureExpr> constraints = new HashSet<>();

    for (String stringConstraint : stringConstraints) {
      FeatureExpr constraint = MinConfigsGenerator.parseAsFeatureExpr(stringConstraint);
      constraints.add(constraint);
    }

    return constraints;
  }

  private List<String> getStringConstraints(
      Set<InfluencingTaints> influencingTaints, Set<String> config) {
    List<String> stringConstraints = new ArrayList<>();

    for (InfluencingTaints influencingTaint : influencingTaints) {
      influencingTaint = this.removeControlTaintsFromDataTaints(influencingTaint);
      Set<String> context = influencingTaint.getContext();
      Set<String> condition = influencingTaint.getCondition();

      if (condition.isEmpty()) {
        continue;
      }

      stringConstraints.addAll(ConstraintUtils.getStringConstraints(context, condition, config));
    }

    return stringConstraints;
  }

  private InfluencingTaints removeControlTaintsFromDataTaints(InfluencingTaints influencingTaint) {
    Set<String> context = influencingTaint.getContext();

    if (context.isEmpty()) {
      return influencingTaint;
    }

    Set<String> condition = influencingTaint.getCondition();
    Set<String> newCondition = new HashSet<>(condition);
    newCondition.removeAll(context);

    return new InfluencingTaints(context, newCondition);
  }

  @Override
  public void writeToFile(Set<ControlFlowStatementConstraints> results) throws IOException {
    String outputFile = this.outputDir() + "/" + this.getProgramName() + Options.DOT_JSON;
    File file = new File(outputFile);
    file.getParentFile().mkdirs();

    Set<ControlFlowStatementConstraintsPretty> prettyResults = new HashSet<>();

    for (ControlFlowStatementConstraints entry : results) {
      Set<String> prettyConstraints = new HashSet<>();
      Set<FeatureExpr> constraints = entry.getInfo();

      for (FeatureExpr constraint : constraints) {
        String prettyConstraint =
            ConstraintUtils.prettyPrintFeatureExpr(constraint, this.getOptions());
        prettyConstraints.add(prettyConstraint);
      }

      ControlFlowStatementConstraintsPretty prettyResult =
          new ControlFlowStatementConstraintsPretty(
              entry.getPackageName(),
              entry.getClassName(),
              entry.getMethodSignature(),
              entry.getDecisionIndex(),
              prettyConstraints);
      prettyResults.add(prettyResult);
    }

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(file, prettyResults);
  }

  @Override
  public Set<ControlFlowStatementConstraints> readFromFile(File file) throws IOException {
    //    ObjectMapper mapper = new ObjectMapper();
    //
    //    return mapper.readValue(file, new TypeReference<Set<ControlFlowStatementInfo>>() {});
    throw new UnsupportedOperationException("implement");
  }

  @Override
  public String outputDir() {
    return IDTA.OUTPUT_DIR + "/analysis/" + this.getProgramName() + "/cc/dataFlowConstraints";
  }
}
