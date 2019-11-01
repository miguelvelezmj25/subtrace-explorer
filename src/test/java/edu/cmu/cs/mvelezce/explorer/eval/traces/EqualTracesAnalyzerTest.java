package edu.cmu.cs.mvelezce.explorer.eval.traces;

import edu.cmu.cs.mvelezce.adapter.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.adapter.adapters.trivial.BaseTrivialAdapter;
import edu.cmu.cs.mvelezce.analysis.dynamic.DynamicAnalysis;
import edu.cmu.cs.mvelezce.utils.configurations.ConfigHelper;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class EqualTracesAnalyzerTest {

  @Test
  public void trivial_all() throws IOException, InterruptedException {
    String programName = BaseTrivialAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(BaseTrivialAdapter.getListOfOptions());
    Set<Set<String>> configs = ConfigHelper.getConfigurations(options);

    for (Set<String> config : configs) {
      DynamicAnalysis analysis = new EqualTracesAnalyzer(programName, config);

      String[] args = new String[3];
      args[0] = "-delres";
      args[1] = "-saveres";
      args[2] = "-i 5";
      analysis.analyze(args);
    }
  }

  @Test
  public void trivial_1() throws IOException, InterruptedException {
    String programName = BaseTrivialAdapter.PROGRAM_NAME;
    Set<String> config = new HashSet<>();
    DynamicAnalysis analysis = new EqualTracesAnalyzer(programName, config);

    String[] args = new String[3];
    args[0] = "-delres";
    args[1] = "-saveres";
    args[2] = "-i 2";
    analysis.analyze(args);
  }

  @Test
  public void berkeley_db_all() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(BaseMeasureDiskOrderedScanAdapter.getListOfOptions());
    Set<Set<String>> configs = ConfigHelper.getConfigurations(options);

    for (Set<String> config : configs) {
      DynamicAnalysis analysis = new EqualTracesAnalyzer(programName, config);

      String[] args = new String[3];
      args[0] = "-delres";
      args[1] = "-saveres";
      args[2] = "-i 5";
      analysis.analyze(args);
    }
  }

  @Test
  public void berkeley_db_1() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    Set<String> config = new HashSet<>();
    DynamicAnalysis analysis = new EqualTracesAnalyzer(programName, config);

    String[] args = new String[3];
    args[0] = "-delres";
    args[1] = "-saveres";
    args[2] = "-i 5";
    analysis.analyze(args);
  }
}
