package edu.cmu.cs.mvelezce.explorer.eval.constraints.idta;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.adapters.iGen.BaseIGenAdapter;
import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.adapters.pngtastic.BasePngtasticAdapter;
import edu.cmu.cs.mvelezce.adapters.trivial.BaseTrivialAdapter;
import edu.cmu.cs.mvelezce.explorer.eval.constraints.idta.constraint.ConfigConstraint;
import edu.cmu.cs.mvelezce.explorer.idta.results.dta.constraints.DTAConstraintAnalysis;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class IDTAConstraintsAnalyzerTest {

  private void analyzeInteractions(String programName, String workloadSize, Set<String> options)
      throws Exception {
    DTAConstraintAnalysis constraintAnalysis = new DTAConstraintAnalysis(programName, workloadSize);
    String[] args = new String[0];
    Set<ConfigConstraint> constraints = constraintAnalysis.analyze(args);
    IDTAConstraintsAnalyzer analysis =
        new IDTAConstraintsAnalyzer(programName, constraints, options);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    Set<FeatureExpr> write = analysis.analyze(args);

    analysis = new IDTAConstraintsAnalyzer(programName);
    args = new String[0];
    Set<FeatureExpr> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void trivialSmall() throws Exception {
    String programName = BaseTrivialAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    Set<String> options = new HashSet<>(BaseTrivialAdapter.getListOfOptions());
    analyzeInteractions(programName, workloadSize, options);
  }

  @Test
  public void iGenSmall() throws Exception {
    String programName = BaseIGenAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    Set<String> options = new HashSet<>(BaseIGenAdapter.getListOfOptions());
    analyzeInteractions(programName, workloadSize, options);
  }

  @Test
  public void pngtasticCounterSmall() throws Exception {
    String programName = BasePngtasticAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    Set<String> options = new HashSet<>(BasePngtasticAdapter.getListOfOptions());
    analyzeInteractions(programName, workloadSize, options);
  }

  @Test
  public void measuredDiskOrderedScanSmall() throws Exception {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    Set<String> options = new HashSet<>(BaseMeasureDiskOrderedScanAdapter.getListOfOptions());
    analyzeInteractions(programName, workloadSize, options);
  }

  @Test
  public void indexFilesSmall() throws Exception {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    Set<String> options = new HashSet<>(BaseIndexFilesAdapter.getListOfOptions());
    analyzeInteractions(programName, workloadSize, options);
  }
}
