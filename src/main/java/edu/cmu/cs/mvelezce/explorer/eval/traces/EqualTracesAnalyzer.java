package edu.cmu.cs.mvelezce.explorer.eval.traces;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.explorer.execute.BaseTraceExecutor;
import edu.cmu.cs.mvelezce.explorer.execute.ConfigToTraceInfo;
import edu.cmu.cs.mvelezce.explorer.log.SubtracesLogger;
import edu.cmu.cs.mvelezce.utils.Options;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EqualTracesAnalyzer extends BaseTraceExecutor<Object> {
  private final Set<String> config;

  EqualTracesAnalyzer(String programName, Set<String> config) {
    super(programName, new HashSet<>(), new HashSet<>());

    this.config = config;
  }

  @Override
  public Object analyze() throws IOException, InterruptedException {
    this.runProgramWithConfig();
    this.analyzeTraces();

    throw new UnsupportedOperationException("What to return?");
  }

  private void analyzeTraces() throws IOException {
    Set<List<String>> recordedTraces = this.readTraces();

    if (recordedTraces.size() == 1) {
      return;
    }

    throw new UnsupportedOperationException("Diff traces");
  }

  private Set<List<String>> readTraces() throws IOException {
    System.err.println("Abstract since it is repeated with SubtraceLabeler");
    File file = new File(this.outputDir());
    Collection<File> files = FileUtils.listFiles(file, null, true);

    Set<List<String>> traces = new HashSet<>();

    for (File resultFile : files) {
      ObjectMapper mapper = new ObjectMapper();
      List<ConfigToTraceInfo> configToTraceInfoList =
          mapper.readValue(resultFile, new TypeReference<List<ConfigToTraceInfo>>() {});
      ConfigToTraceInfo configToTraceInfo = configToTraceInfoList.get(0);
      traces.add(configToTraceInfo.getTrace());
    }

    return traces;
  }

  private void runProgramWithConfig() throws IOException, InterruptedException {
    int iterations = Options.getIterations();

    for (int i = 0; i < iterations; i++) {
      this.runProgram(config);
      this.processResults(config, i);

      Files.deleteIfExists(Paths.get(SubtracesLogger.RESULTS_FILE));
    }
  }

  @Override
  public void writeToFile(Object o) {
    throw new UnsupportedOperationException("Implement");
  }

  @Override
  public Object readFromFile(File file) {
    throw new UnsupportedOperationException("Implement");
  }

  @Override
  public String outputDir() {
    return Options.DIRECTORY
        + "/evaluation/traces/equal/java/programs/"
        + this.getProgramName()
        + "/"
        + this.config.hashCode();
  }
}
