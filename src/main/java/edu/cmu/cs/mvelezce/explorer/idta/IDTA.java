package edu.cmu.cs.mvelezce.explorer.idta;

import de.fosd.typechef.featureexpr.FeatureExpr;
import de.fosd.typechef.featureexpr.sat.SATFeatureExprFactory;
import edu.cmu.cs.mvelezce.MinConfigsGenerator;
import edu.cmu.cs.mvelezce.analysis.dynamic.BaseDynamicAnalysis;
import edu.cmu.cs.mvelezce.explorer.idta.constraint.Constraint;
import edu.cmu.cs.mvelezce.explorer.idta.execute.DynamicAnalysisExecutor;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;
import edu.cmu.cs.mvelezce.explorer.idta.results.constraints.DTAConstraintCalculator;
import edu.cmu.cs.mvelezce.explorer.idta.results.parser.DecisionTaints;
import edu.cmu.cs.mvelezce.explorer.idta.results.parser.DynamicAnalysisResultsParser;
import edu.cmu.cs.mvelezce.explorer.idta.results.partitions.IDTAPartitionsAnalysis;
import edu.cmu.cs.mvelezce.explorer.idta.results.statement.ControlFlowStmtPartitioningAnalysis;
import edu.cmu.cs.mvelezce.explorer.idta.results.statement.ControlFlowStmtTaintAnalysis;
import edu.cmu.cs.mvelezce.explorer.idta.results.statement.info.ControlFlowStmtPartitioning;
import edu.cmu.cs.mvelezce.explorer.idta.results.statement.info.ControlFlowStmtTaints;
import edu.cmu.cs.mvelezce.explorer.utils.ConstraintUtils;
import edu.cmu.cs.mvelezce.utils.config.Options;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IDTA extends BaseDynamicAnalysis<Void> {

  public static final String OUTPUT_DIR = Options.DIRECTORY + "/idta";

  private final DynamicAnalysisExecutor dynamicAnalysisExecutor;
  private final DynamicAnalysisResultsParser dynamicAnalysisResultsParser;
  private final ControlFlowStmtTaintAnalysis controlFlowStmtTaintsAnalysis;
  private final ControlFlowStmtPartitioningAnalysis controlFlowStmtPartitioningAnalysis;

  private final IDTAPartitionsAnalysis IDTAPartitionsAnalysis;

  public IDTA(
      String programName, String workloadSize, List<String> options, Set<String> initialConfig) {
    super(programName, new HashSet<>(options), initialConfig);

    this.dynamicAnalysisExecutor = new DynamicAnalysisExecutor(programName);
    this.dynamicAnalysisResultsParser = new DynamicAnalysisResultsParser(programName);
    this.controlFlowStmtTaintsAnalysis =
        new ControlFlowStmtTaintAnalysis(programName, workloadSize, options);
    this.controlFlowStmtPartitioningAnalysis =
        new ControlFlowStmtPartitioningAnalysis(programName, workloadSize, options);

    this.IDTAPartitionsAnalysis = new IDTAPartitionsAnalysis(programName, workloadSize);
  }

  @Override
  public Void analyze() throws IOException, InterruptedException {
    this.runProgramAnalysis();

    Set<ControlFlowStmtTaints> controlFlowStmtTaintInfos =
        this.controlFlowStmtTaintsAnalysis.analyze();
    this.controlFlowStmtTaintsAnalysis.writeToFile(controlFlowStmtTaintInfos);

    Set<ControlFlowStmtPartitioning> controlFlowStmtPartitioningsInfo =
        this.controlFlowStmtPartitioningAnalysis.analyze();
    this.controlFlowStmtPartitioningAnalysis.writeToFile(controlFlowStmtPartitioningsInfo);

    Set<Partition> partitions = this.IDTAPartitionsAnalysis.analyze();
    this.IDTAPartitionsAnalysis.writeToFile(partitions);

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
      this.IDTAPartitionsAnalysis.savePartitions(
          this.controlFlowStmtPartitioningAnalysis.getStatementsToData().values());

      Set<Constraint> currentConstraints =
          DTAConstraintCalculator.deriveIDTAConstraints(
              this.controlFlowStmtPartitioningAnalysis.getStatementsToData().values());
      Set<Constraint> constraintsToExplore =
          DTAConstraintCalculator.getConstraintsToExplore(exploredConstraints, currentConstraints);
      System.out.println("Constraints yet to explore " + constraintsToExplore.size());

      config = this.getNextGreedyConfig(constraintsToExplore);
      sampleConfigs++;
    }

    System.out.println("Configs sampled by IDTA: " + sampleConfigs);
  }

  @Nullable
  private Set<String> getNextGreedyConfig(Set<Constraint> constraintsToExplore) {
    if (constraintsToExplore.isEmpty()) {
      return null;
    }

    FeatureExpr formula = SATFeatureExprFactory.True();

    for (Constraint constraint : constraintsToExplore) {
      if (formula.mex(constraint.getFeatureExpr()).isTautology()) {
        continue;
      }

      formula = formula.and(constraint.getFeatureExpr());
    }

    return ConstraintUtils.toConfig(formula, this.getOptions());
  }
}
