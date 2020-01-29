package edu.cmu.cs.mvelezce.explorer.idta.results.partitions;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.analysis.dynamic.BaseDynamicAnalysis;
import edu.cmu.cs.mvelezce.explorer.idta.IDTA;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partitioning;
import edu.cmu.cs.mvelezce.explorer.utils.ConstraintUtils;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class IDTAPartitionsAnalysis extends BaseDynamicAnalysis<Set<Partition>> {

  private final Set<Partition> partitions = new HashSet<>();
  private final String workloadSize;

  public IDTAPartitionsAnalysis(
      String programName, String workloadSize, Collection<String> options) {
    super(programName, new HashSet<>(options), new HashSet<>());

    this.workloadSize = workloadSize;
  }

  @Override
  public Set<Partition> analyze() {
    return this.partitions;
  }

  public void savePartitions(Collection<Partitioning> partitionings) {
    for (Partitioning partitioning : partitionings) {
      this.partitions.addAll(partitioning.getPartitions());
    }
  }

  @Override
  public void writeToFile(Set<Partition> partitions) throws IOException {
    String outputFile = this.outputDir() + "/" + this.getProgramName() + Options.DOT_JSON;
    File file = new File(outputFile);
    file.getParentFile().mkdirs();

    Set<String> prettyPartitions = new HashSet<>();

    for (Partition partition : partitions) {
      String s =
          ConstraintUtils.prettyPrintFeatureExpr(partition.getFeatureExpr(), this.getOptions());
      prettyPartitions.add(s);
    }

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(file, prettyPartitions);
  }

  @Override
  public Set<Partition> readFromFile(File file) throws IOException {
    throw new UnsupportedOperationException("implement");
    //    ObjectMapper mapper = new ObjectMapper();
    //
    //    return mapper.readValue(file, new TypeReference<Set<Partition>>() {});
  }

  @Override
  public String outputDir() {
    return IDTA.OUTPUT_DIR
        + "/analysis/"
        + this.getProgramName()
        + "/cc/"
        + this.workloadSize
        + "/allPartitions";
  }
}
