package edu.cmu.cs.mvelezce.explorer.idta;

import edu.cmu.cs.mvelezce.adapters.convert.BaseConvertAdapter;
import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.adapters.runBenchC.BaseRunBenchCAdapter;
import edu.cmu.cs.mvelezce.adapters.trivial.BaseTrivialAdapter;
import edu.cmu.cs.mvelezce.explorer.idta.config.ConfigAnalysis;
import edu.cmu.cs.mvelezce.explorer.idta.config.IDTAConfigAnalysis;
import edu.cmu.cs.mvelezce.explorer.idta.config.SpecificConfigsGetter;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class SpecificConfigsIDTATest {

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

    ConfigAnalysis configAnalysis = new IDTAConfigAnalysis(programName, workloadSize);
    String[] args = new String[0];
    Set<Set<String>> configs = configAnalysis.analyze(args);

    IDTA analysis = new SpecificConfigsIDTA(programName, workloadSize, options, configs);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    analysis.analyze(args);
  }

  @Test
  public void MeasureDiskOrderedScanSmall() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    SpecificConfigsGetter specificConfigsGetter = new SpecificConfigsGetter(programName);
    String[] args = new String[0];
    Set<Set<String>> configs = specificConfigsGetter.analyze(args);

    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    IDTA analysis = new SpecificConfigsIDTA(programName, "small", options, configs);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    analysis.analyze(args);
  }

  @Test
  public void indexFilesSmall() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    SpecificConfigsGetter specificConfigsGetter = new SpecificConfigsGetter(programName);
    String[] args = new String[0];
    Set<Set<String>> configs = specificConfigsGetter.analyze(args);

    List<String> options = BaseIndexFilesAdapter.getListOfOptions();
    IDTA analysis = new SpecificConfigsIDTA(programName, "small", options, configs);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    analysis.analyze(args);
  }

  @Test
  public void ConvertSmall() throws IOException, InterruptedException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    SpecificConfigsGetter specificConfigsGetter = new SpecificConfigsGetter(programName);
    String[] args = new String[0];
    Set<Set<String>> configs = specificConfigsGetter.analyze(args);

    List<String> options = BaseConvertAdapter.getListOfOptions();
    IDTA analysis = new SpecificConfigsIDTA(programName, "small", options, configs);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    analysis.analyze(args);
  }

  @Test
  public void RunBenchCSmall() throws IOException, InterruptedException {
    String programName = BaseRunBenchCAdapter.PROGRAM_NAME;
    SpecificConfigsGetter specificConfigsGetter = new SpecificConfigsGetter(programName);
    String[] args = new String[0];
    Set<Set<String>> configs = specificConfigsGetter.analyze(args);

    List<String> options = BaseRunBenchCAdapter.getListOfOptions();
    IDTA analysis = new SpecificConfigsIDTA(programName, "small", options, configs);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    analysis.analyze(args);
  }

  @Test
  public void MeasureDiskOrderedScanLarge() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    SpecificConfigsGetter specificConfigsGetter = new SpecificConfigsGetter(programName);
    String[] args = new String[0];
    Set<Set<String>> configs = specificConfigsGetter.analyze(args);

    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    IDTA analysis = new SpecificConfigsIDTA(programName, "large", options, configs);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    analysis.analyze(args);
  }

  @Test
  public void indexFilesLarge() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    SpecificConfigsGetter specificConfigsGetter = new SpecificConfigsGetter(programName);
    String[] args = new String[0];
    Set<Set<String>> configs = specificConfigsGetter.analyze(args);

    List<String> options = BaseIndexFilesAdapter.getListOfOptions();
    IDTA analysis = new SpecificConfigsIDTA(programName, "large", options, configs);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    analysis.analyze(args);
  }

  @Test
  public void ConvertLarge() throws IOException, InterruptedException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    SpecificConfigsGetter specificConfigsGetter = new SpecificConfigsGetter(programName);
    String[] args = new String[0];
    Set<Set<String>> configs = specificConfigsGetter.analyze(args);

    List<String> options = BaseConvertAdapter.getListOfOptions();
    IDTA analysis = new SpecificConfigsIDTA(programName, "large", options, configs);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    analysis.analyze(args);
  }

  @Test
  public void RunBenchCLarge() throws IOException, InterruptedException {
    String programName = BaseRunBenchCAdapter.PROGRAM_NAME;
    SpecificConfigsGetter specificConfigsGetter = new SpecificConfigsGetter(programName);
    String[] args = new String[0];
    Set<Set<String>> configs = specificConfigsGetter.analyze(args);

    List<String> options = BaseRunBenchCAdapter.getListOfOptions();
    IDTA analysis = new SpecificConfigsIDTA(programName, "large", options, configs);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    analysis.analyze(args);
  }
}
