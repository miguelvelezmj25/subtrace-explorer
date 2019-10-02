package edu.cmu.cs.mvelezce.explorer.eval.constraints;

import de.fosd.typechef.featureexpr.FeatureExpr;
import de.fosd.typechef.featureexpr.sat.SATFeatureExprFactory;
import edu.cmu.cs.mvelezce.adapter.adapters.iGen.BaseIGenAdapter;
import edu.cmu.cs.mvelezce.adapter.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapter.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.adapter.adapters.pngtastic.BasePngtasticAdapter;
import edu.cmu.cs.mvelezce.adapter.adapters.trivial.BaseTrivialAdapter;
import edu.cmu.cs.mvelezce.explorer.eval.constraints.idta.IDTAConstraintsAnalyzer;
import edu.cmu.cs.mvelezce.explorer.eval.constraints.subtraces.SubtraceOutcomeConstraint;
import edu.cmu.cs.mvelezce.explorer.eval.constraints.subtraces.SubtracesConstraintsAnalyzer;
import org.junit.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ConstraintsEvaluationAnalysisTest {

  private void analyze(String programName, Set<String> options) throws Exception {
    Set<FeatureExpr> idtaConstraints = getIDTAConstraints(programName);
    Set<FeatureExpr> subtraceConstraints = getSubtraceConstraints(programName);

    ConstraintsEvaluationAnalysis analysis =
        new ConstraintsEvaluationAnalysis(programName, options);
    analysis.analyze(idtaConstraints, subtraceConstraints);
  }

  private Set<FeatureExpr> getSubtraceConstraints(String programName) throws IOException {
    SubtracesConstraintsAnalyzer subtracesConstraintsAnalyzer =
        new SubtracesConstraintsAnalyzer(programName);
    String[] args = new String[0];
    Set<SubtraceOutcomeConstraint> subtracesOutcomeConstraint =
        subtracesConstraintsAnalyzer.analyze(args);

    Set<FeatureExpr> subtraceConstraints = new HashSet<>();

    FeatureExpr True = SATFeatureExprFactory.True();
    FeatureExpr False = SATFeatureExprFactory.False();

    for (SubtraceOutcomeConstraint subtraceOutcomeConstraint : subtracesOutcomeConstraint) {
      Collection<FeatureExpr> constraints =
          subtraceOutcomeConstraint.getOutcomesToConstraints().values();

      for (FeatureExpr constraint : constraints) {
        if (constraint.equals(True) || constraint.equals(False)) {
          continue;
        }

        subtraceConstraints.add(constraint);
      }
    }

    if (subtraceConstraints.isEmpty()) {
      throw new RuntimeException("The GT constraints are empty");
    }

    return subtraceConstraints;
  }

  private Set<FeatureExpr> getIDTAConstraints(String programName) throws IOException {
    System.err.println(
        "Might want to change how to get the IDTA constraints to a map from statements to constraints");
    IDTAConstraintsAnalyzer idtaConstraintsAnalyzer = new IDTAConstraintsAnalyzer(programName);
    String[] args = new String[0];

    Set<FeatureExpr> idtaConstraints = idtaConstraintsAnalyzer.analyze(args);

    if (idtaConstraints.isEmpty()) {
      throw new RuntimeException("The IDTA constraints are empty");
    }

    return idtaConstraints;
  }

  @Test
  public void trivial() throws Exception {
    String programName = BaseTrivialAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(BaseTrivialAdapter.getListOfOptions());
    analyze(programName, options);
  }

  @Test
  public void iGen() throws Exception {
    String programName = BaseIGenAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(BaseIGenAdapter.getListOfOptions());
    analyze(programName, options);
  }

  @Test
  public void pngtasticCounter() throws Exception {
    String programName = BasePngtasticAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(BasePngtasticAdapter.getListOfOptions());
    analyze(programName, options);
  }

  @Test
  public void measureDiskOrderedScan() throws Exception {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(BaseMeasureDiskOrderedScanAdapter.getListOfOptions());
    analyze(programName, options);
  }

  @Test
  public void indexFiles() throws Exception {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(BaseIndexFilesAdapter.getListOfOptions());
    analyze(programName, options);
  }
}
