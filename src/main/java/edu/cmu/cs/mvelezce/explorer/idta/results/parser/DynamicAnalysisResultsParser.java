package edu.cmu.cs.mvelezce.explorer.idta.results.parser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mijecu25.meme.utils.execute.Executor;
import edu.cmu.cs.mvelezce.cc.control.sink.ControlStmtField;
import edu.cmu.cs.mvelezce.cc.control.sink.SinkManager;
import edu.cmu.cs.mvelezce.cc.instrumenter.ControlStmt;
import edu.cmu.cs.mvelezce.cc.instrumenter.SinkInstrumenter;
import edu.columbia.cs.psl.phosphor.runtime.Taint;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;

public class DynamicAnalysisResultsParser {

  private static final String PHOSPHOR_OUTPUT_DIR =
      Executor.USER_HOME
          + "/Documents/Programming/Java/Projects/phosphor/Phosphor/examples/control";
  private static final String PHOSPHOR_INSTRUMENT_DIR =
      Executor.USER_HOME
          + "/Documents/Programming/Java/Projects/phosphor/Phosphor/scripts/instrument/control";

  private final Map<Integer, String> fields = new HashMap<>();
  private final Map<Integer, Taint<Integer>> taints = new HashMap<>();

  private final String programName;
  private final Map<String, ControlStmt> fieldsToControlStmts;

  public DynamicAnalysisResultsParser(String programName) {
    this.programName = programName;
    this.fieldsToControlStmts = this.readControlStmtFields();
  }

  private Map<String, ControlStmt> readControlStmtFields() {
    Set<ControlStmtField> instrumentedFields;

    try {
      ObjectMapper mapper = new ObjectMapper();
      File inputFile = new File(PHOSPHOR_INSTRUMENT_DIR + "/" + this.programName + ".json");
      instrumentedFields =
          mapper.readValue(inputFile, new TypeReference<Set<ControlStmtField>>() {});
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }

    Map<String, ControlStmt> fieldsToControlStmts = new HashMap<>();

    for (ControlStmtField entry : instrumentedFields) {
      if (fieldsToControlStmts.containsKey(entry.getField())) {
        throw new RuntimeException("The field id " + entry.getField() + " is duplicated");
      }

      ControlStmt readControlStmt = entry.getControlStmt();
      ControlStmt controlStmt =
          new ControlStmt(
              readControlStmt.getClassName(),
              readControlStmt.getMethodName(),
              readControlStmt.getDesc(),
              readControlStmt.getIndex());
      fieldsToControlStmts.put(entry.getField(), controlStmt);
    }

    return fieldsToControlStmts;
  }

  public Set<DecisionTaints> parseResults() throws IOException {
    String dir = PHOSPHOR_OUTPUT_DIR + "/" + this.programName;
    Collection<File> serializedFiles = this.getSerializedFiles(dir);

    if (serializedFiles.size() != 3) {
      throw new RuntimeException("The directory " + dir + " must have 3 files.");
    }

    for (File file : serializedFiles) {
      if (file.getName().endsWith(SinkManager.DATA_FILE)) {
        continue;
      }

      if (file.getName().endsWith(SinkManager.FIELDS_FILE)) {
        this.readNames(file, this.fields);
      } else if (file.getName().endsWith(SinkManager.TAINTS_FILE)) {
        this.readTaints(file);
      } else {
        throw new RuntimeException("Do not know how to read file " + file);
      }
    }

    for (File file : serializedFiles) {
      if (file.getName().endsWith(SinkManager.DATA_FILE)) {
        return this.readData(file);
      }
    }

    throw new RuntimeException("Could not find the results to parse");
  }

  private void readTaints(File file) throws IOException {
    DataInputStream dis = new DataInputStream(new FileInputStream(file));

    while (dis.available() > 0) {
      int entry = dis.readInt();
      List<Taint<Integer>> labels = new ArrayList<>();

      while (entry != SinkManager.LABELS_END) {
        labels.add(Taint.withLabel(entry));
        dis.read(SinkManager.LABEL_SEP_BYTES);
        entry = dis.readInt();
      }

      Taint<Integer> taint = Taint.emptyTaint();

      for (Taint<Integer> label : labels) {
        taint = Taint.combineTags(taint, label);
      }

      dis.read(SinkManager.NEW_LINE_BYTES);
      int index = dis.readInt();
      dis.read(SinkManager.NEW_LINE_BYTES);

      this.taints.put(index, taint);
    }
  }

  private Set<DecisionTaints> readData(File file) throws IOException {
    Set<DecisionTaints> results = new HashSet<>();
    DataInputStream dis = new DataInputStream(new FileInputStream(file));

    while (dis.available() > 0) {
      String fieldName =
          this.fields.get(dis.readInt()).replaceAll(SinkInstrumenter.STATIC_FIELD_PREFIX_CC, "");
      dis.read(SinkManager.NEW_LINE_BYTES);
      Taint<Integer> control = this.taints.get(dis.readInt());
      dis.read(SinkManager.NEW_LINE_BYTES);
      Taint<Integer> data = this.taints.get(dis.readInt());
      dis.read(SinkManager.NEW_LINE_BYTES);

      ControlStmt controlStmt = this.fieldsToControlStmts.get(fieldName);
      DecisionTaints decisionTaints =
          new DecisionTaints(
              controlStmt.getClassName()
                  + "."
                  + controlStmt.getMethodName()
                  + controlStmt.getDesc()
                  + "."
                  + controlStmt.getIndex(),
              control,
              data);
      results.add(decisionTaints);
    }

    return results;
  }

  private void readNames(File file, Map<Integer, String> mapWithNames) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(file));
    String name;
    while ((name = reader.readLine()) != null) {
      int index = ByteBuffer.wrap(reader.readLine().getBytes()).getInt();
      mapWithNames.put(index, name);
    }

    reader.close();
  }

  private Collection<File> getSerializedFiles(String dir) {
    File dirFile = new File(dir);

    return FileUtils.listFiles(dirFile, null, false);
  }
}
