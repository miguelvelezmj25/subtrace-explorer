package edu.cmu.cs.mvelezce.explorer.idta.results.statement;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.cc.DecisionTaints;
import edu.cmu.cs.mvelezce.explorer.idta.IDTA;
import edu.cmu.cs.mvelezce.explorer.idta.results.statement.info.ControlFlowStatementInfo;
import edu.cmu.cs.mvelezce.explorer.idta.results.statement.info.ControlFlowStatementTaints;
import edu.cmu.cs.mvelezce.explorer.idta.taint.InfluencingTaints;
import edu.cmu.cs.mvelezce.utils.Options;

import java.io.File;
import java.io.IOException;
import java.util.*;

// TODO might need to know what configurations will be executed to know what can be removed from
// instrumentation
public class ControlFlowInfluencingTaintsAnalysis
    extends ControlFlowAnalysis<Set<ControlFlowStatementTaints>, InfluencingTaints> {

  public ControlFlowInfluencingTaintsAnalysis(String programName, List<String> options) {
    super(programName, options);
  }

  ControlFlowInfluencingTaintsAnalysis(String programName) {
    this(programName, new ArrayList<>());
  }

  @Override
  public Set<ControlFlowStatementTaints> analyze() throws IOException, InterruptedException {
    System.err.println("Check that we remove the control flow taints from the data flow taints");
    Set<ControlFlowStatementTaints> results = new HashSet<>();
    Map<String, Set<InfluencingTaints>> statementsToTaints = this.getStatementsToData();

    for (Map.Entry<String, Set<InfluencingTaints>> entry : statementsToTaints.entrySet()) {
      String statement = entry.getKey();
      String[] statementComponents = statement.split("\\.");

      String packageName = getPackageName(statementComponents[0]);
      String className = getClassName(statementComponents[0]);
      String methodSignature = statementComponents[1];
      int decisionIndex = Integer.parseInt(statementComponents[2]);

      ControlFlowStatementTaints controlFlowStatementConstraint =
          new ControlFlowStatementTaints(
              packageName, className, methodSignature, decisionIndex, entry.getValue());
      results.add(controlFlowStatementConstraint);
    }

    return results;
  }

  public void saveTaints(Set<DecisionTaints> decisionTaints) {
    this.addStatements(decisionTaints);
    Map<String, Set<InfluencingTaints>> statementsToInfluencingTaints =
        this.addInfluencingTaints(decisionTaints);

    Map<String, Set<InfluencingTaints>> data = this.getStatementsToData();

    for (Map.Entry<String, Set<InfluencingTaints>> entry :
        statementsToInfluencingTaints.entrySet()) {
      String statement = entry.getKey();
      Set<InfluencingTaints> influencingTaints = entry.getValue();

      data.put(statement, new HashSet<>(influencingTaints));
    }
  }

  @Override
  public void writeToFile(Set<ControlFlowStatementTaints> controlFlowInfos) throws IOException {
    String outputFile = this.outputDir() + "/" + this.getProgramName() + Options.DOT_JSON;
    File file = new File(outputFile);
    file.getParentFile().mkdirs();

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(file, controlFlowInfos);
  }

  @Override
  public Set<ControlFlowStatementTaints> readFromFile(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    return mapper.readValue(file, new TypeReference<Set<ControlFlowStatementInfo>>() {});
  }

  @Override
  public String outputDir() {
    return IDTA.OUTPUT_DIR + "/analysis/" + this.getProgramName() + "/cc/statements";
  }

  //  private void mergeTaints() {
  //    this.mergeInfluencingTaints();
  //    this.addToMatchingTypeDataStructure();
  //  }
  //
  //  private void addToMatchingTypeDataStructure() {
  //    for (Map.Entry<String, Set<InfluencingTaints>> entry :
  // this.statementsToOptionsSet.entrySet()) {
  //      this.statementsToOptions.put(entry.getKey(), entry.getValue().iterator().next());
  //    }
  //  }
  //
  //  private void mergeInfluencingTaints() {
  //    for (Map.Entry<String, Set<InfluencingTaints>> entry :
  // this.statementsToOptionsSet.entrySet()) {
  //      Set<String> mergedContextTaints = new HashSet<>();
  //      Set<String> mergedConditionTaints = new HashSet<>();
  //
  //      for (InfluencingTaints influencingTaints : entry.getValue()) {
  //        Set<String> contextTaints = influencingTaints.getContext();
  //        mergedContextTaints.addAll(contextTaints);
  //
  //        Set<String> conditionTaints = influencingTaints.getCondition();
  //        mergedConditionTaints.addAll(conditionTaints);
  //      }
  //
  //      InfluencingTaints newInfluencingTaints = new InfluencingTaints(mergedContextTaints,
  //          mergedConditionTaints);
  //      Set<InfluencingTaints> setWithOnlyOneInfluencingTaint = new HashSet<>();
  //      setWithOnlyOneInfluencingTaint.add(newInfluencingTaints);
  //
  //      this.statementsToOptionsSet.put(entry.getKey(), setWithOnlyOneInfluencingTaint);
  //    }
  //  }
  //
  //  private void updatedConditionTaintsIfremoveContextTaintsInConditionTaints() {
  //    for (Map.Entry<String, Set<InfluencingTaints>> entry : this.statementsToOptions.entrySet())
  // {
  //      Set<InfluencingTaints> setOfInfluencingTaints = entry.getValue();
  //
  //      if (setOfInfluencingTaints.size() <= 1) {
  //        continue;
  //      }
  //
  //      Set<String> superSetOfConditionTaints = new HashSet<>();
  //
  //      for (InfluencingTaints influencingTaints : setOfInfluencingTaints) {
  //        Set<String> conditionTaints = influencingTaints.getCondition();
  //
  //        if (conditionTaints.isEmpty()) {
  //          superSetOfConditionTaints = new HashSet<>();
  //          break;
  //        }
  //
  //        if (conditionTaints.containsAll(superSetOfConditionTaints) || superSetOfConditionTaints
  //            .containsAll(conditionTaints)) {
  //          superSetOfConditionTaints.addAll(conditionTaints);
  //        }
  //      }
  //
  //      // TODO update the context with the super set
  //      if (!superSetOfConditionTaints.isEmpty()) {
  //        System.out.println();
  //      }
  //    }
  //  }
  //
  //  private void removeContextOptionsInConditionOptions() {
  //    for (Map.Entry<String, InfluencingTaints> entry : this.statementsToOptions.entrySet()) {
  //      InfluencingTaints influencingTaints = entry.getValue();
  //      InfluencingTaints newInfluencingTaints = this.getNewInfluencingTaint(influencingTaints);
  //      this.statementsToOptions.put(entry.getKey(), newInfluencingTaints);
  //    }
  //  }
  //
  //  private InfluencingTaints getNewInfluencingTaint(InfluencingTaints influencingTaints) {
  //    Set<String> contextTaints = influencingTaints.getContext();
  //    Set<String> conditionTaints = influencingTaints.getCondition();
  //
  //    Set<String> newConditionTaints = new HashSet<>(conditionTaints);
  //    newConditionTaints.removeAll(contextTaints);
  //
  //    return new InfluencingTaints(contextTaints, newConditionTaints);
  //  }
  //
  //  private void checkContextTaintsNotEmpty() {
  //    for (Map.Entry<String, Set<InfluencingTaints>> entry : this.statementsToOptions.entrySet())
  // {
  //      Set<InfluencingTaints> setOfInfluencingTaints = entry.getValue();
  //
  //      if (setOfInfluencingTaints.size() <= 1) {
  //        continue;
  //      }
  //
  //      Set<String> superSetOfContextTaints = new HashSet<>();
  //
  //      for (InfluencingTaints influencingTaints : setOfInfluencingTaints) {
  //        Set<String> contextTaints = influencingTaints.getContext();
  //
  //        if (contextTaints.isEmpty()) {
  //          superSetOfContextTaints = new HashSet<>();
  //          break;
  //        }
  //
  //        if (contextTaints.containsAll(superSetOfContextTaints) || superSetOfContextTaints
  //            .containsAll(contextTaints)) {
  //          superSetOfContextTaints.addAll(contextTaints);
  //        }
  //      }
  //
  //      // TODO update the context with the super set
  //      if (!superSetOfContextTaints.isEmpty()) {
  //        System.out.println();
  //      }
  //    }
  //  }
  //
  //  // If the context is the same of all influencing taints, but the conditions are different and
  //  // we do not sample all combos of the conditions, we do not know what execution belongs to
  // which options.
  //  private void checkIfProblematicEntry() {
  //    for (Map.Entry<String, Set<InfluencingTaints>> entry : this.statementsToOptions.entrySet())
  // {
  //      Set<Set<String>> allContextTaints = new HashSet<>();
  //      Set<InfluencingTaints> setOfInfluencingTaints = entry.getValue();
  //
  //      for (InfluencingTaints influencingTaints : setOfInfluencingTaints) {
  //        Set<String> contextTaints = influencingTaints.getContext();
  //        allContextTaints.add(contextTaints);
  //      }
  //
  //      if (setOfInfluencingTaints.size() > 1 && allContextTaints.size() == 1) {
  //        // MIGHT BE A PROBLEM
  //        System.out.println();
  //      }
  //    }
  //  }
  //
  //  private void removeStatementsWithOnlyEmptyConditionTaints() {
  //    Set<String> statementsToRemove = new HashSet<>();
  //
  //    for (Map.Entry<String, Set<InfluencingTaints>> entry :
  // this.statementsToOptionsSet.entrySet()) {
  //      Set<String> allConditionTaints = new HashSet<>();
  //      Set<InfluencingTaints> setOfInfluencingTaints = entry.getValue();
  //
  //      for (InfluencingTaints influencingTaints : setOfInfluencingTaints) {
  //        Set<String> conditionTaints = influencingTaints.getCondition();
  //        allConditionTaints.addAll(conditionTaints);
  //      }
  //
  //      if (allConditionTaints.isEmpty()) {
  //        statementsToRemove.add(entry.getKey());
  //      }
  //    }
  //
  //    this.statementsToOptionsSet.keySet().removeAll(statementsToRemove);
  //  }
  //
  //  private void removeStatementsWithOnlyEmptyConditionOptions() {
  //    Set<String> statementsToRemove = new HashSet<>();
  //
  //    for (Map.Entry<String, InfluencingTaints> entry : this.statementsToOptions.entrySet()) {
  //      InfluencingTaints influencingTaints = entry.getValue();
  //      Set<String> conditionTaints = influencingTaints.getCondition();
  //
  //      if (conditionTaints.isEmpty()) {
  //        statementsToRemove.add(entry.getKey());
  //      }
  //    }
  //
  //    this.statementsToOptions.keySet().removeAll(statementsToRemove);
  //  }
}
