package edu.cmu.cs.mvelezce.explorer.idta;

import edu.cmu.cs.mvelezce.adapter.adapters.trivial.BaseTrivialAdapter;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IDTATest {

  @Test
  public void Trivial() throws IOException, InterruptedException {
    String programName = BaseTrivialAdapter.PROGRAM_NAME;
    List<String> options = BaseTrivialAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, options, initialConfig);
    analysis.analyze(args);
  }
}
