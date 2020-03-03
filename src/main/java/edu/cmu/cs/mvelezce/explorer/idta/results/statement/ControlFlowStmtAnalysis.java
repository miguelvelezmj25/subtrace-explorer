package edu.cmu.cs.mvelezce.explorer.idta.results.statement;

import edu.cmu.cs.mvelezce.analysis.dynamic.BaseDynamicAnalysis;
import edu.cmu.cs.mvelezce.explorer.idta.results.parser.DecisionTaints;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @param <T> The resulting data of the analysis
 * @param <E> The data that is saved for each control flow statement
 */
abstract class ControlFlowStmtAnalysis<T, E> extends BaseDynamicAnalysis<T> {

  private final String workloadSize;
  private final Map<String, E> statementsToData = new HashMap<>();
  private final List<String> options;

  ControlFlowStmtAnalysis(String programName, String workloadSize, List<String> options) {
    super(programName, new HashSet<>(options), new HashSet<>());

    this.workloadSize = workloadSize;
    this.options = options;
  }

  static String getClassName(String statementComponent) {
    int indexOfLastSlash = statementComponent.lastIndexOf("/");
    return statementComponent.substring(indexOfLastSlash + 1);
  }

  static String getPackageName(String statementComponent) {
    int indexOfLastSlash = statementComponent.lastIndexOf("/");
    statementComponent = statementComponent.substring(0, indexOfLastSlash);

    return statementComponent.replaceAll("/", ".");
  }

  public T analyze(String[] args) throws IOException, InterruptedException {
    Options.getCommandLine(args);
    String outputFile = this.outputDir();
    File file = new File(outputFile);
    Options.checkIfDeleteResult(file);

    if (file.exists()) {
      return this.readFromFile(file);

    } else {
      T analysisResults = this.analyze();

      if (Options.checkIfSave()) {
        this.writeToFile(analysisResults);
      }

      return analysisResults;
    }
  }

  abstract void addData(Set<String> config, Set<DecisionTaints> results);

  abstract void addStatements(Set<DecisionTaints> results);

  public Map<String, E> getStatementsToData() {
    return statementsToData;
  }

  public String getWorkloadSize() {
    return workloadSize;
  }

  public List<String> getOptionsList() {
    return options;
  }
}
