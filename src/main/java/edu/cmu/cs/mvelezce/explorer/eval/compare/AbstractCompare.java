package edu.cmu.cs.mvelezce.explorer.eval.compare;

import edu.cmu.cs.mvelezce.explorer.idta.results.statement.info.ControlFlowStmtInfo;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractCompare<T> {

  public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.###");

  private final String programName;
  private final Set<Set<String>> configs;

  public AbstractCompare(String programName) {
    this.programName = programName;
    this.configs = new HashSet<>();
  }

  public AbstractCompare(String programName, Set<Set<String>> configs) {
    this.programName = programName;
    this.configs = configs;
  }

  public static Set<? extends ControlFlowStmtInfo> getExtraStmts(
      Set<? extends ControlFlowStmtInfo> baseResults,
      Set<? extends ControlFlowStmtInfo> newResults) {
    Set<ControlFlowStmtInfo> extraStmts = new HashSet<>();

    for (ControlFlowStmtInfo newResult : newResults) {
      boolean extraStmt = true;

      for (ControlFlowStmtInfo baseResult : baseResults) {
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

  public abstract void compare(Set<T> baseResults, Set<T> newResults) throws IOException;

  public String getProgramName() {
    return programName;
  }

  public Set<Set<String>> getConfigs() {
    return configs;
  }
}
