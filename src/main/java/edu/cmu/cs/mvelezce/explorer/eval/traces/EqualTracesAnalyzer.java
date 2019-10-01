package edu.cmu.cs.mvelezce.explorer.eval.traces;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.explorer.gt.execute.BaseTraceExecutor;
import edu.cmu.cs.mvelezce.explorer.gt.execute.ConfigToTraceInfo;
import edu.cmu.cs.mvelezce.explorer.gt.log.SubtracesLogger;
import edu.cmu.cs.mvelezce.utils.Options;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class EqualTracesAnalyzer extends BaseTraceExecutor<ConfigWithDiffControlFlowStatements> {
  private final Set<String> config;

  EqualTracesAnalyzer(String programName, Set<String> config) {
    super(programName, new HashSet<>(), new HashSet<>());

    this.config = config;
  }

  @Override
  public ConfigWithDiffControlFlowStatements analyze() throws IOException, InterruptedException {
    this.runProgramWithConfig();

    return this.analyzeTraces();
  }

  public ConfigWithDiffControlFlowStatements analyze(String[] args)
      throws IOException, InterruptedException {
    Options.getCommandLine(args);
    String outputFile = this.outputDir();
    File file = new File(outputFile);
    Options.checkIfDeleteResult(file);

    ConfigWithDiffControlFlowStatements analysisResults;

    if (file.exists()) {
      analysisResults = this.analyzeTraces();
    } else {
      analysisResults = this.analyze();
    }

    if (Options.checkIfSave()) {
      this.writeToFile(analysisResults);
    }

    return analysisResults;
  }

  private ConfigWithDiffControlFlowStatements analyzeTraces() throws IOException {
    Set<List<String>> recordedTraces = this.readTraces();

    if (recordedTraces.size() == 1) {
      return new ConfigWithDiffControlFlowStatements(this.config, new ArrayList<>());
    }

    this.checkTracesSize(recordedTraces);
    this.checkEqualControlFlowDecisionOrder(recordedTraces);
    List<String> diffControlFlowStatements = getDiffControlFlowStatements(recordedTraces);

    return new ConfigWithDiffControlFlowStatements(this.config, diffControlFlowStatements);
  }

  /**
   * This check might fail if the control flow decision take a different branch or if, randomly,
   * some other decision is executed. So, I am not sure if I should run this check first or the
   * check of the branches of control flow decisions.
   *
   * @param recordedTraces
   */
  private void checkEqualControlFlowDecisionOrder(Set<List<String>> recordedTraces) {
    Iterator<List<String>> iter = recordedTraces.iterator();
    List<String> traceA = iter.next();

    while (iter.hasNext()) {
      List<String> traceB = iter.next();

      for (int i = 0; i < traceA.size(); i++) {
        String entryA = traceA.get(i);

        if (!entryA.contains(SubtracesLogger.ARROW)) {
          continue;
        }

        String entryB = traceB.get(i);

        if (entryA.equals(entryB)) {
          continue;
        }

        System.err.println("Add a stack with the 10 latest control-flow decisions.");
        throw new RuntimeException(
            "The traces do not have the same control-flow decision order. One trace has "
                + entryA
                + " while the other has "
                + entryB);
      }
    }
  }

  private void checkTracesSize(Set<List<String>> recordedTraces) {
    Iterator<List<String>> iter = recordedTraces.iterator();
    int size = iter.next().size();

    while (iter.hasNext()) {
      List<String> trace = iter.next();

      if (trace.size() != size) {
        throw new RuntimeException("The trace sizes are not the same. Handle this case");
      }
    }
  }

  /**
   * This check returns the first control flow decision with a different outcome. The idea is that
   * this difference could change the outcome of other decision and which decisions are executed.
   *
   * @param recordedTraces
   * @return
   */
  private List<String> getDiffControlFlowStatements(Set<List<String>> recordedTraces) {
    List<String> diffControlFlowStatements = new ArrayList<>();

    Iterator<List<String>> iter = recordedTraces.iterator();
    List<String> traceA = iter.next();

    while (iter.hasNext()) {
      List<String> traceB = iter.next();

      String controlFlowDecision = "";

      for (int i = 0; i < Math.max(traceA.size(), traceB.size()); i++) {
        String entryA = traceA.get(i);
        String entryB = traceB.get(i);

        if (entryA.equals(entryB)) {
          controlFlowDecision = entryA;

          continue;
        }

        diffControlFlowStatements.add(controlFlowDecision);
        break;
      }
    }

    return diffControlFlowStatements;
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
  public void writeToFile(ConfigWithDiffControlFlowStatements results) throws IOException {
    String outputFile = this.outputDir() + Options.DOT_JSON;
    File file = new File(outputFile);
    file.getParentFile().mkdirs();

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(file, results);
  }

  @Override
  public ConfigWithDiffControlFlowStatements readFromFile(File file) {
    throw new UnsupportedOperationException("Should not be called");
  }

  @Override
  public String outputDir() {
    return Options.DIRECTORY
        + "/evaluation/traces/equal/java/programs/"
        + this.getProgramName()
        + "/"
        + ("config " + this.config).hashCode();
  }
}
