package edu.cmu.cs.mvelezce.explorer.eval.constraints.subtraces;

import edu.cmu.cs.mvelezce.adapters.iGen.BaseIGenAdapter;
import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.adapters.pngtastic.BasePngtasticAdapter;
import edu.cmu.cs.mvelezce.adapters.trivial.BaseTrivialAdapter;
import edu.cmu.cs.mvelezce.explorer.gt.valueanalysis.SubtraceAnalysisInfo;
import edu.cmu.cs.mvelezce.explorer.gt.valueanalysis.SubtracesValueAnalysis;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class SubtracesConstraintsAnalyzerTest {

  private void analyzeConstraints(String programName, Set<String> options) throws Exception {
    SubtracesValueAnalysis subtracesValueAnalysis = new SubtracesValueAnalysis(programName);
    String[] args = new String[0];
    Set<SubtraceAnalysisInfo> subtraceAnalysisInfos = subtracesValueAnalysis.analyze(args);
    SubtracesConstraintsAnalyzer analysis =
        new SubtracesConstraintsAnalyzer(programName, subtraceAnalysisInfos, options);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    Set<SubtraceOutcomeConstraint> write = analysis.analyze(args);

    analysis = new SubtracesConstraintsAnalyzer(programName);
    args = new String[0];
    Set<SubtraceOutcomeConstraint> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void trivial() throws Exception {
    String programName = BaseTrivialAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(BaseTrivialAdapter.getListOfOptions());
    analyzeConstraints(programName, options);
  }

  @Test
  public void pngtasticCounter() throws Exception {
    String programName = BasePngtasticAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(BasePngtasticAdapter.getListOfOptions());
    analyzeConstraints(programName, options);
  }

  @Test
  public void iGen() throws Exception {
    String programName = BaseIGenAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(BaseIGenAdapter.getListOfOptions());
    analyzeConstraints(programName, options);
  }

  @Test
  public void measuredDiskOrderedScan() throws Exception {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(BaseMeasureDiskOrderedScanAdapter.getListOfOptions());
    analyzeConstraints(programName, options);
  }

  @Test
  public void indexFiles() throws Exception {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(BaseIndexFilesAdapter.getListOfOptions());
    analyzeConstraints(programName, options);
  }
}
