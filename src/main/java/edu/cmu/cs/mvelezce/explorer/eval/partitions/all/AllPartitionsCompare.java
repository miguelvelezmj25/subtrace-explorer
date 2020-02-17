package edu.cmu.cs.mvelezce.explorer.eval.partitions.all;

import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;

import java.util.HashSet;
import java.util.Set;

public final class AllPartitionsCompare {

  public static void compare(Set<Partition> baseResults, Set<Partition> newResults) {
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
            System.out.println(newPartition + " -> " + basePartition);
          }
        }
      }
    }
  }

  private static Set<Partition> getEquivalentPartitions(
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
}
