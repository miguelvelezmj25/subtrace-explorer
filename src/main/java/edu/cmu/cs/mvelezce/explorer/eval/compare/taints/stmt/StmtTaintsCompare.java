package edu.cmu.cs.mvelezce.explorer.eval.compare.taints.stmt;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.explorer.eval.compare.AbstractCompare;
import edu.cmu.cs.mvelezce.explorer.eval.compare.partitions.all.AllPartitionsCompare;
import edu.cmu.cs.mvelezce.explorer.eval.stmt.ControlFlowStmt;
import edu.cmu.cs.mvelezce.explorer.idta.results.statement.info.ControlFlowStmtTaints;
import edu.cmu.cs.mvelezce.explorer.idta.taint.InfluencingTaints;
import edu.cmu.cs.mvelezce.utils.config.Options;
import org.apache.commons.io.FileUtils;
import org.apache.commons.math3.util.Pair;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class StmtTaintsCompare extends AbstractCompare<ControlFlowStmtTaints> {

  private static final String OUTPUT_DIR = Options.DIRECTORY + "/eval/java/programs/taints/stmt";

  public StmtTaintsCompare(String programName) {
    super(programName);
  }

  public StmtTaintsCompare(String programName, Set<Set<String>> configs) {
    super(programName, configs);
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

  //  @Override
  //  public void compare(Set<ControlFlowStmtTaints> baseResults, Set<ControlFlowStmtTaints>
  // newResults)
  //      throws IOException {
  //    Set<ControlFlowStmtTaints> extraStmts =
  //        (Set<ControlFlowStmtTaints>) AbstractCompare.getExtraStmts(baseResults, newResults);
  //    this.saveExtraStmts(extraStmts);
  //    this.saveDiffStmts(baseResults, newResults, extraStmts);
  //  }

  @Override
  public void compare(Set<ControlFlowStmtTaints> baseResults, Set<ControlFlowStmtTaints> newResults)
      throws IOException {
    for (Set<String> config : this.getConfigs()) {
      File outputFile = new File(OUTPUT_DIR + "/" + this.getProgramName() + "/" + config);

      if (outputFile.exists()) {
        FileUtils.forceDelete(outputFile);
      }

      Set<ControlFlowStmtTaints> baseStmtTaintsForConfig = this.getStmtTaints(config, baseResults);
      Set<ControlFlowStmtTaints> newStmtTaintsForConfig = this.getStmtTaints(config, newResults);

      Set<ControlFlowStmt> controlFlowStmts =
          this.getUniqueControlFlowStmts(baseStmtTaintsForConfig);
      controlFlowStmts.addAll(this.getUniqueControlFlowStmts(newStmtTaintsForConfig));
      Map<ControlFlowStmt, ControlFlowStmtTaints> baseStmtsToTaints =
          this.mapStmtsToTaints(baseStmtTaintsForConfig);
      Map<ControlFlowStmt, ControlFlowStmtTaints> newStmtsToTaints =
          this.mapStmtsToTaints(newStmtTaintsForConfig);

      for (ControlFlowStmt controlFlowStmt : controlFlowStmts) {
        this.compareTaints(config, controlFlowStmt, baseStmtsToTaints, newStmtsToTaints);
      }
    }
  }

  private void compareTaints(
      Set<String> config,
      ControlFlowStmt controlFlowStmt,
      Map<ControlFlowStmt, ControlFlowStmtTaints> baseStmtsToTaints,
      Map<ControlFlowStmt, ControlFlowStmtTaints> newStmtsToTaints)
      throws IOException {

    Set<InfluencingTaints> baseTaints =
        this.getInfluencingTaints(baseStmtsToTaints, controlFlowStmt);
    Set<InfluencingTaints> newTaints = this.getInfluencingTaints(newStmtsToTaints, controlFlowStmt);

    StringBuilder data = new StringBuilder();
    data.append(",Control,Data");
    data.append("\n");

    Set<InfluencingTaints> taints = new HashSet<>(baseTaints);
    taints.addAll(new HashSet<>(newTaints));

    Set<Pair<Set<String>, Set<String>>> equalTaints =
        this.getEqualTaints(baseTaints, newTaints, taints);
    //    data.append("\n");
    //    data.append(this.getDiffTaints(baseTaints, newTaints, taints, "Extra new taints"));
    //    data.append("\n");
    //    data.append(this.getDiffTaints(newTaints, baseTaints, taints, "Extra base taints"));
    //    data.append("\n");

    File outputFile = new File(OUTPUT_DIR + "/" + this.getProgramName() + ".json");

    if (outputFile.exists()) {
      FileUtils.forceDelete(outputFile);
    }

    ObjectMapper mapper = new ObjectMapper();
    //    mapper.writeValue(outputFile, );
  }

  private Set<InfluencingTaints> getInfluencingTaints(
      Map<ControlFlowStmt, ControlFlowStmtTaints> stmtsToTaints, ControlFlowStmt controlFlowStmt) {
    ControlFlowStmtTaints stmtTaints = stmtsToTaints.get(controlFlowStmt);

    if (stmtTaints == null) {
      return new HashSet<>();
    }

    return stmtTaints.getInfo();
  }

  private String getDiffTaints(
      Set<InfluencingTaints> baseTaints,
      Set<InfluencingTaints> newTaints,
      Set<InfluencingTaints> taints,
      String header) {
    StringBuilder data = new StringBuilder();
    data.append(header);
    data.append(",,");
    data.append("\n");

    for (InfluencingTaints taint : taints) {
      if (baseTaints.contains(taint) && newTaints.contains(taint)) {
        continue;
      }

      if (!newTaints.contains(taint)) {
        continue;
      }

      data.append(",");
      data.append('"');
      data.append(taint.getControlTaints());
      data.append('"');
      data.append(",");
      data.append('"');
      data.append(taint.getDataTaints());
      data.append('"');
      data.append("\n");
    }

    return data.toString();
  }

  private Set<Pair<Set<String>, Set<String>>> getEqualTaints(
      Set<InfluencingTaints> baseTaints,
      Set<InfluencingTaints> newTaints,
      Set<InfluencingTaints> taints) {
    Set<Pair<Set<String>, Set<String>>> equalTaints = new HashSet<>();

    for (InfluencingTaints taint : taints) {
      if (!baseTaints.contains(taint) || !newTaints.contains(taint)) {
        continue;
      }

      Pair<Set<String>, Set<String>> x =
          Pair.create(taint.getControlTaints(), taint.getDataTaints());
      equalTaints.add(x);
    }

    return equalTaints;
  }

  private Map<ControlFlowStmt, ControlFlowStmtTaints> mapStmtsToTaints(
      Set<ControlFlowStmtTaints> stmtTaints) {
    Map<ControlFlowStmt, ControlFlowStmtTaints> map = new HashMap<>();

    for (ControlFlowStmtTaints stmtTaint : stmtTaints) {
      ControlFlowStmt stmt =
          new ControlFlowStmt(
              stmtTaint.getPackageName(),
              stmtTaint.getClassName(),
              stmtTaint.getMethodSignature(),
              stmtTaint.getDecisionIndex());
      map.put(stmt, stmtTaint);
    }

    return map;
  }

  private Set<ControlFlowStmt> getUniqueControlFlowStmts(Set<ControlFlowStmtTaints> stmtTaints) {
    Set<ControlFlowStmt> stmts = new HashSet<>();

    for (ControlFlowStmtTaints stmtTaint : stmtTaints) {
      ControlFlowStmt stmt =
          new ControlFlowStmt(
              stmtTaint.getPackageName(),
              stmtTaint.getClassName(),
              stmtTaint.getMethodSignature(),
              stmtTaint.getDecisionIndex());
      stmts.add(stmt);
    }

    return stmts;
  }

  private Set<ControlFlowStmtTaints> getStmtTaints(
      Set<String> config, Set<ControlFlowStmtTaints> results) {
    Set<ControlFlowStmtTaints> stmtTaintsForConfig = new HashSet<>();

    for (ControlFlowStmtTaints stmtTaints : results) {
      Set<InfluencingTaints> influencingTaintsForConfig = new HashSet<>();
      ControlFlowStmtTaints stmtTaintForConfig =
          new ControlFlowStmtTaints(
              stmtTaints.getPackageName(),
              stmtTaints.getClassName(),
              stmtTaints.getMethodSignature(),
              stmtTaints.getDecisionIndex(),
              influencingTaintsForConfig);
      boolean hasTaintsInConfig = false;

      for (InfluencingTaints influencingTaint : stmtTaints.getInfo()) {
        if (!influencingTaint.getConfig().equals(config)) {
          continue;
        }

        hasTaintsInConfig = true;
        stmtTaintForConfig.getInfo().add(influencingTaint);
      }

      if (hasTaintsInConfig) {
        stmtTaintsForConfig.add(stmtTaintForConfig);
      }
    }

    return stmtTaintsForConfig;
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
