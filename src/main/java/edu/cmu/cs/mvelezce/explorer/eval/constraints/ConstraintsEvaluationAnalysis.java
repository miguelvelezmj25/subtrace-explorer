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

class ConstraintsEvaluationAnalysis {

  private static final String LINE_SEPARATOR = System.getProperty("line.separator");

  private final String programName;
  private final Set<String> options;

  ConstraintsEvaluationAnalysis(String programName, Set<String> options) {
    this.programName = programName;
    this.options = options;
  }

  void analyze(Set<FeatureExpr> idtaInteractions, Set<FeatureExpr> subtracesInteractions)
      throws IOException {
    StringBuilder results = new StringBuilder();
    Set<FeatureExpr> foundIDTAInteractions =
        findCoveredIDTAInteractions(idtaInteractions, subtracesInteractions, results);
    idtaInteractions.removeAll(foundIDTAInteractions);
    subtracesInteractions.removeAll(foundIDTAInteractions);
    results.append(LINE_SEPARATOR);
    System.out.println();

    Set<FeatureExpr> foundImplyingIDTAInteractions =
        findImplyingIDTAInteractions(idtaInteractions, subtracesInteractions, results);
    idtaInteractions.removeAll(foundImplyingIDTAInteractions);
    removeAllImplyingIDTAInteractions(subtracesInteractions, foundImplyingIDTAInteractions);

    printExtraIDTAInteractions(idtaInteractions, results);
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

  private void printExtraIDTAInteractions(
      Set<FeatureExpr> idtaInteractions, StringBuilder results) {
    if (idtaInteractions.isEmpty()) {
      return;
    }

    String result = "Extra constraints derived from IDTA";
    results.append(result);
    results.append(LINE_SEPARATOR);
    System.out.println(result);

    for (FeatureExpr idtaInteraction : idtaInteractions) {
      result = "\t" + this.prettyPrintFeatureExpr(idtaInteraction);
      results.append(result);
      results.append(LINE_SEPARATOR);
      System.out.println(result);
    }

    results.append(LINE_SEPARATOR);
    System.out.println();
  }

  private void removeAllImplyingIDTAInteractions(
      Set<FeatureExpr> subtracesInteractions, Set<FeatureExpr> foundImplyingIDTAInteractions) {
    Set<FeatureExpr> subtracesInteractionsToRemove = new HashSet<>();

    for (FeatureExpr subtracesInteraction : subtracesInteractions) {
      boolean removeSubtrace = false;

      for (FeatureExpr idtaInteraction : foundImplyingIDTAInteractions) {
        if (idtaInteraction.implies(subtracesInteraction).isTautology()) {
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

  private Set<FeatureExpr> findCoveredIDTAInteractions(
      Set<FeatureExpr> idtaInteractions,
      Set<FeatureExpr> subtracesInteractions,
      StringBuilder results) {
    Set<FeatureExpr> foundIDTAInteractions = new HashSet<>();

    for (FeatureExpr idtaInteraction : idtaInteractions) {
      if (!subtracesInteractions.contains(idtaInteraction)) {
        continue;
      }

      String result = "Constraints found by IDTA " + this.prettyPrintFeatureExpr(idtaInteraction);
      results.append(result);
      results.append(LINE_SEPARATOR);
      System.out.println(result);
      foundIDTAInteractions.add(idtaInteraction);
    }

    return foundIDTAInteractions;
  }

  private Set<FeatureExpr> findImplyingIDTAInteractions(
      Set<FeatureExpr> idtaInteractions,
      Set<FeatureExpr> subtracesInteractions,
      StringBuilder results) {
    Set<FeatureExpr> foundImplyingIDTAInteractions = new HashSet<>();

    for (FeatureExpr subtracesInteraction : subtracesInteractions) {
      Set<FeatureExpr> implyingIDTAInteractions = new HashSet<>();

      for (FeatureExpr idtaInteraction : idtaInteractions) {
        if (!idtaInteraction.implies(subtracesInteraction).isTautology()) {
          continue;
        }

        implyingIDTAInteractions.add(idtaInteraction);
      }

      if (implyingIDTAInteractions.isEmpty()) {
        continue;
      }

      String result =
          "Subtrace constraint "
              + this.prettyPrintFeatureExpr(subtracesInteraction)
              + " implied by ";
      results.append(result);
      results.append(LINE_SEPARATOR);
      System.out.println(result);

      foundImplyingIDTAInteractions.addAll(implyingIDTAInteractions);

      for (FeatureExpr idtaInteraction : implyingIDTAInteractions) {
        result = "\t" + this.prettyPrintFeatureExpr(idtaInteraction);
        results.append(result);
        results.append(LINE_SEPARATOR);
        System.out.println(result);
      }

      results.append("\n");
      System.out.println();
    }

    return foundImplyingIDTAInteractions;
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
