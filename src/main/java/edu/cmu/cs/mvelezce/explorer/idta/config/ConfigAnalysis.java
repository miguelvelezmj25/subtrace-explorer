package edu.cmu.cs.mvelezce.explorer.idta.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.analysis.BaseAnalysis;
import edu.cmu.cs.mvelezce.explorer.idta.IDTA;
import edu.cmu.cs.mvelezce.explorer.idta.constraint.Constraint;
import edu.cmu.cs.mvelezce.utils.config.Options;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class ConfigAnalysis extends BaseAnalysis<Set<Set<String>>> {

  private final String workloadSize;
  private final List<String> options;
  private final Set<Set<String>> executedConfigs = new HashSet<>();

  public ConfigAnalysis(String programName, String workloadSize, List<String> options) {
    super(programName);

    this.workloadSize = workloadSize;
    this.options = options;
  }

  public void saveExecutedConfig(Set<String> config) {
    this.executedConfigs.add(config);
  }

  @Nullable
  public abstract Set<String> getNextConfig(Set<Constraint> constraintsToExplore);

  public abstract Set<String> getInitialConfig();

  @Override
  public Set<Set<String>> analyze() {
    return this.executedConfigs;
  }

  @Override
  public void writeToFile(Set<Set<String>> configs) throws IOException {
    String outputFile = this.outputDir() + "/" + this.getProgramName() + Options.DOT_JSON;
    File file = new File(outputFile);
    file.getParentFile().mkdirs();

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(file, configs);
  }

  @Override
  public Set<Set<String>> readFromFile(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    return mapper.readValue(file, new TypeReference<Set<Set<String>>>() {});
  }

  @Override
  public String outputDir() {
    return IDTA.OUTPUT_DIR
        + "/analysis/"
        + this.getProgramName()
        + "/cc/"
        + this.workloadSize
        + "/configs";
  }

  public List<String> getOptions() {
    return options;
  }
}
