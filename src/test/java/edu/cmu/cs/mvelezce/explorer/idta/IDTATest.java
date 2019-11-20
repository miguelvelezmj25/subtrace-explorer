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

  @Test
  public void StaticMethodCall() throws IOException, InterruptedException {
    String programName = BaseStaticMethodCallAdapter.PROGRAM_NAME;
    List<String> options = BaseStaticMethodCallAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, options, initialConfig);
    analysis.analyze(args);
  }

  @Test
  public void MethodCall() throws IOException, InterruptedException {
    String programName = BaseMethodCallAdapter.PROGRAM_NAME;
    List<String> options = BaseMethodCallAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, options, initialConfig);
    analysis.analyze(args);
  }

  @Test
  public void OverrideJREMethod() throws IOException, InterruptedException {
    String programName = BaseOverrideJREMethodAdapter.PROGRAM_NAME;
    List<String> options = BaseOverrideJREMethodAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, options, initialConfig);
    analysis.analyze(args);
  }

  @Test
  public void CanRemoveNestedConstraintsMultipleCallSites()
      throws IOException, InterruptedException {
    String programName = BaseCanRemoveNestedConstraintsMultipleCallSitesAdapter.PROGRAM_NAME;
    List<String> options =
        BaseCanRemoveNestedConstraintsMultipleCallSitesAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, options, initialConfig);
    analysis.analyze(args);
  }

  @Test
  public void CannotRemoveNestedRegions() throws IOException, InterruptedException {
    String programName = BaseCannotRemoveNestedRegionsAdapter.PROGRAM_NAME;
    List<String> options = BaseCannotRemoveNestedRegionsAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, options, initialConfig);
    analysis.analyze(args);
  }

  @Test
  public void Subtraces() throws IOException, InterruptedException {
    String programName = BaseSubtracesAdapter.PROGRAM_NAME;
    List<String> options = BaseSubtracesAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, options, initialConfig);
    analysis.analyze(args);
  }

  @Test
  public void iGen() throws IOException, InterruptedException {
    String programName = BaseIGenAdapter.PROGRAM_NAME;
    List<String> options = BaseIGenAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, options, initialConfig);
    analysis.analyze(args);
  }

  @Test
  public void pngtasticCounter() throws IOException, InterruptedException {
    String programName = BasePngtasticAdapter.PROGRAM_NAME;
    List<String> options = BasePngtasticAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, options, initialConfig);
    analysis.analyze(args);
  }

  @Test
  public void MeasureDiskOrderedScan() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, options, initialConfig);
    analysis.analyze(args);
  }

  @Test
  public void indexFiles() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    List<String> options = BaseIndexFilesAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, options, initialConfig);
    analysis.analyze(args);
  }

  @Test
  public void contextDataTaintsEqual() throws IOException, InterruptedException {
    String programName = BaseContextDataTaintsEqualAdapter.PROGRAM_NAME;
    List<String> options = BaseContextDataTaintsEqualAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, options, initialConfig);
    analysis.analyze(args);
  }

  @Test
  public void cannotExpandConstraintsDown() throws IOException, InterruptedException {
    String programName = BaseCannotExpandConstraintsDownAdapter.PROGRAM_NAME;
    List<String> options = BaseCannotExpandConstraintsDownAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, options, initialConfig);
    analysis.analyze(args);
  }

  @Test
  public void canExpandConstraintsDown() throws IOException, InterruptedException {
    String programName = BaseCanExpandConstraintsDownAdapter.PROGRAM_NAME;
    List<String> options = BaseCanExpandConstraintsDownAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, options, initialConfig);
    analysis.analyze(args);
  }

  @Test
  public void multipleReturns() throws IOException, InterruptedException {
    String programName = BaseMultipleReturnsAdapter.PROGRAM_NAME;
    List<String> options = BaseMultipleReturnsAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    IDTA analysis = new IDTA(programName, options, initialConfig);
    analysis.analyze(args);
  }
}
