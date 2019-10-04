package edu.cmu.cs.mvelezce.explorer.eval.constraints.idta;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.MinConfigsGenerator;
import edu.cmu.cs.mvelezce.analysis.Analysis;
import edu.cmu.cs.mvelezce.explorer.utils.ConstraintUtils;
import edu.cmu.cs.mvelezce.utils.Options;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public abstract class ConstraintAnalyzer implements Analysis<Set<FeatureExpr>> {

  static {
    System.err.println("Stop using your own ConfigConstraint class and use the FeatureExpr lib");
  }

  private final String programName;
  private final Set<String> options;

  ConstraintAnalyzer(String programName, Set<String> options) {
    this.programName = programName;
    this.options = options;
  }

  protected abstract List<String> getStringConstraints();

  String getProgramName() {
    return programName;
  }

  Set<String> getOptions() {
    return options;
  }

  @Override
  public Set<FeatureExpr> analyze() {
    List<String> stringConstraints = this.getStringConstraints();

    return new HashSet<>(MinConfigsGenerator.getFeatureExprs(stringConstraints));
  }

  @Override
  public Set<FeatureExpr> analyze(String[] args) throws Exception {
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

    Set<FeatureExpr> interactions = this.analyze();

    if (Options.checkIfSave()) {
      this.writeToFile(interactions);
    }

    return interactions;
  }

  @Override
  public void writeToFile(Set<FeatureExpr> interactions) throws IOException {
    System.err.println("This code was copied and pasted");
    String outputFile = this.outputDir() + "/" + this.programName + Options.DOT_JSON;
    File file = new File(outputFile);
    file.getParentFile().mkdirs();

    List<String> stringInteractions = new ArrayList<>();

    for (FeatureExpr featureExpr : interactions) {
      String stringInteraction = ConstraintUtils.prettyPrintFeatureExpr(featureExpr, this.options);

      stringInteractions.add(stringInteraction);
    }

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(file, stringInteractions);
  }

  @Override
  public Set<FeatureExpr> readFromFile(File file) throws IOException {
    System.err.println("This code was copied and pasted");
    ObjectMapper mapper = new ObjectMapper();
    List<String> stringConstraints = mapper.readValue(file, new TypeReference<List<String>>() {});

    return new HashSet<>(MinConfigsGenerator.getFeatureExprs(stringConstraints));
  }

  @Override
  public String outputDir() {
    return Options.DIRECTORY + "/evaluation/idta/constraints/java/programs";
  }
}
