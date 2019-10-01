package edu.cmu.cs.mvelezce.explorer.gt.valueanalysis;

import java.util.Map;
import java.util.Set;

public class SubtraceAnalysisInfo {

  private String subtrace;
  private Map<String, Set<Set<String>>> valuesToConfigs;

  // Dummy constructor needed for jackson xml
  private SubtraceAnalysisInfo() {}

  public SubtraceAnalysisInfo(String subtrace, Map<String, Set<Set<String>>> valuesToConfigs) {
    this.subtrace = subtrace;
    this.valuesToConfigs = valuesToConfigs;
  }

  public String getSubtrace() {
    return subtrace;
  }

  public Map<String, Set<Set<String>>> getValuesToConfigs() {
    return valuesToConfigs;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    SubtraceAnalysisInfo subtraceAnalysisInfo = (SubtraceAnalysisInfo) o;

    if (!subtrace.equals(subtraceAnalysisInfo.subtrace)) {
      return false;
    }
    return valuesToConfigs.equals(subtraceAnalysisInfo.valuesToConfigs);
  }

  @Override
  public int hashCode() {
    int result = subtrace.hashCode();
    result = 31 * result + valuesToConfigs.hashCode();
    return result;
  }
}
