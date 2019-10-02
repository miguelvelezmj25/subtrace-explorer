package edu.cmu.cs.mvelezce.explorer.gt.valueanalysis;

import edu.cmu.cs.mvelezce.adapter.adapters.iGen.BaseIGenAdapter;
import edu.cmu.cs.mvelezce.adapter.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapter.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.adapter.adapters.pngtastic.BasePngtasticAdapter;
import edu.cmu.cs.mvelezce.adapter.adapters.trivial.BaseTrivialAdapter;
import edu.cmu.cs.mvelezce.explorer.gt.labeler.SubtraceLabeler;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SubtracesValueAnalysisTest {

  @Test
  public void trivial() throws IOException, InterruptedException {
    String programName = BaseTrivialAdapter.PROGRAM_NAME;
    SubtraceLabeler subtraceLabeler = new SubtraceLabeler(programName);

    String[] args = new String[0];
    Map<Set<String>, List<String>> configsToLabeledTraces = subtraceLabeler.analyze(args);
    SubtracesValueAnalysis subtracesValueAnalysis =
        new SubtracesValueAnalysis(programName, configsToLabeledTraces);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    Set<SubtraceAnalysisInfo> write = subtracesValueAnalysis.analyze(args);

    subtracesValueAnalysis = new SubtracesValueAnalysis(programName);
    args = new String[0];
    Set<SubtraceAnalysisInfo> read = subtracesValueAnalysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void iGen() throws IOException, InterruptedException {
    String programName = BaseIGenAdapter.PROGRAM_NAME;
    SubtraceLabeler subtraceLabeler = new SubtraceLabeler(programName);

    String[] args = new String[0];
    Map<Set<String>, List<String>> configsToLabeledTraces = subtraceLabeler.analyze(args);
    SubtracesValueAnalysis subtracesValueAnalysis =
        new SubtracesValueAnalysis(programName, configsToLabeledTraces);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    Set<SubtraceAnalysisInfo> write = subtracesValueAnalysis.analyze(args);

    subtracesValueAnalysis = new SubtracesValueAnalysis(programName);
    args = new String[0];
    Set<SubtraceAnalysisInfo> read = subtracesValueAnalysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void pngtasticCounter() throws IOException, InterruptedException {
    String programName = BasePngtasticAdapter.PROGRAM_NAME;
    SubtraceLabeler subtraceLabeler = new SubtraceLabeler(programName);

    String[] args = new String[0];
    Map<Set<String>, List<String>> configsToLabeledTraces = subtraceLabeler.analyze(args);
    SubtracesValueAnalysis subtracesValueAnalysis =
        new SubtracesValueAnalysis(programName, configsToLabeledTraces);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    subtracesValueAnalysis.analyze(args);
  }

  @Test
  public void MeasureDiskOrderedScanAdapter() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    SubtraceLabeler subtraceLabeler = new SubtraceLabeler(programName);

    String[] args = new String[0];
    Map<Set<String>, List<String>> configsToLabeledTraces = subtraceLabeler.analyze(args);
    SubtracesValueAnalysis subtracesValueAnalysis =
        new SubtracesValueAnalysis(programName, configsToLabeledTraces);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    subtracesValueAnalysis.analyze(args);
  }

  @Test
  public void indexFiles() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    SubtraceLabeler subtraceLabeler = new SubtraceLabeler(programName);

    String[] args = new String[0];
    Map<Set<String>, List<String>> configsToLabeledTraces = subtraceLabeler.analyze(args);
    SubtracesValueAnalysis subtracesValueAnalysis =
        new SubtracesValueAnalysis(programName, configsToLabeledTraces);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    subtracesValueAnalysis.analyze(args);
  }
}
