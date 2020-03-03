package edu.cmu.cs.mvelezce.explorer.eval.compare.partitions.stmt;

import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.explorer.idta.results.statement.ControlFlowStmtPartitioningAnalysis;
import edu.cmu.cs.mvelezce.explorer.idta.results.statement.info.ControlFlowStmtPartitioning;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class StmtPartitionsCompareTest {

  @Test
  public void lucene_large_0_1() throws IOException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    String workloadSize = "large";
    ControlFlowStmtPartitioningAnalysis analysis =
        new ControlFlowStmtPartitioningAnalysis(programName, workloadSize);
    Set<ControlFlowStmtPartitioning> largeResults0 =
        analysis.readFromFile(
            new File(
                "src/main/resources/eval/java/programs/idta/analysis/IndexFiles/cc/specific/large/0/partitions"));
    Set<ControlFlowStmtPartitioning> largeResults1 =
        analysis.readFromFile(
            new File(
                "src/main/resources/eval/java/programs/idta/analysis/IndexFiles/cc/specific/large/1/partitions"));

    StmtPartitionsCompare stmtPartitionsCompare = new StmtPartitionsCompare(programName);
    stmtPartitionsCompare.compare(largeResults0, largeResults1);
  }

  @Test
  public void lucene_small_large() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    ControlFlowStmtPartitioningAnalysis analysis =
        new ControlFlowStmtPartitioningAnalysis(programName, workloadSize);

    String[] args = new String[0];
    Set<ControlFlowStmtPartitioning> smallResults = analysis.analyze(args);

    workloadSize = "specific/large";
    analysis = new ControlFlowStmtPartitioningAnalysis(programName, workloadSize);
    Set<ControlFlowStmtPartitioning> largeResults = analysis.analyze(args);

    StmtPartitionsCompare stmtPartitionsCompare = new StmtPartitionsCompare(programName);
    stmtPartitionsCompare.compare(smallResults, largeResults);
  }

  //  @Test
  //  public void lucene_small_large_numberOptions() throws IOException, InterruptedException {
  //    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
  //    String workloadSize = "small";
  //    ControlFlowStmtPartitioningAnalysis analysis =
  //        new ControlFlowStmtPartitioningAnalysis(programName, workloadSize);
  //
  //    String[] args = new String[0];
  //    Set<ControlFlowStmtPartitioning> smallResults = analysis.analyze(args);
  //
  //    workloadSize = "large";
  //    analysis = new ControlFlowStmtPartitioningAnalysis(programName, workloadSize);
  //    Set<ControlFlowStmtPartitioning> largeResults = analysis.analyze(args);
  //
  //    StmtPartitionsCompare compare = new StmtPartitionsCompare(programName);
  //    compare.compareNumberInteractingOptions(smallResults, largeResults);
  //  }
}
