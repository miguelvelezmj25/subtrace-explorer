package edu.cmu.cs.mvelezce.explorer.eval.partitions.all;

import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;
import edu.cmu.cs.mvelezce.utils.config.Options;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

public final class AllPartitionsCompare {

  public static final String DOT_TXT = ".txt";

  private static final String OUTPUT_DIR = Options.DIRECTORY + "/eval/java/programs/partitions/all";

  private final String programName;

  AllPartitionsCompare(String programName) {
    this.programName = programName;
  }

  public static String comparePartitions(Set<Partition> baseResults, Set<Partition> newResults) {
    StringBuilder results = new StringBuilder();
    Set<Partition> equivalentPartitions = getEquivalentPartitions(baseResults, newResults);

    for (Partition newPartition : newResults) {
      if (equivalentPartitions.contains(newPartition)) {
        continue;
      }

      for (Partition basePartition : baseResults) {
        if (equivalentPartitions.contains(basePartition)) {
          continue;
        }

        if (newPartition.getFeatureExpr().implies(basePartition.getFeatureExpr()).isTautology()) {
          if (!basePartition.getFeatureExpr().toString().contains("|")) {
            results.append("\t");
            results.append(newPartition);
            results.append(" -> ");
            results.append(basePartition);
            results.append("\n");
          }
        }
      }
    }

    return results.toString();
  }

  public static Set<Partition> getEquivalentPartitions(
      Set<Partition> baseResults, Set<Partition> newResults) {
    Set<Partition> equivalentPartitions = new HashSet<>();

    for (Partition p1 : baseResults) {
      boolean equiv = false;

      for (Partition p2 : newResults) {
        if (p1.getFeatureExpr().equiv(p2.getFeatureExpr()).isTautology()) {
          equiv = true;
          break;
        }
      }

      if (equiv) {
        equivalentPartitions.add(p1);
      }
    }

    return equivalentPartitions;
  }

  public void compareResults(Set<Partition> baseResults, Set<Partition> newResults)
      throws IOException {
    System.err.println(
        "CHANGE LOGIC OF CHECKING FOR '|' TO CHECKING IF THE PARTITION IS THE REMAINING ONE");
    File outputFile = new File(OUTPUT_DIR + "/compare/" + this.programName + DOT_TXT);
    outputFile.getParentFile().mkdirs();

    if (outputFile.exists()) {
      FileUtils.forceDelete(outputFile);
    }

    PrintWriter writer = new PrintWriter(outputFile);
    writer.write(comparePartitions(baseResults, newResults));
    writer.flush();
    writer.close();
  }
}
