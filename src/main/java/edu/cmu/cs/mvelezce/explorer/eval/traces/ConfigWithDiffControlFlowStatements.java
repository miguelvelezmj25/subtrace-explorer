package edu.cmu.cs.mvelezce.explorer.eval.traces;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConfigWithDiffControlFlowStatements {

  private final Set<String> config;
  private final List<String> diffControlFlowStatements;

  // Dummy constructor for faster xml
  public ConfigWithDiffControlFlowStatements() {
    this.config = new HashSet<>();
    this.diffControlFlowStatements = new ArrayList<>();
  }

  public ConfigWithDiffControlFlowStatements(
      Set<String> config, List<String> diffControlFlowStatements) {
    this.config = config;
    this.diffControlFlowStatements = diffControlFlowStatements;
  }

  public List<String> getDiffControlFlowStatements() {
    return diffControlFlowStatements;
  }

  public Set<String> getConfig() {
    return config;
  }
}
