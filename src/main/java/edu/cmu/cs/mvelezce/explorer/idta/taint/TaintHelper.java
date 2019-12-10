package edu.cmu.cs.mvelezce.explorer.idta.taint;

import edu.cmu.cs.mvelezce.explorer.idta.results.parser.DecisionTaints;
import edu.columbia.cs.psl.phosphor.runtime.Taint;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TaintHelper {

  private TaintHelper() {}

  public static Set<String> getContextTaints(DecisionTaints decisionTaints, List<String> options) {
    @Nullable Taint contextTaintObject = decisionTaints.getExecCtxTaints();
    Set<String> contextTaints = new HashSet<>();

    if (contextTaintObject != null) {
      contextTaints = getTaintingOptions(contextTaintObject, options);
    }

    return contextTaints;
  }

  public static Set<String> getConditionTaints(
      DecisionTaints decisionTaints, List<String> options) {
    @Nullable Taint conditionTaintObject = decisionTaints.getConditionTaints();
    Set<String> contextTaints = new HashSet<>();

    if (conditionTaintObject != null) {
      contextTaints = getTaintingOptions(conditionTaintObject, options);
    }

    return contextTaints;
  }

  private static Set<String> getTaintingOptions(Taint taint, List<String> options) {
    Set<String> taintingOptions = new HashSet<>();
    Object[] tags = taint.getLabels();

    if (tags == null) {
      throw new RuntimeException("The tags array is null");
    }

    for (Object o : tags) {
      int tag = (int) o;
      String taintingOption = options.get(tag);
      taintingOptions.add(taintingOption);
    }

    return taintingOptions;
  }
}
