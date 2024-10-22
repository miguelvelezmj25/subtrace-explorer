package edu.cmu.cs.mvelezce.explorer.eval.constraints.subtraces;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.analysis.Analysis;
import edu.cmu.cs.mvelezce.explorer.gt.valueanalysis.SubtraceAnalysisInfo;
import edu.cmu.cs.mvelezce.explorer.idta.IDTA;
import edu.cmu.cs.mvelezce.explorer.utils.ConstraintUtils;
import edu.cmu.cs.mvelezce.explorer.utils.FeatureExprUtils;
import edu.cmu.cs.mvelezce.utils.config.Options;
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
        String stringConstraints = ConstraintUtils.toStringConstraints(configs, this.options);

        if (stringConstraints.isEmpty()) {
          outcomesToConstraints.put(entry.getKey(), FeatureExprUtils.getFalse(IDTA.USE_BDD));

          continue;
        }

        FeatureExpr featureExpr =
            FeatureExprUtils.parseAsFeatureExpr(IDTA.USE_BDD, stringConstraints);
        outcomesToConstraints.put(entry.getKey(), featureExpr);
      }

      subtracesOutcomeConstraint.add(subtraceOutcomeConstraint);

      count++;

      System.out.println("Subtraces to analyze: " + (this.subtraceAnalysisInfos.size() - count));
    }

    return subtracesOutcomeConstraint;
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
        FeatureExpr constraint = entry.getValue();
        String prettyConstraint = ConstraintUtils.prettyPrintFeatureExpr(constraint, this.options);

        serializableOutcomesToStringConstraints.put(entry.getKey(), prettyConstraint);
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
          featureExpr = FeatureExprUtils.getFalse(IDTA.USE_BDD);
        } else if (stringConstraint.equals("1")) {
          featureExpr = FeatureExprUtils.getTrue(IDTA.USE_BDD);
        } else {
          featureExpr = FeatureExprUtils.parseAsFeatureExpr(IDTA.USE_BDD, stringConstraint);
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
