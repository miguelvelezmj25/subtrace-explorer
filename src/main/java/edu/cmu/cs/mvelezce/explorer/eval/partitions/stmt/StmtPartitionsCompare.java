package edu.cmu.cs.mvelezce.explorer.eval.partitions.stmt;

import edu.cmu.cs.mvelezce.explorer.eval.partitions.all.AllPartitionsCompare;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;
import edu.cmu.cs.mvelezce.explorer.idta.results.statement.info.ControlFlowStmtPartitioning;
import edu.cmu.cs.mvelezce.utils.config.Options;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

public final class StmtPartitionsCompare {

  private static final String OUTPUT_DIR =
      Options.DIRECTORY + "/eval/java/programs/partitions/stmt";
  private final String programName;

  StmtPartitionsCompare(String programName) {
    this.programName = programName;
  }

  private static String printDiffPartitions(
      Set<ControlFlowStmtPartitioning> baseResults,
      Set<ControlFlowStmtPartitioning> newResults,
      Set<ControlFlowStmtPartitioning> extraStmts) {
    StringBuilder result = new StringBuilder();

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

        String comparePartitionsResult =
            AllPartitionsCompare.comparePartitions(
                baseResult.getInfo().getPartitions(), newResult.getInfo().getPartitions());

        if (comparePartitionsResult.isEmpty()) {
          continue;
        }

        result.append(newResult.getPackageName());
        result.append(".");
        result.append(newResult.getClassName());
        result.append(".");
        result.append(newResult.getMethodSignature());
        result.append(".");
        result.append(newResult.getDecisionIndex());
        result.append("\n");
        result.append(comparePartitionsResult);
        result.append("\n");
      }
    }

    return result.toString();
  }

  private static String printExtraStmts(Set<ControlFlowStmtPartitioning> extraStmts) {
    StringBuilder result = new StringBuilder();

    for (ControlFlowStmtPartitioning extraStmt : extraStmts) {
      result.append(extraStmt.getPackageName());
      result.append(".");
      result.append(extraStmt.getClassName());
      result.append(".");
      result.append(extraStmt.getMethodSignature());
      result.append(".");
      result.append(extraStmt.getDecisionIndex());
      result.append("\n");

      for (Partition partition : extraStmt.getInfo().getPartitions()) {
        result.append("\t");
        result.append(partition);
        result.append("\n");
      }

      result.append("\n");
    }

    return result.toString();
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

  public void compare(
      Set<ControlFlowStmtPartitioning> baseResults, Set<ControlFlowStmtPartitioning> newResults)
      throws IOException {
    System.err.println(
        "CHANGE LOGIC OF CHECKING FOR '|' TO CHECKING IF THE PARTITION IS THE REMAINING ONE");
    Set<ControlFlowStmtPartitioning> extraStmts = getExtraStmts(baseResults, newResults);
    String results = printExtraStmts(extraStmts);

    File outputFile =
        new File(OUTPUT_DIR + "/compare/extras" + this.programName + AllPartitionsCompare.DOT_TXT);
    outputFile.getParentFile().mkdirs();

    if (outputFile.exists()) {
      FileUtils.forceDelete(outputFile);
    }

    PrintWriter writer = new PrintWriter(outputFile);
    writer.write(results);
    writer.flush();
    writer.close();

    results = printDiffPartitions(baseResults, newResults, extraStmts);

    outputFile =
        new File(OUTPUT_DIR + "/compare/diff" + this.programName + AllPartitionsCompare.DOT_TXT);
    outputFile.getParentFile().mkdirs();

    if (outputFile.exists()) {
      FileUtils.forceDelete(outputFile);
    }

    writer = new PrintWriter(outputFile);
    writer.write(results);
    writer.flush();
    writer.close();
  }
}
