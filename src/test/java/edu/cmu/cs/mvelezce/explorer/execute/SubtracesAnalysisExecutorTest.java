package edu.cmu.cs.mvelezce.explorer.execute;

import edu.cmu.cs.mvelezce.adapter.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapter.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.adapter.adapters.trivial.BaseTrivialAdapter;
import edu.cmu.cs.mvelezce.analysis.dynamic.DynamicAnalysis;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SubtracesAnalysisExecutorTest {

  @Test
  public void trivial() throws IOException, InterruptedException {
    String programName = BaseTrivialAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(BaseTrivialAdapter.getListOfOptions());
    DynamicAnalysis<Map<Set<String>, List<String>>> analysis =
        new SubtracesAnalysisExecutor(programName, options);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    analysis.analyze(args);

    analysis = new SubtracesAnalysisExecutor(programName);
    args = new String[0];
    Map<Set<String>, List<String>> read = analysis.analyze(args);

    Assert.assertFalse(read.isEmpty());
  }

  @Test
  public void MeasureDiskOrderedScan() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(BaseMeasureDiskOrderedScanAdapter.getListOfOptions());
    DynamicAnalysis<Map<Set<String>, List<String>>> analysis =
        new SubtracesAnalysisExecutor(programName, options);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    analysis.analyze(args);
  }

  @Test
  public void indexFiles() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(BaseIndexFilesAdapter.getListOfOptions());
    DynamicAnalysis<Map<Set<String>, List<String>>> analysis =
        new SubtracesAnalysisExecutor(programName, options);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    analysis.analyze(args);
  }
}
