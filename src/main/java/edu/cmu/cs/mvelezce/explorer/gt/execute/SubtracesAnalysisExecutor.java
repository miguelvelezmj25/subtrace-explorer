package edu.cmu.cs.mvelezce.explorer.gt.execute;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.explorer.gt.Explorer;
import edu.cmu.cs.mvelezce.explorer.gt.log.SubtracesLogger;
import edu.cmu.cs.mvelezce.utils.ConfigHelper;
import edu.cmu.cs.mvelezce.utils.Options;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Class to execute the programs that have been instrumented with code to perform subtrace analysis.
 */
public class SubtracesAnalysisExecutor extends BaseTraceExecutor<Map<Set<String>, List<String>>> {

  private final Map<Set<String>, List<String>> configsToTraces = new HashMap<>();

  SubtracesAnalysisExecutor(String programName, Set<String> options) {
    super(programName, options, new HashSet<>());
  }

  public SubtracesAnalysisExecutor(String programName) {
    this(programName, new HashSet<>());
  }

  @Override
  public Map<Set<String>, List<String>> analyze(String[] args)
      throws IOException, InterruptedException {
    Options.getCommandLine(args);

    String outputFile = this.outputDir();
    File file = new File(outputFile);

    Options.checkIfDeleteResult(file);

    if (file.exists()) {
      return this.readFromFile(file);
    }

    Map<Set<String>, List<String>> analysisResults = this.analyze();

    if (Options.checkIfSave()) {
      this.writeToFile(analysisResults);
    }

    return analysisResults;
  }

  @Override
  public Map<Set<String>, List<String>> analyze() throws IOException, InterruptedException {
    Set<Set<String>> configs = ConfigHelper.getConfigurations(this.getOptions());
    Iterator<Set<String>> configsIter = configs.iterator();

    for (int i = 0; configsIter.hasNext(); i++) {
      Set<String> config = configsIter.next();
      this.runProgram(config);
      this.processResults(config, i);
    }

    Files.deleteIfExists(Paths.get(SubtracesLogger.RESULTS_FILE));

    return configsToTraces;
  }

  // TODO abstract since it is repeated with SubtraceLabeler
  @Override
  public Map<Set<String>, List<String>> readFromFile(File file) throws IOException {
    Collection<File> files = FileUtils.listFiles(file, null, true);

    for (File resultFile : files) {
      ObjectMapper mapper = new ObjectMapper();
      List<ConfigToTraceInfo> configToTraceInfoList =
          mapper.readValue(resultFile, new TypeReference<List<ConfigToTraceInfo>>() {});

      for (ConfigToTraceInfo configToTraceInfo : configToTraceInfoList) {
        configsToTraces.put(configToTraceInfo.getConfig(), configToTraceInfo.getTrace());
      }
    }

    return configsToTraces;
  }

  @Override
  public String outputDir() {
    return Explorer.OUTPUT_DIR + "/execute/traces/java/programs/" + this.getProgramName();
  }
}
