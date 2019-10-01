package edu.cmu.cs.mvelezce.explorer.eval.traces;

import edu.cmu.cs.mvelezce.adapter.adapters.trivial.BaseTrivialAdapter;
import edu.cmu.cs.mvelezce.analysis.dynamic.DynamicAnalysis;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class EqualTracesAnalyzerTest {

  @Test
  public void trivial() throws IOException, InterruptedException {
    String programName = BaseTrivialAdapter.PROGRAM_NAME;
    Set<String> config = new HashSet<>();
    DynamicAnalysis analysis = new EqualTracesAnalyzer(programName, config);

    String[] args = new String[3];
    args[0] = "-delres";
    args[1] = "-saveres";
    args[2] = "-i 2";
    analysis.analyze(args);
  }
}
