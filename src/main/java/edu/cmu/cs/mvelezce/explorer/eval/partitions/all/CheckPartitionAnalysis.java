package edu.cmu.cs.mvelezce.explorer.eval.partitions.all;

import edu.cmu.cs.mvelezce.MinConfigsGenerator;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;

import java.util.Set;

public class CheckPartitionAnalysis {

  public static void check(Set<Partition> partitions, String prettyPartitionToCheck) {
    Partition partitionToCheck =
        new Partition(MinConfigsGenerator.parseAsBDDFeatureExpr(prettyPartitionToCheck));

    if (partitions.contains(partitionToCheck)) {
      System.out.println("The exact same partition was found");

      return;
    }

    System.out.println("The partition was not found");
  }
}
