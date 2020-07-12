package edu.cmu.cs.mvelezce.explorer.idta.config;

import edu.cmu.cs.mvelezce.adapters.convert.BaseConvertAdapter;
import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.adapters.runBenchC.BaseRunBenchCAdapter;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

public class SpecificConfigsGetterTest {

  private static final int NUM_CONFIGS_TO_GET = 10;

  @Test
  public void MeasureDiskOrderedScanSmall() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    ConfigAnalysis configAnalysis = new IDTAConfigAnalysis(programName, "small");
    String[] args = new String[0];
    Set<Set<String>> configs = configAnalysis.analyze(args);

    SpecificConfigsGetter analysis =
        new SpecificConfigsGetter(programName, configs, NUM_CONFIGS_TO_GET);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    analysis.analyze(args);
  }

  @Test
  public void indexFilesSmall() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    ConfigAnalysis configAnalysis = new IDTAConfigAnalysis(programName, "small");
    String[] args = new String[0];
    Set<Set<String>> configs = configAnalysis.analyze(args);

    SpecificConfigsGetter analysis =
        new SpecificConfigsGetter(programName, configs, NUM_CONFIGS_TO_GET);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    analysis.analyze(args);
  }

  @Test
  public void ConvertSmall() throws IOException, InterruptedException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    ConfigAnalysis configAnalysis = new IDTAConfigAnalysis(programName, "small");
    String[] args = new String[0];
    Set<Set<String>> configs = configAnalysis.analyze(args);

    SpecificConfigsGetter analysis =
        new SpecificConfigsGetter(programName, configs, NUM_CONFIGS_TO_GET);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    analysis.analyze(args);
  }

  @Test
  public void RunBenchCSmall() throws IOException, InterruptedException {
    String programName = BaseRunBenchCAdapter.PROGRAM_NAME;
    ConfigAnalysis configAnalysis = new IDTAConfigAnalysis(programName, "small");
    String[] args = new String[0];
    Set<Set<String>> configs = configAnalysis.analyze(args);

    SpecificConfigsGetter analysis =
        new SpecificConfigsGetter(programName, configs, NUM_CONFIGS_TO_GET);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    analysis.analyze(args);
  }
}
