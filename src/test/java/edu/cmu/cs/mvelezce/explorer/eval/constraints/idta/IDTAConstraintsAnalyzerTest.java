package edu.cmu.cs.mvelezce.explorer.eval.constraints.idta;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.adapter.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapter.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.adapter.adapters.trivial.BaseTrivialAdapter;
import edu.cmu.cs.mvelezce.explorer.eval.constraints.idta.constraint.ConfigConstraint;
import edu.cmu.cs.mvelezce.explorer.eval.constraints.idta.constraintanalysis.DTAConstraintAnalysis;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class IDTAConstraintsAnalyzerTest {

  private void analyzeInteractions(String programName, Set<String> options) throws Exception {
    DTAConstraintAnalysis constraintAnalysis = new DTAConstraintAnalysis(programName);
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
  public void trivial() throws Exception {
    String programName = BaseTrivialAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(BaseTrivialAdapter.getListOfOptions());
    analyzeInteractions(programName, options);
  }

  @Test
  public void measuredDiskOrderedScan() throws Exception {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(BaseMeasureDiskOrderedScanAdapter.getListOfOptions());
    analyzeInteractions(programName, options);
  }

  @Test
  public void indexFiles() throws Exception {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(BaseIndexFilesAdapter.getListOfOptions());
    analyzeInteractions(programName, options);
  }
}