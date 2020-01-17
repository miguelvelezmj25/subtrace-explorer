package edu.cmu.cs.mvelezce.explorer.idta.results.partitions;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.analysis.dynamic.BaseDynamicAnalysis;
import edu.cmu.cs.mvelezce.explorer.eval.constraints.idta.constraint.ConfigConstraint;
import edu.cmu.cs.mvelezce.explorer.idta.IDTA;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partitioning;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class IDTAPartitionsAnalysis extends BaseDynamicAnalysis<Set<Partition>> {

  private final Set<Partition> partitions = new HashSet<>();
  private final String workloadSize;

  public IDTAPartitionsAnalysis(String programName, String workloadSize) {
    super(programName, new HashSet<>(), new HashSet<>());

    this.workloadSize = workloadSize;
  }

  @Override
  public Set<Partition> analyze() throws IOException, InterruptedException {
    System.err.println("Possibly use the feature expr library");
    System.err.println(
        "Do we want to just return the constraints we found in the analysis? Or do some simplification");
    //    return this.getSimplifiedConstraints(this.constraints);
    return this.partitions;
  }

  public void savePartitions(Collection<Partitioning> partitionings) {
    for (Partitioning partitioning : partitionings) {
      this.partitions.addAll(partitioning.getPartitions());
    }
  }

  private Set<ConfigConstraint> getSimplifiedConstraints(Set<ConfigConstraint> constraints) {
    Set<ConfigConstraint> simplifiedConstraints = new HashSet<>();

    for (ConfigConstraint candidateConstraintToAdd : constraints) {
      boolean add = true;

      for (ConfigConstraint constraint : constraints) {
        if (candidateConstraintToAdd.equals(constraint)) {
          continue;
        }

        if (candidateConstraintToAdd.isSubConstraintOf(constraint)) {
          add = false;
          break;
        }
      }

      if (add) {
        simplifiedConstraints.add(candidateConstraintToAdd);
      }
    }

    return simplifiedConstraints;
  }

  @Override
  public void writeToFile(Set<Partition> partitions) throws IOException {
    String outputFile = this.outputDir() + "/" + this.getProgramName() + Options.DOT_JSON;
    File file = new File(outputFile);
    file.getParentFile().mkdirs();

    Set<String> prettyPartitions = new HashSet<>();

    for (Partition partition : partitions) {
      prettyPartitions.add(partition.getPrettyPartition());
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
