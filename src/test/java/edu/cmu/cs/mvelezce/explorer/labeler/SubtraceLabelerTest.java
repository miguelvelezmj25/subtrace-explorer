package edu.cmu.cs.mvelezce.explorer.labeler;

import edu.cmu.cs.mvelezce.adapter.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapter.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.adapter.adapters.trivial.BaseTrivialAdapter;
import edu.cmu.cs.mvelezce.explorer.execute.SubtracesAnalysisExecutor;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SubtraceLabelerTest {

  @Test
  public void trivial() throws IOException, InterruptedException {
    String programName = BaseTrivialAdapter.PROGRAM_NAME;
    SubtracesAnalysisExecutor analysis = new SubtracesAnalysisExecutor(programName);

    String[] args = new String[0];
    Map<Set<String>, List<String>> configsToTraces = analysis.analyze(args);

    SubtraceLabeler subtraceLabeler = new SubtraceLabeler(programName, configsToTraces);
    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    Map<Set<String>, List<String>> write = subtraceLabeler.analyze(args);

    subtraceLabeler = new SubtraceLabeler(programName);
    args = new String[0];
    Map<Set<String>, List<String>> read = subtraceLabeler.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void MeasureDiskOrderedScan() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    SubtracesAnalysisExecutor analysis = new SubtracesAnalysisExecutor(programName);

    String[] args = new String[0];
    Map<Set<String>, List<String>> configsToTraces = analysis.analyze(args);

    SubtraceLabeler subtraceLabeler = new SubtraceLabeler(programName, configsToTraces);
    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    subtraceLabeler.analyze(args);
  }

  @Test
  public void indexFiles() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    SubtracesAnalysisExecutor analysis = new SubtracesAnalysisExecutor(programName);

    String[] args = new String[0];
    Map<Set<String>, List<String>> configsToTraces = analysis.analyze(args);

    SubtraceLabeler subtraceLabeler = new SubtraceLabeler(programName, configsToTraces);
    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    subtraceLabeler.analyze(args);
  }
}
