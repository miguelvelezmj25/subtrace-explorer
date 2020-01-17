package edu.cmu.cs.mvelezce.explorer.idta;

import edu.cmu.cs.mvelezce.MinConfigsGenerator;
import edu.cmu.cs.mvelezce.analysis.dynamic.BaseDynamicAnalysis;
import edu.cmu.cs.mvelezce.explorer.eval.constraints.idta.constraint.ConfigConstraint;
import edu.cmu.cs.mvelezce.explorer.idta.constraint.Constraint;
import edu.cmu.cs.mvelezce.explorer.idta.execute.DynamicAnalysisExecutor;
import edu.cmu.cs.mvelezce.explorer.idta.other.DTAConstraintCalculator;
import edu.cmu.cs.mvelezce.explorer.idta.results.dta.constraints.DTAConstraintAnalysis;
import edu.cmu.cs.mvelezce.explorer.idta.results.parser.DecisionTaints;
import edu.cmu.cs.mvelezce.explorer.idta.results.parser.DynamicAnalysisResultsParser;
import edu.cmu.cs.mvelezce.explorer.idta.results.statement.ControlFlowStmtPartitioningAnalysis;
import edu.cmu.cs.mvelezce.explorer.idta.results.statement.ControlFlowStmtTaintAnalysis;
import edu.cmu.cs.mvelezce.explorer.idta.results.statement.info.ControlFlowStmtPartitioning;
import edu.cmu.cs.mvelezce.explorer.idta.results.statement.info.ControlFlowStmtTaints;
import edu.cmu.cs.mvelezce.explorer.utils.ConstraintUtils;
import edu.cmu.cs.mvelezce.utils.config.Options;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IDTA extends BaseDynamicAnalysis<Void> {

  public static final String OUTPUT_DIR = Options.DIRECTORY + "/idta";

  private final DynamicAnalysisExecutor dynamicAnalysisExecutor;
  private final DynamicAnalysisResultsParser dynamicAnalysisResultsParser;
  private final ControlFlowStmtTaintAnalysis controlFlowStmtTaintsAnalysis;
  private final ControlFlowStmtPartitioningAnalysis controlFlowStmtPartitioningAnalysis;

  private final ConfigConstraintAnalyzer configConstraintAnalyzer;
  private final DTAConstraintCalculator DTAConstraintCalculator;
  private final DTAConstraintAnalysis DTAConstraintAnalysis;

  public IDTA(
      String programName, String workloadSize, List<String> options, Set<String> initialConfig) {
    super(programName, new HashSet<>(options), initialConfig);

    this.dynamicAnalysisExecutor = new DynamicAnalysisExecutor(programName);
    this.dynamicAnalysisResultsParser = new DynamicAnalysisResultsParser(programName);
    this.controlFlowStmtTaintsAnalysis =
        new ControlFlowStmtTaintAnalysis(programName, workloadSize, options);
    this.controlFlowStmtPartitioningAnalysis =
        new ControlFlowStmtPartitioningAnalysis(programName, workloadSize, options);

    this.configConstraintAnalyzer = new ConfigConstraintAnalyzer(new HashSet<>(options));
    this.DTAConstraintCalculator = new DTAConstraintCalculator(options);
    this.DTAConstraintAnalysis = new DTAConstraintAnalysis(programName, workloadSize);
  }

  @Nullable
  @Override
  public Void analyze() throws IOException, InterruptedException {
    this.runProgramAnalysis();

    Set<ConfigConstraint> constraints = this.DTAConstraintAnalysis.analyze();
    this.DTAConstraintAnalysis.writeToFile(constraints);

    System.err.println("Might want to save the constraints per decision, not the taints");
    Set<ControlFlowStmtTaints> controlFlowStatementInfos =
        this.controlFlowStmtTaintsAnalysis.analyze();
    this.controlFlowStmtTaintsAnalysis.writeToFile(controlFlowStatementInfos);

    Set<ControlFlowStmtPartitioning> dataFlowConstraints =
        this.controlFlowStmtPartitioningAnalysis.analyze();
    this.controlFlowStmtPartitioningAnalysis.writeToFile(dataFlowConstraints);

    return null;
  }

  @Override
  public void writeToFile(Void value) {}

  @Override
  public Void readFromFile(File file) {
    throw new UnsupportedOperationException(
        "You cannot call the read method for the phosphor analysis. You want to call the specific"
            + " read methods for the results of a phosphor analysis.");
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR + "/analysis" + this.getProgramName() + "/cc";
  }

  private void runProgramAnalysis() throws IOException, InterruptedException {
    int sampleConfigs = 0;

    Set<String> config = this.getInitialConfig();

    Set<Constraint> exploredConstraints = new HashSet<>();

    Set<ConfigConstraint> configConstraintsToSatisfy = new HashSet<>();
    Set<ConfigConstraint> satisfiedConfigConstraints = new HashSet<>();
    Set<ConfigConstraint> exploredConfigConstraints = new HashSet<>();

    while (config != null) {
      String stringConstraint = ConstraintUtils.parseAsConstraint(config, this.getOptions());
      Constraint exploringConstraint =
          new Constraint(MinConfigsGenerator.parseAsFeatureExpr(stringConstraint));
      exploredConstraints.add(exploringConstraint);

      this.dynamicAnalysisExecutor.runAnalysis(config);
      Set<DecisionTaints> decisionTaints = this.dynamicAnalysisResultsParser.parseResults();
      //      System.out.println(results.size());

      this.controlFlowStmtTaintsAnalysis.saveTaints(config, decisionTaints);
      this.controlFlowStmtPartitioningAnalysis.savePartitions(config, decisionTaints);

      System.err.println("CHECK LOGIC BELOW");
      System.err.println("Get the constraints from the control flow constraints");
      Collection<Set<ConfigConstraint>> constraintsSet =
          this.DTAConstraintCalculator.deriveConstraints(decisionTaints, config).values();
      Set<ConfigConstraint> analysisConstraints = new HashSet<>();

      for (Set<ConfigConstraint> entry : constraintsSet) {
        analysisConstraints.addAll(entry);
      }

      this.DTAConstraintAnalysis.addConstraints(analysisConstraints);

      configConstraintsToSatisfy.addAll(analysisConstraints);
      configConstraintsToSatisfy.removeAll(satisfiedConfigConstraints);

      System.out.println("Constraints yet to explore " + configConstraintsToSatisfy.size());

      Set<Set<String>> configsToRun =
          this.configConstraintAnalyzer.getConfigsThatSatisfyConfigConstraints(
              configConstraintsToSatisfy, exploredConfigConstraints);

      config = this.getNextConfig(configsToRun);
      /////// CHECK

      sampleConfigs++;
      break;
    }

    System.out.println("Configs sampled by IDTA: " + sampleConfigs);
  }

  @Nullable
  private Set<String> getNextConfig(Set<Set<String>> configsToRun) {
    if (configsToRun.isEmpty()) {
      return null;
    }

    // Optimize
    return configsToRun.iterator().next();
  }
}
