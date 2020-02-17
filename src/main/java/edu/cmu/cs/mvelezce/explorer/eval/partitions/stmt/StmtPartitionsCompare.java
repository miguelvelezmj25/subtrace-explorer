package edu.cmu.cs.mvelezce.explorer.eval.partitions.stmt;

import edu.cmu.cs.mvelezce.explorer.eval.partitions.all.AllPartitionsCompare;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;
import edu.cmu.cs.mvelezce.explorer.idta.results.statement.info.ControlFlowStmtPartitioning;

import java.util.HashSet;
import java.util.Set;

public final class StmtPartitionsCompare {

  public static void compare(
      Set<ControlFlowStmtPartitioning> baseResults, Set<ControlFlowStmtPartitioning> newResults) {
    System.err.println(
        "CHANGE LOGIC OF CHECKING FOR '|' TO CHECKING IF THE PARTITION IS THE REMAINING ONE");
    Set<ControlFlowStmtPartitioning> extraStmts = getExtraStmts(baseResults, newResults);
    printExtraStmts(extraStmts);
    printDiffPartitions(baseResults, newResults, extraStmts);
  }

  private static void printDiffPartitions(
      Set<ControlFlowStmtPartitioning> baseResults,
      Set<ControlFlowStmtPartitioning> newResults,
      Set<ControlFlowStmtPartitioning> extraStmts) {
    System.out.println(
        "###############################################################################################################");
    System.out.println("Extra stmts:");

    for (ControlFlowStmtPartitioning newResult : newResults) {
      if (extraStmts.contains(newResult)) {
        continue;
      }

      for (ControlFlowStmtPartitioning baseResult : baseResults) {
        if (!newResult.getPackageName().equals(baseResult.getPackageName())
            || !newResult.getClassName().equals(baseResult.getClassName())
            || !newResult.getMethodSignature().equals(baseResult.getMethodSignature())
            || newResult.getDecisionIndex() != baseResult.getDecisionIndex()) {
          continue;
        }

        String result =
            AllPartitionsCompare.comparePartitions(
                baseResult.getInfo().getPartitions(), newResult.getInfo().getPartitions());

        if (result.isEmpty()) {
          continue;
        }

        System.out.println(
            newResult.getPackageName()
                + "."
                + newResult.getClassName()
                + "."
                + newResult.getMethodSignature()
                + "."
                + newResult.getDecisionIndex());
        System.out.println(result);
        System.out.println();
      }
    }

    System.out.println(
        "###############################################################################################################");
  }

  private static void printExtraStmts(Set<ControlFlowStmtPartitioning> extraStmts) {
    System.out.println(
        "###############################################################################################################");
    System.out.println("Extra stmts:");

    for (ControlFlowStmtPartitioning extraStmt : extraStmts) {
      System.out.println(
          extraStmt.getPackageName()
              + "."
              + extraStmt.getClassName()
              + "."
              + extraStmt.getMethodSignature()
              + "."
              + extraStmt.getDecisionIndex());

      for (Partition partition : extraStmt.getInfo().getPartitions()) {
        System.out.println("\t" + partition);
      }
    }

    System.out.println(
        "###############################################################################################################");
    System.out.println();
  }

  private static Set<ControlFlowStmtPartitioning> getExtraStmts(
      Set<ControlFlowStmtPartitioning> baseResults, Set<ControlFlowStmtPartitioning> newResults) {
    Set<ControlFlowStmtPartitioning> extraStmts = new HashSet<>();

    for (ControlFlowStmtPartitioning newResult : newResults) {
      boolean extraStmt = true;

      for (ControlFlowStmtPartitioning baseResult : baseResults) {
        if (newResult.getPackageName().equals(baseResult.getPackageName())
            && newResult.getClassName().equals(baseResult.getClassName())
            && newResult.getMethodSignature().equals(baseResult.getMethodSignature())
            && newResult.getDecisionIndex() == baseResult.getDecisionIndex()) {
          extraStmt = false;

          break;
        }
      }

      if (extraStmt) {
        extraStmts.add(newResult);
      }
    }

    return extraStmts;
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
