package edu.cmu.cs.mvelezce.explorer.eval.constraints.idta;

import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IDTAPartitionsAnalyzer extends ConstraintAnalyzer {

  private final Set<Partition> partitions;

  IDTAPartitionsAnalyzer(String programName, Set<Partition> partitions, Set<String> options) {
    super(programName, options);

    this.partitions = partitions;
  }

  public IDTAPartitionsAnalyzer(String programName) {
    this(programName, new HashSet<>(), new HashSet<>());
  }

  @Override
  protected List<String> getStringConstraints() {
    List<String> stringConstraints = new ArrayList<>();

    for (Partition partition : this.partitions) {
      throw new UnsupportedOperationException("Do we need the pretty partition right now?");
      //      String constraint = partition.getPrettyPartition();
      //      stringConstraints.add(constraint);
    }

    return stringConstraints;
  }

  @Override
  public String outputDir() {
    return super.outputDir() + "/idta/" + this.getProgramName();
  }
}
