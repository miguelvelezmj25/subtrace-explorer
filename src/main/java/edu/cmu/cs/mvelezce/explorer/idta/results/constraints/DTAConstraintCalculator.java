package edu.cmu.cs.mvelezce.explorer.idta.results.constraints;

import edu.cmu.cs.mvelezce.explorer.idta.constraint.Constraint;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partitioning;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class DTAConstraintCalculator {

  private DTAConstraintCalculator() {}

  public static Set<Constraint> deriveIDTAConstraints(Collection<Partitioning> partitionings) {
    Set<Partition> allPartitions = getAllPartitions(partitionings);
    Set<Constraint> constraints = new HashSet<>();

    for (Partition partition : allPartitions) {
      constraints.add(new Constraint(partition.getFeatureExpr()));
    }

    return constraints;
  }

  private static Set<Partition> getAllPartitions(Collection<Partitioning> partitionings) {
    Set<Partition> partitions = new HashSet<>();
    for (Partitioning partitioning : partitionings) {
      partitions.addAll(partitioning.getPartitions());
    }

    return partitions;
  }

  public static Set<Constraint> getConstraintsToExplore(
      Set<Constraint> exploredConstraints, Set<Constraint> currentConstraints) {
    Set<Constraint> constraintsToExplore = new HashSet<>();

    for (Constraint currentConstraint : currentConstraints) {
      boolean explored = false;

      for (Constraint exploredConstraint : exploredConstraints) {
        if (exploredConstraint
            .getFeatureExpr()
            .implies(currentConstraint.getFeatureExpr())
            .isTautology()) {
          explored = true;
          break;
        }
      }

      if (!explored) {
        constraintsToExplore.add(currentConstraint);
      }
    }

    return constraintsToExplore;
  }
}
