package edu.cmu.cs.mvelezce.explorer.eval.constraints.subtraces;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.fosd.typechef.featureexpr.FeatureExpr;
import de.fosd.typechef.featureexpr.sat.SATFeatureExprFactory;
import edu.cmu.cs.mvelezce.MinConfigsGenerator;
import edu.cmu.cs.mvelezce.analysis.Analysis;
import edu.cmu.cs.mvelezce.explorer.valueanalysis.SubtraceAnalysisInfo;
import edu.cmu.cs.mvelezce.utils.Options;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SubtracesConstraintsAnalyzer implements Analysis<Set<SubtraceOutcomeConstraint>> {

  private static final String ID = "id";
  private static final String OUTCOMES_TO_STRING_CONSTRAINTS = "outcomesToStringConstraints";

  private final String programName;
  private final Set<SubtraceAnalysisInfo> subtraceAnalysisInfos;
  private final Set<String> options;

  SubtracesConstraintsAnalyzer(
      String programName, Set<SubtraceAnalysisInfo> subtraceAnalysisInfos, Set<String> options) {
    this.programName = programName;
    this.subtraceAnalysisInfos = subtraceAnalysisInfos;
    this.options = options;
  }

  public SubtracesConstraintsAnalyzer(String programName) {
    this.programName = programName;
    this.subtraceAnalysisInfos = new HashSet<>();
    this.options = new HashSet<>();
  }

  @Override
  public Set<SubtraceOutcomeConstraint> analyze() {
    Set<SubtraceOutcomeConstraint> subtracesOutcomeConstraint = new HashSet<>();

    int count = 0;

    for (SubtraceAnalysisInfo subtraceAnalysisInfo : this.subtraceAnalysisInfos) {
      Map<String, Set<Set<String>>> valuesToConfigs = subtraceAnalysisInfo.getValuesToConfigs();

      if (valuesToConfigs.size() == 1) {
        throw new UnsupportedOperationException("Implement size == 1");
      }

      SubtraceOutcomeConstraint subtraceOutcomeConstraint =
          new SubtraceOutcomeConstraint(subtraceAnalysisInfo.getSubtrace());
      Map<String, FeatureExpr> outcomesToConstraints =
          subtraceOutcomeConstraint.getOutcomesToConstraints();

      for (Map.Entry<String, Set<Set<String>>> entry : valuesToConfigs.entrySet()) {
        Set<Set<String>> configs = entry.getValue();
        String stringConstraints = toStringConstraints(configs);

        if (stringConstraints.isEmpty()) {
          outcomesToConstraints.put(entry.getKey(), SATFeatureExprFactory.False());

          continue;
        }

        FeatureExpr featureExpr = MinConfigsGenerator.parseAsFeatureExpr(stringConstraints);
        outcomesToConstraints.put(entry.getKey(), featureExpr);
      }

      subtracesOutcomeConstraint.add(subtraceOutcomeConstraint);

      count++;

      System.out.println("Subtraces to analyze: " + (this.subtraceAnalysisInfos.size() - count));
    }

    return subtracesOutcomeConstraint;
  }

  private String toStringConstraints(Set<Set<String>> configs) {
    StringBuilder orConstraints = new StringBuilder();

    Iterator<Set<String>> configsIter = configs.iterator();

    while (configsIter.hasNext()) {
      Set<String> config = configsIter.next();
      String andConstraint = this.getAndConstraints(config);
      orConstraints.append(andConstraint);

      if (configsIter.hasNext()) {
        orConstraints.append(" || ");
      }
    }

    return orConstraints.toString();
  }

  private String getAndConstraints(Set<String> config) {
    StringBuilder stringBuilder = new StringBuilder("(");

    Iterator<String> optionsIter = this.options.iterator();

    while (optionsIter.hasNext()) {
      String option = optionsIter.next();

      if (!config.contains(option)) {
        stringBuilder.append("!");
      }

      stringBuilder.append(option);

      if (optionsIter.hasNext()) {
        stringBuilder.append(" && ");
      }
    }

    stringBuilder.append(")");

    return stringBuilder.toString();
  }

  @Override
  public Set<SubtraceOutcomeConstraint> analyze(String[] args) throws IOException {
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

    Set<SubtraceOutcomeConstraint> subtracesOutcomeConstraint = this.analyze();

    if (Options.checkIfSave()) {
      this.writeToFile(subtracesOutcomeConstraint);
    }

    return subtracesOutcomeConstraint;
  }

  @Override
  public void writeToFile(Set<SubtraceOutcomeConstraint> subtracesOutcomeConstraint)
      throws IOException {
    String outputFile = this.outputDir() + "/" + this.programName + Options.DOT_JSON;
    File file = new File(outputFile);
    file.getParentFile().mkdirs();

    Set<Map<String, Object>> serialObjects = new HashSet<>();

    for (SubtraceOutcomeConstraint subtraceOutcomeConstraint : subtracesOutcomeConstraint) {
      Map<String, Object> serialObject = new LinkedHashMap<>();
      serialObject.put(ID, subtraceOutcomeConstraint.getSubtraceLabelUUID().toString());

      Map<String, String> serializableOutcomesToStringConstraints = new HashMap<>();
      serialObject.put(OUTCOMES_TO_STRING_CONSTRAINTS, serializableOutcomesToStringConstraints);

      Map<String, FeatureExpr> outcomesToConstraints =
          subtraceOutcomeConstraint.getOutcomesToConstraints();

      for (Map.Entry<String, FeatureExpr> entry : outcomesToConstraints.entrySet()) {
        String constraint = entry.getValue().toTextExpr().replaceAll("definedEx\\(", "");

        for (String option : this.options) {
          constraint = constraint.replaceAll(option + "\\)", option);
        }

        serializableOutcomesToStringConstraints.put(entry.getKey(), constraint);
      }

      serialObjects.add(serialObject);
    }

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(file, serialObjects);
  }

  @Override
  public Set<SubtraceOutcomeConstraint> readFromFile(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    List<Map<String, Object>> input =
        mapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {});

    Set<SubtraceOutcomeConstraint> subtracesOutcomeConstraint = new HashSet<>();

    for (Map<String, Object> entry : input) {
      String id = (String) entry.get(ID);
      SubtraceOutcomeConstraint subtraceOutcomeConstraint = new SubtraceOutcomeConstraint(id);

      Map<String, FeatureExpr> outcomesToConstraints =
          subtraceOutcomeConstraint.getOutcomesToConstraints();
      Map<String, String> outcomesToStringConstraints =
          (Map<String, String>) entry.get(OUTCOMES_TO_STRING_CONSTRAINTS);

      for (Map.Entry<String, String> outcomeToStringConstraint :
          outcomesToStringConstraints.entrySet()) {
        String stringConstraint = outcomeToStringConstraint.getValue();

        FeatureExpr featureExpr;

        if (stringConstraint.equals("0")) {
          featureExpr = SATFeatureExprFactory.False();
        } else if (stringConstraint.equals("1")) {
          featureExpr = SATFeatureExprFactory.True();
        } else {
          featureExpr = MinConfigsGenerator.parseAsFeatureExpr(stringConstraint);
        }

        outcomesToConstraints.put(outcomeToStringConstraint.getKey(), featureExpr);
      }

      subtracesOutcomeConstraint.add(subtraceOutcomeConstraint);
    }

    return subtracesOutcomeConstraint;
  }

  @Override
  public String outputDir() {
    return Options.DIRECTORY
        + "/evaluation/idta/constraints/java/programs/subtraces/"
        + this.programName;
  }
}
