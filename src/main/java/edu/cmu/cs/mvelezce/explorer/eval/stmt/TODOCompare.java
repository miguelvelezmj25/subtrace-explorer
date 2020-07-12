package edu.cmu.cs.mvelezce.explorer.eval.stmt;

import org.apache.commons.math3.util.Pair;

import java.util.HashSet;
import java.util.Set;

public class TODOCompare {

  private final Set<String> config;
  private final String packageName;
  private final String className;
  private final String methodSignature;
  private final int decisionIndex;
  private final Set<Pair<Set<String>, Set<String>>> equalTaints;
  private final Set<Pair<Set<String>, Set<String>>> missingBaseTaints;
  private final Set<Pair<Set<String>, Set<String>>> missingNewTaints;

  public TODOCompare(
      Set<String> config,
      String packageName,
      String className,
      String methodSignature,
      int decisionIndex,
      Set<Pair<Set<String>, Set<String>>> equalTaints,
      Set<Pair<Set<String>, Set<String>>> missingBaseTaints,
      Set<Pair<Set<String>, Set<String>>> missingNewTaints) {
    this.config = config;
    this.packageName = packageName;
    this.className = className;
    this.methodSignature = methodSignature;
    this.decisionIndex = decisionIndex;
    this.equalTaints = equalTaints;
    this.missingBaseTaints = missingBaseTaints;
    this.missingNewTaints = missingNewTaints;
  }

  private TODOCompare() {
      this.config = new HashSet<>();
      this.packageName = "";
      this.className = "";
      this.methodSignature = "";
      this.decisionIndex = -1;
      this.equalTaints = new HashSet<>();
      this.missingBaseTaints = new HashSet<>();
      this.missingNewTaints = new HashSet<>();
  }


}
