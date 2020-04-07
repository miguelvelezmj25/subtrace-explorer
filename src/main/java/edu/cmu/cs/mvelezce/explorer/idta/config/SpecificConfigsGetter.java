package edu.cmu.cs.mvelezce.explorer.idta.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.analysis.BaseAnalysis;
import edu.cmu.cs.mvelezce.explorer.idta.IDTA;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SpecificConfigsGetter extends BaseAnalysis<Set<Set<String>>> {

  private final List<Set<String>> configsToChooseFrom;
  private final int numConfigsToSelect;

  public SpecificConfigsGetter(String programName) {
    this(programName, new HashSet<>(), -1);
  }

  public SpecificConfigsGetter(
      String programName, Set<Set<String>> configsToChooseFrom, int numConfigsToSelect) {
    super(programName);

    this.configsToChooseFrom = new ArrayList<>(configsToChooseFrom);
    this.numConfigsToSelect = numConfigsToSelect;
  }

  @Override
  public Set<Set<String>> analyze() {
    Set<Set<String>> configs = new HashSet<>();

    Random random = new Random();

    while (configs.size() != this.numConfigsToSelect) {
      int i = random.nextInt(this.configsToChooseFrom.size());
      configs.add(this.configsToChooseFrom.get(i));
    }

    return configs;
  }

  @Override
  public void writeToFile(Set<Set<String>> results) throws IOException {
    String outputFile = this.outputDir() + "/" + this.getProgramName() + Options.DOT_JSON;
    File file = new File(outputFile);
    file.getParentFile().mkdirs();

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(file, results);
  }

  @Override
  public Set<Set<String>> readFromFile(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    return mapper.readValue(file, new TypeReference<Set<Set<String>>>() {});
  }

  @Override
  public String outputDir() {
    return IDTA.OUTPUT_DIR + "/analysis/" + this.getProgramName() + "/cc/configs/specific";
  }
}
