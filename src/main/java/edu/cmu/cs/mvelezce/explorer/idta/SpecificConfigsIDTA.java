package edu.cmu.cs.mvelezce.explorer.idta;

import java.util.List;
import java.util.Set;

public class SpecificConfigsIDTA extends IDTA {

  public SpecificConfigsIDTA(
      String programName,
      String workloadSize,
      List<String> options,
      Set<Set<String>> configsToExecute) {
    super(programName, "specific/" + workloadSize, configsToExecute, options);

    System.out.println(configsToExecute.size() + " to execute");
  }
}
