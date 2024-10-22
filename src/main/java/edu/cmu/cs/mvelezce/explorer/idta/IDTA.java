package edu.cmu.cs.mvelezce.explorer.idta;

import com.mijecu25.meme.utils.monitor.memory.MemoryMonitor;
import edu.cmu.cs.mvelezce.analysis.dynamic.BaseDynamicAnalysis;
import edu.cmu.cs.mvelezce.explorer.idta.config.ConfigAnalysis;
import edu.cmu.cs.mvelezce.explorer.idta.config.IDTAConfigAnalysis;
import edu.cmu.cs.mvelezce.explorer.idta.config.SpecificConfigsAnalysis;
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
import edu.cmu.cs.mvelezce.explorer.utils.FeatureExprUtils;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IDTA extends BaseDynamicAnalysis<Void> {

  public static final boolean USE_BDD = true;

  public static final String OUTPUT_DIR = Options.DIRECTORY + "/idta";

  private final DynamicAnalysisExecutor dynamicAnalysisExecutor;
  private final DynamicAnalysisResultsParser dynamicAnalysisResultsParser;
  private final ControlFlowStmtTaintAnalysis controlFlowStmtTaintsAnalysis;
  private final ControlFlowStmtPartitioningAnalysis controlFlowStmtPartitioningAnalysis;
  private final DTAConstraintCalculator dtaConstraintCalculator;
  private final IDTAPartitionsAnalysis IDTAPartitionsAnalysis;
  private final ConfigAnalysis configAnalysis;

  public IDTA(
      String programName, String workloadSize, List<String> options, Set<String> initialConfig) {
    super(programName, new HashSet<>(options), initialConfig);

    this.dynamicAnalysisExecutor = new DynamicAnalysisExecutor(programName);
    this.dynamicAnalysisResultsParser = new DynamicAnalysisResultsParser(programName);
    this.controlFlowStmtTaintsAnalysis =
        new ControlFlowStmtTaintAnalysis(programName, workloadSize, options);
    this.controlFlowStmtPartitioningAnalysis =
        new ControlFlowStmtPartitioningAnalysis(programName, workloadSize, options);
    this.dtaConstraintCalculator = new DTAConstraintCalculator(options);
    this.IDTAPartitionsAnalysis = new IDTAPartitionsAnalysis(programName, workloadSize, options);
    this.configAnalysis = new IDTAConfigAnalysis(programName, workloadSize, options, initialConfig);
  }

  public IDTA(
      String programName,
      String workloadSize,
      Set<Set<String>> configsToExecute,
      List<String> options) {
    super(programName, new HashSet<>(options), configsToExecute.iterator().next());

    this.dynamicAnalysisExecutor = new DynamicAnalysisExecutor(programName);
    this.dynamicAnalysisResultsParser = new DynamicAnalysisResultsParser(programName);
    this.controlFlowStmtTaintsAnalysis =
        new ControlFlowStmtTaintAnalysis(programName, workloadSize, options);
    this.controlFlowStmtPartitioningAnalysis =
        new ControlFlowStmtPartitioningAnalysis(programName, workloadSize, options);
    this.dtaConstraintCalculator = new DTAConstraintCalculator(options);
    this.IDTAPartitionsAnalysis = new IDTAPartitionsAnalysis(programName, workloadSize, options);
    this.configAnalysis =
        new SpecificConfigsAnalysis(programName, workloadSize, options, configsToExecute);
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

    Set<Set<String>> executedConfigs = this.configAnalysis.analyze();
    this.configAnalysis.writeToFile(executedConfigs);

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
    Set<String> config = this.configAnalysis.getInitialConfig();
    Set<Constraint> exploredConstraints = new HashSet<>();

    while (config != null) {
      this.configAnalysis.saveExecutedConfig(config);
      String stringConstraint = ConstraintUtils.parseAsConstraint(config, this.getOptions());
      System.out.println("Executing config: " + stringConstraint);
      Constraint exploringConstraint =
          new Constraint(FeatureExprUtils.parseAsFeatureExpr(IDTA.USE_BDD, stringConstraint));
      exploredConstraints.add(exploringConstraint);

      this.dynamicAnalysisExecutor.runAnalysis(config);
      System.out.println();
      System.out.println("Done running program");
      System.out.println();
      Set<DecisionTaints> decisionTaints = this.dynamicAnalysisResultsParser.parseResults();

      this.controlFlowStmtTaintsAnalysis.saveTaints(config, decisionTaints);
      this.controlFlowStmtPartitioningAnalysis.savePartitions(config, decisionTaints);
      this.IDTAPartitionsAnalysis.savePartitions(
          this.controlFlowStmtPartitioningAnalysis.getStatementsToData().values());

      Set<Constraint> currentConstraints =
          DTAConstraintCalculator.deriveIDTAConstraints(
              this.controlFlowStmtPartitioningAnalysis.getStatementsToData().values());
      Set<Constraint> constraintsToExplore =
          this.dtaConstraintCalculator.getConstraintsToExplore(
              exploredConstraints, currentConstraints);
      System.out.println("Constraints yet to explore " + constraintsToExplore.size());

      long start = System.nanoTime();
      config = this.configAnalysis.getNextConfig(constraintsToExplore);
      long end = System.nanoTime();
      System.out.println("Get next config: " + (end - start) / 1E9);

      sampleConfigs++;
      MemoryMonitor.printMemoryUsage("Memory: ");
      System.out.println();
    }

    System.out.println();
    System.out.println("Configs sampled by IDTA: " + sampleConfigs);
  }
}
