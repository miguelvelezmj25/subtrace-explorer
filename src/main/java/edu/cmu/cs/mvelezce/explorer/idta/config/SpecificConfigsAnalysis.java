package edu.cmu.cs.mvelezce.explorer.idta.config;

import edu.cmu.cs.mvelezce.explorer.idta.constraint.Constraint;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class SpecificConfigsAnalysis extends ConfigAnalysis {

  private final Set<Set<String>> configsToExecute;
  private final Iterator<Set<String>> configsToExecuteIter;

  public SpecificConfigsAnalysis(
      String programName,
      String workloadSize,
      List<String> options,
      Set<Set<String>> configsToExecute) {
    super(programName, workloadSize, options);

    this.configsToExecute = configsToExecute;
    this.configsToExecuteIter = configsToExecute.iterator();
  }

  @Nullable
  public Set<String> getNextConfig(Set<Constraint> constraintsToExplore) {
    if (this.configsToExecuteIter.hasNext()) {
      return this.configsToExecuteIter.next();
    }

    return null;
  }

  @Override
  public Set<String> getInitialConfig() {
    Set<String> initialConfig = this.getNextConfig(new HashSet<>());

    if (initialConfig == null) {
      throw new RuntimeException("The initial config cannot be null");
    }

    return initialConfig;
  }
}
