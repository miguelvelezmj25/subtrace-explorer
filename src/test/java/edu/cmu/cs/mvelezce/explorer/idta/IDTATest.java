package edu.cmu.cs.mvelezce.explorer.idta;

import edu.cmu.cs.mvelezce.adapters.canExpandConstraintsDown.BaseCanExpandConstraintsDownAdapter;
import edu.cmu.cs.mvelezce.adapters.canRemoveNestedConstraintsMultipleCallSites.BaseCanRemoveNestedConstraintsMultipleCallSitesAdapter;
import edu.cmu.cs.mvelezce.adapters.cannotExpandConstraintsDown.BaseCannotExpandConstraintsDownAdapter;
import edu.cmu.cs.mvelezce.adapters.cannotRemoveNestedRegions.BaseCannotRemoveNestedRegionsAdapter;
import edu.cmu.cs.mvelezce.adapters.contextDataTaintsEqual.BaseContextDataTaintsEqualAdapter;
import edu.cmu.cs.mvelezce.adapters.iGen.BaseIGenAdapter;
import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.adapters.methodCall.BaseMethodCallAdapter;
import edu.cmu.cs.mvelezce.adapters.multipleReturns.BaseMultipleReturnsAdapter;
import edu.cmu.cs.mvelezce.adapters.overrideJREMethod.BaseOverrideJREMethodAdapter;
import edu.cmu.cs.mvelezce.adapters.pngtastic.BasePngtasticAdapter;
import edu.cmu.cs.mvelezce.adapters.staticMethodCall.BaseStaticMethodCallAdapter;
import edu.cmu.cs.mvelezce.adapters.subtraces.BaseSubtracesAdapter;
import edu.cmu.cs.mvelezce.adapters.trivial.BaseTrivialAdapter;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IDTATest {

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
}
