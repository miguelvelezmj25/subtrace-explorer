package edu.cmu.cs.mvelezce.explorer.idta;

import edu.cmu.cs.mvelezce.adapter.adapters.cannotExpandConstraintsDown.BaseCannotExpandConstraintsDownAdapter;
import edu.cmu.cs.mvelezce.adapter.adapters.contextDataTaintsEqual.BaseContextDataTaintsEqualAdapter;
import edu.cmu.cs.mvelezce.adapter.adapters.iGen.BaseIGenAdapter;
import edu.cmu.cs.mvelezce.adapter.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapter.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.adapter.adapters.pngtastic.BasePngtasticAdapter;
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
}
