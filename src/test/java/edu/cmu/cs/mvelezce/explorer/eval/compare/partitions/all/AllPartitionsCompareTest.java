package edu.cmu.cs.mvelezce.explorer.eval.compare.partitions.all;

import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;
import edu.cmu.cs.mvelezce.explorer.idta.results.partitions.IDTAPartitionsAnalysis;
import org.junit.Test;

import java.io.File;
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

    workloadSize = "specific/large";
    analysis = new IDTAPartitionsAnalysis(programName, workloadSize);
    Set<Partition> largeResults = analysis.analyze(args);

    AllPartitionsCompare allPartitionsCompare = new AllPartitionsCompare(programName);
    allPartitionsCompare.compare(smallResults, largeResults);
  }

  @Test
  public void lucene_large_0_1() throws IOException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    String workloadSize = "large";
    IDTAPartitionsAnalysis analysis = new IDTAPartitionsAnalysis(programName, workloadSize);
    Set<Partition> largeResults0 =
        analysis.readFromFile(
            new File(
                "src/main/resources/eval/java/programs/idta/analysis/IndexFiles/cc/specific/large/0/allPartitions/IndexFiles.json"));
    Set<Partition> largeResults1 =
        analysis.readFromFile(
            new File(
                "src/main/resources/eval/java/programs/idta/analysis/IndexFiles/cc/specific/large/1/allPartitions/IndexFiles.json"));

    AllPartitionsCompare allPartitionsCompare = new AllPartitionsCompare(programName);
    allPartitionsCompare.compare(largeResults0, largeResults1);
  }
}
