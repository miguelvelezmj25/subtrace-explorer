package edu.cmu.cs.mvelezce.explorer.eval.constraints;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.utils.Options;
import org.apache.commons.io.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ConstraintsEvaluationAnalysis {

  private static final String LINE_SEPARATOR = System.getProperty("line.separator");

  private final String programName;
  private final Set<String> options;

  ConstraintsEvaluationAnalysis(String programName, Set<String> options) {
    this.programName = programName;
    this.options = options;
  }

  void analyze(Set<FeatureExpr> phosphorInteractions, Set<FeatureExpr> subtracesInteractions)
      throws IOException {
    StringBuilder results = new StringBuilder();
    Set<FeatureExpr> foundPhosphorInteractions =
        findCoveredPhosphorInteractions(phosphorInteractions, subtracesInteractions, results);
    phosphorInteractions.removeAll(foundPhosphorInteractions);
    subtracesInteractions.removeAll(foundPhosphorInteractions);
    results.append(LINE_SEPARATOR);
    System.out.println();

    Set<FeatureExpr> foundImplyingPhosphorInteractions =
        findImplyingPhosphorInteractions(phosphorInteractions, subtracesInteractions, results);
    phosphorInteractions.removeAll(foundImplyingPhosphorInteractions);
    removeAllImplyingPhosphorInteractions(subtracesInteractions, foundImplyingPhosphorInteractions);

    printExtraPhosphorInteractions(phosphorInteractions, results);
    printMissingSubtraceInteractions(subtracesInteractions, results);

    this.writeToFile(results);
  }

  private void writeToFile(StringBuilder results) throws IOException {
    String outputFile = this.getOutputDir() + "/" + this.programName + ".txt";
    File file = new File(outputFile);

    if (file.exists()) {
      FileUtils.forceDelete(file);
    }

    file.getParentFile().mkdirs();

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
      writer.append(results);
    }
  }

  private void printMissingSubtraceInteractions(
      Set<FeatureExpr> subtracesInteractions, StringBuilder results) {
    if (subtracesInteractions.isEmpty()) {
      return;
    }

    String result = "Missing subtrace constraints";
    results.append(result);
    results.append(LINE_SEPARATOR);
    System.err.println(result);

    for (FeatureExpr subtraceInteraction : subtracesInteractions) {
      result = "\t" + this.prettyPrintFeatureExpr(subtraceInteraction);
      results.append(result);
      results.append(LINE_SEPARATOR);
      System.err.println(result);
    }
  }

  private void printExtraPhosphorInteractions(
      Set<FeatureExpr> phosphorInteractions, StringBuilder results) {
    if (phosphorInteractions.isEmpty()) {
      return;
    }

    String result = "Extra constraints derived from Phosphor";
    results.append(result);
    results.append(LINE_SEPARATOR);
    System.out.println(result);

    for (FeatureExpr phosphorInteraction : phosphorInteractions) {
      result = "\t" + this.prettyPrintFeatureExpr(phosphorInteraction);
      results.append(result);
      results.append(LINE_SEPARATOR);
      System.out.println(result);
    }

    results.append(LINE_SEPARATOR);
    System.out.println();
  }

  private void removeAllImplyingPhosphorInteractions(
      Set<FeatureExpr> subtracesInteractions, Set<FeatureExpr> foundImplyingPhosphorInteractions) {
    Set<FeatureExpr> subtracesInteractionsToRemove = new HashSet<>();

    for (FeatureExpr subtracesInteraction : subtracesInteractions) {
      boolean removeSubtrace = false;

      for (FeatureExpr phosphorInteraction : foundImplyingPhosphorInteractions) {
        if (phosphorInteraction.implies(subtracesInteraction).isTautology()) {
          removeSubtrace = true;
          break;
        }
      }

      if (removeSubtrace) {
        subtracesInteractionsToRemove.add(subtracesInteraction);
      }
    }

    subtracesInteractions.removeAll(subtracesInteractionsToRemove);
  }

  private Set<FeatureExpr> findCoveredPhosphorInteractions(
      Set<FeatureExpr> phosphorInteractions,
      Set<FeatureExpr> subtracesInteractions,
      StringBuilder results) {
    Set<FeatureExpr> foundPhosphorInteractions = new HashSet<>();

    for (FeatureExpr phosphorInteraction : phosphorInteractions) {
      if (!subtracesInteractions.contains(phosphorInteraction)) {
        continue;
      }

      String result =
          "Constraints found by Phosphor " + this.prettyPrintFeatureExpr(phosphorInteraction);
      results.append(result);
      results.append(LINE_SEPARATOR);
      System.out.println(result);
      foundPhosphorInteractions.add(phosphorInteraction);
    }

    return foundPhosphorInteractions;
  }

  private Set<FeatureExpr> findImplyingPhosphorInteractions(
      Set<FeatureExpr> phosphorInteractions,
      Set<FeatureExpr> subtracesInteractions,
      StringBuilder results) {
    Set<FeatureExpr> foundImplyingPhosphorInteractions = new HashSet<>();

    for (FeatureExpr subtracesInteraction : subtracesInteractions) {
      Set<FeatureExpr> implyingPhosphorInteractions = new HashSet<>();

      for (FeatureExpr phosphorInteraction : phosphorInteractions) {
        if (!phosphorInteraction.implies(subtracesInteraction).isTautology()) {
          continue;
        }

        implyingPhosphorInteractions.add(phosphorInteraction);
      }

      if (implyingPhosphorInteractions.isEmpty()) {
        continue;
      }

      String result =
          "Subtrace constraint "
              + this.prettyPrintFeatureExpr(subtracesInteraction)
              + " implied by ";
      results.append(result);
      results.append(LINE_SEPARATOR);
      System.out.println(result);

      foundImplyingPhosphorInteractions.addAll(implyingPhosphorInteractions);

      for (FeatureExpr phosphorInteraction : implyingPhosphorInteractions) {
        result = "\t" + this.prettyPrintFeatureExpr(phosphorInteraction);
        results.append(result);
        results.append(LINE_SEPARATOR);
        System.out.println(result);
      }

      results.append("\n");
      System.out.println();
    }

    return foundImplyingPhosphorInteractions;
  }

  private String getOutputDir() {
    return Options.DIRECTORY
        + "/evaluation/idta/constraints/java/programs/results/"
        + this.programName;
  }

  private String prettyPrintFeatureExpr(FeatureExpr featureExpr) {
    String stringInteraction = featureExpr.toTextExpr().replaceAll("definedEx\\(", "");

    for (String option : this.options) {
      stringInteraction = stringInteraction.replaceAll(option + "\\)", option);
    }

    return stringInteraction;
  }
}
