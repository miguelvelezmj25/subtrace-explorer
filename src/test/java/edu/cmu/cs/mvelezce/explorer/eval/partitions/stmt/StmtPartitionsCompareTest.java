package edu.cmu.cs.mvelezce.explorer.eval.partitions.stmt;

import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.explorer.idta.results.statement.ControlFlowStmtPartitioningAnalysis;
import edu.cmu.cs.mvelezce.explorer.idta.results.statement.info.ControlFlowStmtPartitioning;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

public class StmtPartitionsCompareTest {

  @Test
  public void lucene_small_large() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    ControlFlowStmtPartitioningAnalysis analysis =
        new ControlFlowStmtPartitioningAnalysis(programName, workloadSize);

    String[] args = new String[0];
    Set<ControlFlowStmtPartitioning> smallResults = analysis.analyze(args);

    workloadSize = "large";
    analysis = new ControlFlowStmtPartitioningAnalysis(programName, workloadSize);
    Set<ControlFlowStmtPartitioning> largeResults = analysis.analyze(args);

    StmtPartitionsCompare compare = new StmtPartitionsCompare(programName);
    compare.compare(smallResults, largeResults);
  }

  @Test
  public void lucene_small_large_numberOptions() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    ControlFlowStmtPartitioningAnalysis analysis =
        new ControlFlowStmtPartitioningAnalysis(programName, workloadSize);

    String[] args = new String[0];
    Set<ControlFlowStmtPartitioning> smallResults = analysis.analyze(args);

    workloadSize = "large";
    analysis = new ControlFlowStmtPartitioningAnalysis(programName, workloadSize);
    Set<ControlFlowStmtPartitioning> largeResults = analysis.analyze(args);

    StmtPartitionsCompare compare = new StmtPartitionsCompare(programName);
    compare.compareNumberInteractingOptions(smallResults, largeResults);
  }
}
