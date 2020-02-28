package edu.cmu.cs.mvelezce.explorer.idta;

import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapters.trivial.BaseTrivialAdapter;
import edu.cmu.cs.mvelezce.explorer.idta.config.ConfigAnalysis;
import edu.cmu.cs.mvelezce.explorer.idta.config.IDTAConfigAnalysis;
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
  public void indexFilesLarge() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    List<String> options = BaseIndexFilesAdapter.getListOfOptions();

    ConfigAnalysis configAnalysis = new IDTAConfigAnalysis(programName, "small");
    String[] args = new String[0];
    Set<Set<String>> configs = configAnalysis.analyze(args);

    IDTA analysis = new SpecificConfigsIDTA(programName, "large", options, configs);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    analysis.analyze(args);
  }
}
