package edu.cmu.cs.mvelezce.explorer.idta;

import edu.cmu.cs.mvelezce.adapters.canExpandConstraintsDown.BaseCanExpandConstraintsDownAdapter;
import edu.cmu.cs.mvelezce.adapters.canRemoveNestedConstraintsMultipleCallSites.BaseCanRemoveNestedConstraintsMultipleCallSitesAdapter;
import edu.cmu.cs.mvelezce.adapters.cannotExpandConstraintsDown.BaseCannotExpandConstraintsDownAdapter;
import edu.cmu.cs.mvelezce.adapters.cannotRemoveNestedRegions.BaseCannotRemoveNestedRegionsAdapter;
import edu.cmu.cs.mvelezce.adapters.cleanConstraints.BaseCleanConstraintsAdapter;
import edu.cmu.cs.mvelezce.adapters.cleanConstraintsIssue.BaseCleanConstraintsIssueAdapter;
import edu.cmu.cs.mvelezce.adapters.contextDataTaintsEqual.BaseContextDataTaintsEqualAdapter;
import edu.cmu.cs.mvelezce.adapters.convert.BaseConvertAdapter;
import edu.cmu.cs.mvelezce.adapters.iGen.BaseIGenAdapter;
import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.adapters.methodCall.BaseMethodCallAdapter;
import edu.cmu.cs.mvelezce.adapters.multipleReturns.BaseMultipleReturnsAdapter;
import edu.cmu.cs.mvelezce.adapters.multithread.BaseMultithreadAdapter;
import edu.cmu.cs.mvelezce.adapters.overrideJREMethod.BaseOverrideJREMethodAdapter;
import edu.cmu.cs.mvelezce.adapters.performance.BasePerformanceAdapter;
import edu.cmu.cs.mvelezce.adapters.pngtastic.BasePngtasticAdapter;
import edu.cmu.cs.mvelezce.adapters.runBenchC.BaseRunBenchCAdapter;
import edu.cmu.cs.mvelezce.adapters.staticMethodCall.BaseStaticMethodCallAdapter;
import edu.cmu.cs.mvelezce.adapters.subtraces.BaseSubtracesAdapter;
import edu.cmu.cs.mvelezce.adapters.trivial.BaseTrivialAdapter;
import edu.cmu.cs.mvelezce.lc.adapters.barInfluence.BaseBarInfluenceAdapter;
import edu.cmu.cs.mvelezce.lc.adapters.barInfluence2.BaseBarInfluence2Adapter;
import edu.cmu.cs.mvelezce.lc.adapters.diffStacks.BaseDiffStacksAdapter;
import edu.cmu.cs.mvelezce.lc.adapters.dummyRegion.BaseDummyRegionAdapter;
import edu.cmu.cs.mvelezce.lc.adapters.earlyReturn.BaseEarlyReturnAdapter;
import edu.cmu.cs.mvelezce.lc.adapters.mooInfluence.BaseMooInfluenceAdapter;
import edu.cmu.cs.mvelezce.lc.adapters.multiplePaths.BaseMultiplePathsAdapter;
import edu.cmu.cs.mvelezce.lc.adapters.needSlicing.BaseNeedSlicingAdapter;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IDTATest {

  @Before
  public void setBDD() {
    System.setProperty("bddCacheSize", Integer.toString(1_000_000));
    System.setProperty("bddValNum", Integer.toString(60_000_000));
  }

  @Test
  public void TrivialSmall() throws IOException, InterruptedException {
    String programName = BaseTrivialAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    List<String> options = BaseTrivialAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, workloadSize, options, initialConfig);
    analysis.analyze(args);
  }

  @Test
  public void BarInfluenceSmall() throws IOException, InterruptedException {
    String programName = BaseBarInfluenceAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    List<String> options = BaseBarInfluenceAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, workloadSize, options, initialConfig);
    analysis.analyze(args);
  }

  @Test
  public void BarInfluence2Small() throws IOException, InterruptedException {
    String programName = BaseBarInfluence2Adapter.PROGRAM_NAME;
    String workloadSize = "small";
    List<String> options = BaseBarInfluence2Adapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, workloadSize, options, initialConfig);
    analysis.analyze(args);
  }

  @Test
  public void DiffStacksSmall() throws IOException, InterruptedException {
    String programName = BaseDiffStacksAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    List<String> options = BaseDiffStacksAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, workloadSize, options, initialConfig);
    analysis.analyze(args);
  }

  @Test
  public void DummyRegionSmall() throws IOException, InterruptedException {
    String programName = BaseDummyRegionAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    List<String> options = BaseDummyRegionAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, workloadSize, options, initialConfig);
    analysis.analyze(args);
  }

  @Test
  public void EarlyReturnSmall() throws IOException, InterruptedException {
    String programName = BaseEarlyReturnAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    List<String> options = BaseEarlyReturnAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, workloadSize, options, initialConfig);
    analysis.analyze(args);
  }

  @Test
  public void MooInfluenceSmall() throws IOException, InterruptedException {
    String programName = BaseMooInfluenceAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    List<String> options = BaseMooInfluenceAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, workloadSize, options, initialConfig);
    analysis.analyze(args);
  }

  @Test
  public void MultiplePathsSmall() throws IOException, InterruptedException {
    String programName = BaseMultiplePathsAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    List<String> options = BaseMultiplePathsAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, workloadSize, options, initialConfig);
    analysis.analyze(args);
  }

  @Test
  public void NeedSlicingSmall() throws IOException, InterruptedException {
    String programName = BaseNeedSlicingAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    List<String> options = BaseNeedSlicingAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, workloadSize, options, initialConfig);
    analysis.analyze(args);
  }

  @Test
  public void StaticMethodCallSmall() throws IOException, InterruptedException {
    String programName = BaseStaticMethodCallAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    List<String> options = BaseStaticMethodCallAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, workloadSize, options, initialConfig);
    analysis.analyze(args);
  }

  @Test
  public void MethodCallSmall() throws IOException, InterruptedException {
    String programName = BaseMethodCallAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    List<String> options = BaseMethodCallAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, workloadSize, options, initialConfig);
    analysis.analyze(args);
  }

  @Test
  public void OverrideJREMethodSmall() throws IOException, InterruptedException {
    String programName = BaseOverrideJREMethodAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    List<String> options = BaseOverrideJREMethodAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, workloadSize, options, initialConfig);
    analysis.analyze(args);
  }

  @Test
  public void CanRemoveNestedConstraintsMultipleCallSites()
      throws IOException, InterruptedException {
    String programName = BaseCanRemoveNestedConstraintsMultipleCallSitesAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    List<String> options =
        BaseCanRemoveNestedConstraintsMultipleCallSitesAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, workloadSize, options, initialConfig);
    analysis.analyze(args);
  }

  @Test
  public void CannotRemoveNestedRegionsSmall() throws IOException, InterruptedException {
    String programName = BaseCannotRemoveNestedRegionsAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    List<String> options = BaseCannotRemoveNestedRegionsAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, workloadSize, options, initialConfig);
    analysis.analyze(args);
  }

  @Test
  public void SubtracesSmall() throws IOException, InterruptedException {
    String programName = BaseSubtracesAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    List<String> options = BaseSubtracesAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, workloadSize, options, initialConfig);
    analysis.analyze(args);
  }

  @Test
  public void iGenSmall() throws IOException, InterruptedException {
    String programName = BaseIGenAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    List<String> options = BaseIGenAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, workloadSize, options, initialConfig);
    analysis.analyze(args);
  }

  @Test
  public void pngtasticCounterSmall() throws IOException, InterruptedException {
    String programName = BasePngtasticAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    List<String> options = BasePngtasticAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, workloadSize, options, initialConfig);
    analysis.analyze(args);
  }

  @Test
  public void MeasureDiskOrderedScanSmall() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    IDTA analysis = new IDTA(programName, workloadSize, options, initialConfig);
    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    analysis.analyze(args);
  }

  @Test
  public void MeasureDiskOrderedScanMild() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    String workloadSize = "mild";
    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    IDTA analysis = new IDTA(programName, workloadSize, options, initialConfig);
    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    analysis.analyze(args);
  }

  // 117 configs in 24 hour timeout
  @Test
  public void MeasureDiskOrderedScanMedium() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    String workloadSize = "medium";
    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    IDTA analysis = new IDTA(programName, workloadSize, options, initialConfig);
    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    analysis.analyze(args);
  }

  @Test
  public void PerformanceSmall() throws IOException, InterruptedException {
    String programName = BasePerformanceAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    List<String> options = BasePerformanceAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    IDTA analysis = new IDTA(programName, workloadSize, options, initialConfig);
    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    analysis.analyze(args);
  }

  @Test
  public void MeasureDiskOrderedScanLarge() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    String workloadSize = "large";
    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    IDTA analysis = new IDTA(programName, workloadSize, options, initialConfig);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    analysis.analyze(args);
  }

  @Test
  public void CleanConstraintSmall() throws IOException, InterruptedException {
    String programName = BaseCleanConstraintsAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    List<String> options = BaseCleanConstraintsAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    IDTA analysis = new IDTA(programName, workloadSize, options, initialConfig);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    analysis.analyze(args);
  }

  @Test
  public void CleanConstraintIssueSmall() throws IOException, InterruptedException {
    String programName = BaseCleanConstraintsIssueAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    List<String> options = BaseCleanConstraintsIssueAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    IDTA analysis = new IDTA(programName, workloadSize, options, initialConfig);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    analysis.analyze(args);
  }

  @Test
  public void indexFilesSmall() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    List<String> options = BaseIndexFilesAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, workloadSize, options, initialConfig);
    analysis.analyze(args);
  }

  @Test
  public void indexFilesMild() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    String workloadSize = "mild";
    List<String> options = BaseIndexFilesAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, workloadSize, options, initialConfig);
    analysis.analyze(args);
  }

  @Test
  public void indexFilesMedium() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    String workloadSize = "medium";
    List<String> options = BaseIndexFilesAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, workloadSize, options, initialConfig);
    analysis.analyze(args);
  }

  @Test
  public void indexFilesLarge() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    String workloadSize = "large";
    List<String> options = BaseIndexFilesAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, workloadSize, options, initialConfig);
    analysis.analyze(args);
  }

  @Test
  public void contextDataTaintsEqualSmall() throws IOException, InterruptedException {
    String programName = BaseContextDataTaintsEqualAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    List<String> options = BaseContextDataTaintsEqualAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, workloadSize, options, initialConfig);
    analysis.analyze(args);
  }

  @Test
  public void cannotExpandConstraintsDownSmall() throws IOException, InterruptedException {
    String programName = BaseCannotExpandConstraintsDownAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    List<String> options = BaseCannotExpandConstraintsDownAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, workloadSize, options, initialConfig);
    analysis.analyze(args);
  }

  @Test
  public void canExpandConstraintsDownSmall() throws IOException, InterruptedException {
    String programName = BaseCanExpandConstraintsDownAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    List<String> options = BaseCanExpandConstraintsDownAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, workloadSize, options, initialConfig);
    analysis.analyze(args);
  }

  @Test
  public void multipleReturnsSmall() throws IOException, InterruptedException {
    String programName = BaseMultipleReturnsAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    List<String> options = BaseMultipleReturnsAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, workloadSize, options, initialConfig);
    analysis.analyze(args);
  }

  // Used https://www.reduceimages.com/
  @Test
  public void ConvertSmall() throws IOException, InterruptedException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    List<String> options = BaseConvertAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, workloadSize, options, initialConfig);
    analysis.analyze(args);
  }

  @Test
  public void ConvertMild() throws IOException, InterruptedException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    String workloadSize = "mild";
    List<String> options = BaseConvertAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, workloadSize, options, initialConfig);
    analysis.analyze(args);
  }

  // 24h timeout (70%) 50 configs, ~40 constraints to go
  @Test
  public void ConvertMedium() throws IOException, InterruptedException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    String workloadSize = "medium";
    List<String> options = BaseConvertAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, workloadSize, options, initialConfig);
    analysis.analyze(args);
  }

  // 24h timeout (100%) 38 configs, ~43 constraints to go
  @Test
  public void ConvertLarge() throws IOException, InterruptedException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    String workloadSize = "large";
    List<String> options = BaseConvertAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, workloadSize, options, initialConfig);
    analysis.analyze(args);
  }

  @Test
  public void MultithreadSmall() throws IOException, InterruptedException {
    String programName = BaseMultithreadAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    List<String> options = BaseMultithreadAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, workloadSize, options, initialConfig);
    analysis.analyze(args);
  }

  @Test
  public void RunBenchCSmall() throws IOException, InterruptedException {
    String programName = BaseRunBenchCAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    List<String> options = BaseRunBenchCAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, workloadSize, options, initialConfig);
    analysis.analyze(args);
  }

  @Test
  public void RunBenchCMild() throws IOException, InterruptedException {
    String programName = BaseRunBenchCAdapter.PROGRAM_NAME;
    String workloadSize = "mild";
    List<String> options = BaseRunBenchCAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, workloadSize, options, initialConfig);
    analysis.analyze(args);
  }

  @Test
  public void RunBenchCMedium() throws IOException, InterruptedException {
    String programName = BaseRunBenchCAdapter.PROGRAM_NAME;
    String workloadSize = "medium";
    List<String> options = BaseRunBenchCAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, workloadSize, options, initialConfig);
    analysis.analyze(args);
  }

  @Test
  public void RunBenchCBig() throws IOException, InterruptedException {
    String programName = BaseRunBenchCAdapter.PROGRAM_NAME;
    String workloadSize = "big";
    List<String> options = BaseRunBenchCAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, workloadSize, options, initialConfig);
    analysis.analyze(args);
  }

  // 24h timeout (5000, 70) 147 configs, ~192 constraints to go
  @Test
  public void RunBenchCLarge() throws IOException, InterruptedException {
    String programName = BaseRunBenchCAdapter.PROGRAM_NAME;
    String workloadSize = "large";
    List<String> options = BaseRunBenchCAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, workloadSize, options, initialConfig);
    analysis.analyze(args);
  }
}
