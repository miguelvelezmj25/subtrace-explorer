package edu.cmu.cs.mvelezce.explorer.eval.compare.taints.stmt;

import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.explorer.idta.results.statement.ControlFlowStmtTaintAnalysis;
import edu.cmu.cs.mvelezce.explorer.idta.results.statement.info.ControlFlowStmtTaints;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class StmtTaintsCompareTest {

  @Test
  public void lucene_large_0_1() throws IOException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    String workloadSize = "large";
    ControlFlowStmtTaintAnalysis analysis =
        new ControlFlowStmtTaintAnalysis(programName, workloadSize);
    Set<ControlFlowStmtTaints> largeResults0 =
        analysis.readFromFile(
            new File(
                "src/main/resources/eval/java/programs/idta/analysis/IndexFiles/cc/specific/large/0/taints"));
    Set<ControlFlowStmtTaints> largeResults1 =
        analysis.readFromFile(
            new File(
                "src/main/resources/eval/java/programs/idta/analysis/IndexFiles/cc/specific/large/1/taints"));

    StmtTaintsCompare stmtTaintsCompare = new StmtTaintsCompare(programName);
    stmtTaintsCompare.compare(largeResults0, largeResults0);
  }

  @Test
  public void lucene_small_large() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    ControlFlowStmtTaintAnalysis analysis =
        new ControlFlowStmtTaintAnalysis(programName, workloadSize);

    String[] args = new String[0];
    Set<ControlFlowStmtTaints> smallResults = analysis.analyze(args);

    workloadSize = "specific/large";
    analysis = new ControlFlowStmtTaintAnalysis(programName, workloadSize);
    Set<ControlFlowStmtTaints> largeResults = analysis.analyze(args);

    StmtTaintsCompare stmtTaintsCompare = new StmtTaintsCompare(programName);
    stmtTaintsCompare.compare(smallResults, largeResults);
  }

  @Test
  public void lucene_small_large_compareTaints_0() throws IOException, InterruptedException {
    //    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    //    String workloadSize = "small";
    //    ControlFlowStmtTaintAnalysis analysis =
    //        new ControlFlowStmtTaintAnalysis(programName, workloadSize);
    //
    //    String[] args = new String[0];
    //    Set<ControlFlowStmtTaints> smallResults = analysis.analyze(args);
    //
    //    workloadSize = "large";
    //    analysis = new ControlFlowStmtTaintAnalysis(programName, workloadSize);
    //    Set<ControlFlowStmtTaints> largeResults = analysis.analyze(args);
    //
    //    String packageName = "org.apache.lucene.core.index";
    //    String className = "FreqProxTermsWriter";
    //    String methodSignature =
    //
    // "flush(Ljava/util/Map;Lorg/apache/lucene/core/index/SegmentWriteState;Lorg/apache/lucene/core/index/Sorter$DocMap;)V";
    //    int decisionIndex = 2;
    //
    //    StmtTaintsCompare.compareTaints(
    //        smallResults, largeResults, packageName, className, methodSignature, decisionIndex);
  }
}
