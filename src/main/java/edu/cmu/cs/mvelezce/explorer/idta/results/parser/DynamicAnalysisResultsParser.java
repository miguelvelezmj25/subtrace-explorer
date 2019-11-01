package edu.cmu.cs.mvelezce.explorer.idta.results.parser;

import edu.cmu.cs.mvelezce.cc.DecisionTaints;
import edu.cmu.cs.mvelezce.utils.execute.Executor;
import edu.columbia.cs.psl.phosphor.runtime.Taint;
import org.apache.commons.io.FileUtils;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DynamicAnalysisResultsParser {

  private static final String PHOSPHOR_OUTPUT_DIR =
      Executor.USER_HOME
          + "/Documents/Programming/Java/Projects/phosphor/Phosphor/examples/implicit-optimized";

  private final String programName;

  public DynamicAnalysisResultsParser(String programName) {
    this.programName = programName;
  }

  public Set<DecisionTaints> parseResults() throws IOException {
    String dir = PHOSPHOR_OUTPUT_DIR + "/" + programName;
    Collection<File> serializedFiles = this.getSerializedFiles(dir);

    if (serializedFiles.size() != 1) {
      throw new RuntimeException("The directory " + dir + " must have 1 file.");
    }

    return this.readResults(serializedFiles.iterator().next());
  }

  private Collection<File> getSerializedFiles(String dir) {
    File dirFile = new File(dir);

    return FileUtils.listFiles(dirFile, null, false);
  }

  private Set<DecisionTaints> readResults(File serializedFile) throws IOException {
    return this.deserialize(serializedFile);
  }

  private Set<DecisionTaints> deserialize(File file) throws IOException {
    Set<DecisionTaints> results = new HashSet<>();

    BufferedReader reader = new BufferedReader(new FileReader(file));
    String line;
    while ((line = reader.readLine()) != null) {
      Taint contextTaints = this.getTaints(reader.readLine());
      Taint conditionTaints = this.getTaints(reader.readLine());

      DecisionTaints decisionTaints = new DecisionTaints(line, contextTaints, conditionTaints);
      results.add(decisionTaints);
    }

    reader.close();

    return results;
  }

  @Nullable
  private Taint getTaints(String data) {
    if (data.isEmpty()) {
      return null;
    }

    int[] tags = this.getTags(data);
    Taint taint = new Taint();

    for (int tag : tags) {
      Taint tmp = new Taint(tag);
      taint.addDependency(tmp);
    }

    return taint;
  }

  private int[] getTags(String data) {
    String[] stringTags = data.split(",");

    if (stringTags.length == 0) {
      throw new RuntimeException("The string tags array cannot be empty when parsing as a string");
    }

    int[] tags = new int[stringTags.length];

    for (int i = 0; i < tags.length; i++) {
      tags[i] = Integer.parseInt(stringTags[i]);
    }

    return tags;
  }
}
