package edu.cmu.cs.mvelezce.explorer.eval.compare.partitions.all;

import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;
import edu.cmu.cs.mvelezce.explorer.idta.results.partitions.IDTAPartitionsAnalysis;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

public class CheckPartitionAnalysisTest {

  @Test
  public void lucene_small_0() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    IDTAPartitionsAnalysis analysis = new IDTAPartitionsAnalysis(programName, workloadSize);

    String[] args = new String[0];
    Set<Partition> results = analysis.analyze(args);

    CheckPartitionAnalysis.check(
        results,
        "(!MAX_BUFFERED_DOCS && MAX_TOKEN_LENGTH && !COMMIT_ON_CLOSE && RAM_BUFFER_SIZE_MB)");
  }

  @Test
  public void lucene_small_1() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    IDTAPartitionsAnalysis analysis = new IDTAPartitionsAnalysis(programName, workloadSize);

    String[] args = new String[0];
    Set<Partition> results = analysis.analyze(args);

    CheckPartitionAnalysis.check(
        results,
        "(!MAX_BUFFERED_DOCS && !MAX_TOKEN_LENGTH && !COMMIT_ON_CLOSE && RAM_BUFFER_SIZE_MB)");
  }
}
