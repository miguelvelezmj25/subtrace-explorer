package edu.cmu.cs.mvelezce.explorer.idta.taint;

import edu.cmu.cs.mvelezce.explorer.idta.results.parser.DecisionTaints;
import edu.columbia.cs.psl.phosphor.runtime.Taint;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TaintHelper {

  private TaintHelper() {}

  public static Set<String> getControlTaints(DecisionTaints decisionTaints, List<String> options) {
    Taint controlTaintsObject = decisionTaints.getControlTaints();
    Set<String> controlTaints = new HashSet<>();

    if (controlTaintsObject != null) {
      controlTaints = getTaintingOptions(controlTaintsObject, options);
    }

    return controlTaints;
  }

  public static Set<String> getDataTaints(DecisionTaints decisionTaints, List<String> options) {
    Taint dataTaintsObject = decisionTaints.getDataTaints();
    Set<String> dataTaints = new HashSet<>();

    if (dataTaintsObject != null) {
      dataTaints = getTaintingOptions(dataTaintsObject, options);
    }

    return dataTaints;
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
