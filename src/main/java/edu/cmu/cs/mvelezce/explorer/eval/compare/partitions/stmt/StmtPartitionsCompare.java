package edu.cmu.cs.mvelezce.explorer.eval.compare.partitions.stmt;

import edu.cmu.cs.mvelezce.explorer.eval.compare.AbstractCompare;
import edu.cmu.cs.mvelezce.explorer.eval.compare.partitions.all.AllPartitionsCompare;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;
import edu.cmu.cs.mvelezce.explorer.idta.results.statement.info.ControlFlowStmtPartitioning;
import edu.cmu.cs.mvelezce.utils.config.Options;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

public final class StmtPartitionsCompare extends AbstractCompare<ControlFlowStmtPartitioning> {

  private static final String OUTPUT_DIR =
      Options.DIRECTORY + "/eval/java/programs/partitions/stmt";

  StmtPartitionsCompare(String programName) {
    super(programName);
  }

  private String printDiffPartitions(
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

  private String printExtraStmts(Set<ControlFlowStmtPartitioning> extraStmts) {
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

  @Override
  public void compare(
      Set<ControlFlowStmtPartitioning> baseResults, Set<ControlFlowStmtPartitioning> newResults)
      throws IOException {
    System.err.println(
        "CHANGE LOGIC OF CHECKING FOR '|' TO CHECKING IF THE PARTITION IS THE REMAINING ONE");
    Set<ControlFlowStmtPartitioning> extraStmts =
        (Set<ControlFlowStmtPartitioning>) AbstractCompare.getExtraStmts(baseResults, newResults);
    this.saveExtraStmts(extraStmts);
    this.saveDiffStmts(baseResults, newResults, extraStmts);
  }

  private void saveDiffStmts(
      Set<ControlFlowStmtPartitioning> baseResults,
      Set<ControlFlowStmtPartitioning> newResults,
      Set<ControlFlowStmtPartitioning> extraStmts)
      throws IOException {
    String results = this.printDiffPartitions(baseResults, newResults, extraStmts);

    File outputFile =
        new File(
            OUTPUT_DIR + "/compare/diff" + this.getProgramName() + AllPartitionsCompare.DOT_TXT);
    outputFile.getParentFile().mkdirs();

    if (outputFile.exists()) {
      FileUtils.forceDelete(outputFile);
    }

    PrintWriter writer = new PrintWriter(outputFile);
    writer.write(results);
    writer.flush();
    writer.close();
  }

  private void saveExtraStmts(Set<ControlFlowStmtPartitioning> extraStmts) throws IOException {
    String results = this.printExtraStmts(extraStmts);

    File outputFile =
        new File(
            OUTPUT_DIR + "/compare/extras" + this.getProgramName() + AllPartitionsCompare.DOT_TXT);
    outputFile.getParentFile().mkdirs();

    if (outputFile.exists()) {
      FileUtils.forceDelete(outputFile);
    }

    PrintWriter writer = new PrintWriter(outputFile);
    writer.write(results);
    writer.flush();
    writer.close();
  }

  //    public void compareNumberInteractingOptions(
  //        Set<ControlFlowStmtPartitioning> baseResults, Set<ControlFlowStmtPartitioning>
  // newResults)
  //   {
  //      Set<ControlFlowStmt> stmts = this.getControlFlowStmt(baseResults);
  //      stmts.addAll(this.getControlFlowStmt(newResults));
  //      int totalStmts = stmts.size();
  //      System.out.println("# of total stms: " + totalStmts);
  //
  //      Map<ControlFlowStmt, Partitioning> baseStmtsToPartitions =
  //          this.getStmtsToPartitions(baseResults);
  //      int totalBaseStmts = baseStmtsToPartitions.size();
  //      System.out.println(
  //          "# of found stmts in base results: "
  //              + totalBaseStmts
  //              + " -> "
  //              + DECIMAL_FORMAT.format(100.0 * totalBaseStmts / totalStmts)
  //              + "%");
  //
  //      Map<ControlFlowStmt, Partitioning> newStmtsToPartitions =
  //   this.getStmtsToPartitions(newResults);
  //      int totalNewStmts = newStmtsToPartitions.size();
  //      System.out.println(
  //          "# of found stmts in new results (should be 100%): "
  //              + totalNewStmts
  //              + " -> "
  //              + DECIMAL_FORMAT.format(100.0 * totalNewStmts / totalStmts)
  //              + "%");
  //
  //      int equalPartitions = 0;
  //
  //      for (ControlFlowStmt stmt : stmts) {
  //        Partitioning basePartition = baseStmtsToPartitions.get(stmt);
  //        Set<String> baseInteractingOptions = this.getInteractingOptions(basePartition);
  //        Partitioning newPartition = newStmtsToPartitions.get(stmt);
  //        Set<String> newInteractingOptions = this.getInteractingOptions(newPartition);
  //
  //        if (baseInteractingOptions.equals(newInteractingOptions)) {
  //          equalPartitions++;
  //        } else {
  //          throw new RuntimeException("Handle");
  //        }
  //      }
  //
  //      throw new UnsupportedOperationException("implement");
  //    }
  //
  //    private Set<String> getInteractingOptions(Partitioning partitioning) {
  //      Set<String> interactingOptions = new HashSet<>();
  //      Set<Partition> partitions = partitioning.getPartitions();
  //
  //      for (Partition partition : partitions) {
  //        Iterator<SingleFeatureExpr> partitionIter =
  //            partition.getFeatureExpr().collectDistinctFeatureObjects().iterator();
  //
  //        while (partitionIter.hasNext()) {
  //          interactingOptions.add(partitionIter.next().feature());
  //        }
  //      }
  //
  //      return interactingOptions;
  //    }
  //
  //    private Map<ControlFlowStmt, Partitioning> getStmtsToPartitions(
  //        Set<ControlFlowStmtPartitioning> results) {
  //      Map<ControlFlowStmt, Partitioning> stmtsToPartitions = new HashMap<>();
  //
  //      for (ControlFlowStmtPartitioning result : results) {
  //        ControlFlowStmt stmt =
  //            new ControlFlowStmt(
  //                result.getPackageName(),
  //                result.getClassName(),
  //                result.getMethodSignature(),
  //                result.getDecisionIndex());
  //
  //        stmtsToPartitions.put(stmt, result.getInfo());
  //      }
  //
  //      return stmtsToPartitions;
  //    }
  //
  //    private Set<ControlFlowStmt> getControlFlowStmt(Set<ControlFlowStmtPartitioning> results) {
  //      Set<ControlFlowStmt> stmts = new HashSet<>();
  //
  //      for (ControlFlowStmtPartitioning result : results) {
  //        ControlFlowStmt stmt =
  //            new ControlFlowStmt(
  //                result.getPackageName(),
  //                result.getClassName(),
  //                result.getMethodSignature(),
  //                result.getDecisionIndex());
  //        stmts.add(stmt);
  //      }
  //
  //      return stmts;
  //    }
}
