package edu.cmu.cs.mvelezce.explorer.gt.valueanalysis;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.analysis.Analysis;
import edu.cmu.cs.mvelezce.explorer.gt.Explorer;
import edu.cmu.cs.mvelezce.explorer.gt.log.SubtracesLogger;
import edu.cmu.cs.mvelezce.utils.Options;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/** Analyzes what configs result in each possible value of a subtrace */
public class SubtracesValueAnalysis implements Analysis<Set<SubtraceAnalysisInfo>> {

  private final String programName;
  private final Map<Set<String>, List<String>> configsToTraces;

  public SubtracesValueAnalysis(String programName) {
    this(programName, new HashMap<>());
  }

  public SubtracesValueAnalysis(
      String programName, Map<Set<String>, List<String>> configsToTraces) {
    this.programName = programName;
    this.configsToTraces = configsToTraces;
  }

  @Override
  public Set<SubtraceAnalysisInfo> analyze() {
    Set<String> uniqueSubtraces = this.getUniqueSubtraces();
    Map<String, Set<String>> subtracesToValues = this.getSubtracesToValues(uniqueSubtraces);
    Map<Set<String>, Map<String, String>> configsToSubtracesAnalysis =
        this.getConfigsToSubtracesAnalysis();

    return this.getSubtraceAnalysisInfos(subtracesToValues, configsToSubtracesAnalysis);
  }

  private Map<Set<String>, Map<String, String>> getConfigsToSubtracesAnalysis() {
    Map<Set<String>, Map<String, String>> configsToSubtracesAnalysis = new HashMap<>();

    for (Map.Entry<Set<String>, List<String>> entry : this.configsToTraces.entrySet()) {
      List<String> trace = entry.getValue();
      Map<String, String> subtracesToValues = new HashMap<>(trace.size() >> 1);
      Iterator<String> traceIter = trace.iterator();

      while (traceIter.hasNext()) {
        String label = traceIter.next();
        String value = traceIter.next();

        subtracesToValues.put(label, value);
      }

      configsToSubtracesAnalysis.put(entry.getKey(), subtracesToValues);
    }

    return configsToSubtracesAnalysis;
  }

  private Set<SubtraceAnalysisInfo> getSubtraceAnalysisInfos(
      Map<String, Set<String>> subtracesToValues,
      Map<Set<String>, Map<String, String>> configsToSubtracesAnalysis) {
    Map<String, SubtraceAnalysisInfo> subtracesToAnalysisInfos =
        this.getSubtracesToAnalysisInfos(subtracesToValues);
    this.addConfigsToSubtracesValues(configsToSubtracesAnalysis, subtracesToAnalysisInfos);

    return new HashSet<>(subtracesToAnalysisInfos.values());
  }

  private void addConfigsToSubtracesValues(
      Map<Set<String>, Map<String, String>> configsToSubtracesAnalysis,
      Map<String, SubtraceAnalysisInfo> subtracesToAnalysisInfos) {
    for (Map.Entry<Set<String>, Map<String, String>> entry :
        configsToSubtracesAnalysis.entrySet()) {
      Set<String> config = entry.getKey();
      Map<String, String> subtracesToValues = entry.getValue();
      this.addConfigToSubtraceValue(config, subtracesToValues, subtracesToAnalysisInfos);
    }
  }

  private void addConfigToSubtraceValue(
      Set<String> config,
      Map<String, String> subtracesToValues,
      Map<String, SubtraceAnalysisInfo> subtracesToAnalysisInfos) {
    for (Map.Entry<String, String> entry : subtracesToValues.entrySet()) {
      String subtrace = entry.getKey();
      String value = entry.getValue();

      SubtraceAnalysisInfo subtracesAnalysisInfos = subtracesToAnalysisInfos.get(subtrace);
      Set<Set<String>> configs = subtracesAnalysisInfos.getValuesToConfigs().get(value);
      configs.add(config);
    }
  }

  private Map<String, SubtraceAnalysisInfo> getSubtracesToAnalysisInfos(
      Map<String, Set<String>> subtracesToValues) {
    Map<String, SubtraceAnalysisInfo> subtracesToAnalysisInfos = new HashMap<>();

    for (Map.Entry<String, Set<String>> entry : subtracesToValues.entrySet()) {
      String subtrace = entry.getKey();
      Set<String> values = entry.getValue();

      Map<String, Set<Set<String>>> valuesToConfigs = new HashMap<>();

      for (String value : values) {
        valuesToConfigs.put(value, new HashSet<>());
      }

      SubtraceAnalysisInfo subtraceAnalysisInfo =
          new SubtraceAnalysisInfo(subtrace, valuesToConfigs);
      subtracesToAnalysisInfos.put(subtrace, subtraceAnalysisInfo);
    }

    return subtracesToAnalysisInfos;
  }

  private Set<Set<String>> getConfigsWithValue(String subtrace, String value) {
    Set<Set<String>> configs = new HashSet<>();

    for (Map.Entry<Set<String>, List<String>> entry : this.configsToTraces.entrySet()) {
      List<String> trace = entry.getValue();

      for (int i = 0; i < trace.size(); i++) {
        String subtraceInEntry = trace.get(i);

        if (!subtraceInEntry.equals(subtrace)) {
          continue;
        }

        String valueInSubtrace = trace.get(i + 1);

        if (!valueInSubtrace.equals(value)) {
          continue;
        }

        configs.add(entry.getKey());
      }
    }

    return configs;
  }

  private Map<String, Set<String>> getSubtracesToValues(Set<String> uniqueSubtraces) {
    Set<String> subtraceValues = new HashSet<>();
    subtraceValues.add(SubtracesLogger.TRUE);
    subtraceValues.add(SubtracesLogger.FALSE);

    Map<String, Set<String>> subtracesToValues = new HashMap<>();

    for (String subtrace : uniqueSubtraces) {
      subtracesToValues.put(subtrace, subtraceValues);
    }

    return subtracesToValues;
  }

  private Set<String> getUniqueSubtraces() {
    Set<String> uniqueSubtraces = new HashSet<>();

    for (List<String> trace : this.configsToTraces.values()) {
      for (String entry : trace) {
        if (entry.equals(SubtracesLogger.TRUE) || entry.equals(SubtracesLogger.FALSE)) {
          continue;
        }

        uniqueSubtraces.add(entry);
      }
    }

    return uniqueSubtraces;
  }

  @Override
  public Set<SubtraceAnalysisInfo> analyze(String[] args) throws IOException {
    Options.getCommandLine(args);

    String outputFile = this.outputDir();
    File file = new File(outputFile);

    Options.checkIfDeleteResult(file);

    if (file.exists()) {
      Collection<File> files = FileUtils.listFiles(file, null, true);

      if (files.size() != 1) {
        throw new RuntimeException(
            "We expected to find 1 file in the directory, but that is not the case " + outputFile);
      }

      return this.readFromFile(files.iterator().next());
    }

    Set<SubtraceAnalysisInfo> configSubtraceValues = this.analyze();

    if (Options.checkIfSave()) {
      this.writeToFile(configSubtraceValues);
    }

    return configSubtraceValues;
  }

  @Override
  public Set<SubtraceAnalysisInfo> readFromFile(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    return mapper.readValue(file, new TypeReference<Set<SubtraceAnalysisInfo>>() {});
  }

  @Override
  public void writeToFile(Set<SubtraceAnalysisInfo> configTraceValues) throws IOException {
    String outputFile = this.outputDir() + "/" + this.programName + Options.DOT_JSON;
    File file = new File(outputFile);
    file.getParentFile().mkdirs();

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(file, configTraceValues);
  }

  @Override
  public String outputDir() {
    return Explorer.OUTPUT_DIR + "/value/analysis/java/programs/" + this.programName;
  }
}
