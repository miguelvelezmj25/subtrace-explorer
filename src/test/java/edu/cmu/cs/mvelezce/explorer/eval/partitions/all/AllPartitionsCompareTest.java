package edu.cmu.cs.mvelezce.explorer.eval.partitions.all;

import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;
import edu.cmu.cs.mvelezce.explorer.idta.results.partitions.IDTAPartitionsAnalysis;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

public class AllPartitionsCompareTest {

  @Test
  public void lucene_small_large() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    IDTAPartitionsAnalysis analysis = new IDTAPartitionsAnalysis(programName, workloadSize);

    String[] args = new String[0];
    Set<Partition> smallResults = analysis.analyze(args);

    workloadSize = "large";
    analysis = new IDTAPartitionsAnalysis(programName, workloadSize);
    Set<Partition> largeResults = analysis.analyze(args);

    AllPartitionsCompare compare = new AllPartitionsCompare(programName);
    compare.compareResults(smallResults, largeResults);
  }
}
