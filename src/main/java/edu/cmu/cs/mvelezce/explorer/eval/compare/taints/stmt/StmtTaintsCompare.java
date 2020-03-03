package edu.cmu.cs.mvelezce.explorer.eval.compare.taints.stmt;

import edu.cmu.cs.mvelezce.explorer.eval.compare.AbstractCompare;
import edu.cmu.cs.mvelezce.explorer.eval.compare.partitions.all.AllPartitionsCompare;
import edu.cmu.cs.mvelezce.explorer.idta.results.statement.info.ControlFlowStmtTaints;
import edu.cmu.cs.mvelezce.explorer.idta.taint.InfluencingTaints;
import edu.cmu.cs.mvelezce.utils.config.Options;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

public final class StmtTaintsCompare extends AbstractCompare<ControlFlowStmtTaints> {

  private static final String OUTPUT_DIR = Options.DIRECTORY + "/eval/java/programs/taints/stmt";

  public StmtTaintsCompare(String programName) {
    super(programName);
  }

  //  public static void compare(
  //      Set<ControlFlowStmtTaints> baseResults,
  //      Set<ControlFlowStmtTaints> newResults,
  //      String packageName,
  //      String className,
  //      String methodSignature,
  //      int decisionIndex) {
  //    Set<InfluencingTaints> baseInfluencingTaints =
  //        getInfluencingTaints(baseResults, packageName, className, methodSignature,
  // decisionIndex);
  //    Set<InfluencingTaints> newInfluencingTaints =
  //        getInfluencingTaints(newResults, packageName, className, methodSignature,
  // decisionIndex);
  //
  //    Set<Set<String>> configs = getConfigs(baseInfluencingTaints);
  //    configs.addAll(getConfigs(newInfluencingTaints));
  //  }

  //  private static Set<Set<String>> getConfigs(Set<InfluencingTaints> influencingTaints) {
  //    Set<Set<String>> configs = new HashSet<>();
  //
  //    for (InfluencingTaints influencingTaint : influencingTaints) {
  //      configs.add(influencingTaint.getConfig());
  //    }
  //
  //    return configs;
  //  }
  //
  //  private static Set<InfluencingTaints> getInfluencingTaints(
  //      Set<ControlFlowStmtTaints> results,
  //      String packageName,
  //      String className,
  //      String methodSignature,
  //      int decisionIndex) {
  //    for (ControlFlowStmtTaints result : results) {
  //      if (result.getPackageName().equals(packageName)
  //          && result.getClassName().equals(className)
  //          && result.getMethodSignature().equals(methodSignature)
  //          && result.getDecisionIndex() != decisionIndex) {
  //        return result.getInfo();
  //      }
  //    }
  //
  //    throw new RuntimeException(
  //        "Could not find stmt "
  //            + packageName
  //            + "."
  //            + className
  //            + "."
  //            + methodSignature
  //            + ", "
  //            + decisionIndex);
  //  }

  private String printExtraStmts(Set<ControlFlowStmtTaints> extraStmts) {
    StringBuilder result = new StringBuilder();

    for (ControlFlowStmtTaints extraStmt : extraStmts) {
      result.append(extraStmt.getPackageName());
      result.append(".");
      result.append(extraStmt.getClassName());
      result.append(".");
      result.append(extraStmt.getMethodSignature());
      result.append(".");
      result.append(extraStmt.getDecisionIndex());
      result.append("\n");

      for (InfluencingTaints influencingTaints : extraStmt.getInfo()) {
        result.append("\t");
        result.append("Config: ");
        result.append(influencingTaints.getConfig());
        result.append("\n");
        result.append("\t");
        result.append("Control taints: ");
        result.append(influencingTaints.getControlTaints());
        result.append("\n");
        result.append("\t");
        result.append("Data taints: ");
        result.append(influencingTaints.getDataTaints());
        result.append("\n");
        result.append("\n");
      }

      result.append("\n");
    }

    return result.toString();
  }

  @Override
  public void compare(Set<ControlFlowStmtTaints> baseResults, Set<ControlFlowStmtTaints> newResults)
      throws IOException {
    Set<ControlFlowStmtTaints> extraStmts =
        (Set<ControlFlowStmtTaints>) AbstractCompare.getExtraStmts(baseResults, newResults);
    this.saveExtraStmts(extraStmts);
    this.saveDiffStmts(baseResults, newResults, extraStmts);
  }

  private void saveDiffStmts(
      Set<ControlFlowStmtTaints> baseResults,
      Set<ControlFlowStmtTaints> newResults,
      Set<ControlFlowStmtTaints> extraStmts)
      throws IOException {
    throw new UnsupportedOperationException("Compare the taints per executed config");
    //    String results = this.printDiffPartitions(baseResults, newResults, extraStmts);
    //
    //    File outputFile =
    //        new File(
    //            OUTPUT_DIR + "/compare/diff" + this.getProgramName() +
    // AllPartitionsCompare.DOT_TXT);
    //    outputFile.getParentFile().mkdirs();
    //
    //    if (outputFile.exists()) {
    //      FileUtils.forceDelete(outputFile);
    //    }
    //
    //    PrintWriter writer = new PrintWriter(outputFile);
    //    writer.write(results);
    //    writer.flush();
    //    writer.close();
  }

  //  private String printDiffPartitions(
  //          Set<ControlFlowStmtTaints> baseResults,
  //          Set<ControlFlowStmtTaints> newResults,
  //          Set<ControlFlowStmtTaints> extraStmts) {
  //    StringBuilder result = new StringBuilder();
  //
  //    for (ControlFlowStmtTaints newResult : newResults) {
  //      if (extraStmts.contains(newResult)) {
  //        continue;
  //      }
  //
  //      for (ControlFlowStmtTaints baseResult : baseResults) {
  //        if (!newResult.getPackageName().equals(baseResult.getPackageName())
  //                || !newResult.getClassName().equals(baseResult.getClassName())
  //                || !newResult.getMethodSignature().equals(baseResult.getMethodSignature())
  //                || newResult.getDecisionIndex() != baseResult.getDecisionIndex()) {
  //          continue;
  //        }
  //
  //
  //        String comparePartitionsResult = this.compareInfluencingTaints(baseResult.getInfo(),
  // newResult.getInfo());
  //
  //        if (comparePartitionsResult.isEmpty()) {
  //          continue;
  //        }
  //
  //        result.append(newResult.getPackageName());
  //        result.append(".");
  //        result.append(newResult.getClassName());
  //        result.append(".");
  //        result.append(newResult.getMethodSignature());
  //        result.append(".");
  //        result.append(newResult.getDecisionIndex());
  //        result.append("\n");
  //        result.append(comparePartitionsResult);
  //        result.append("\n");
  //      }
  //    }
  //
  //    return result.toString();
  //  }

  //  private String compareInfluencingTaints(Set<InfluencingTaints> baseResults,
  // Set<InfluencingTaints> newResults) {
  //    if(baseResults.equals(newResults)) {
  //      return "";
  //    }
  //
  //    for(InfluencingTaints newTaints : newResults) {
  //      boolean x = baseResults.contains(newTaints);
  //      System.out.println();
  //    }
  //
  //    StringBuilder results = new StringBuilder();
  //
  //
  //
  //    return results.toString();
  //  }

  private void saveExtraStmts(Set<ControlFlowStmtTaints> extraStmts) throws IOException {
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
}
