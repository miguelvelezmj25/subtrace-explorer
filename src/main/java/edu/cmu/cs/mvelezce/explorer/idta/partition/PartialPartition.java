package edu.cmu.cs.mvelezce.explorer.idta.partition;

import java.util.Set;

/** Does not cover the entire configuration space */
public class PartialPartition extends Partitioning {

  public PartialPartition(Set<Partition> partitions) {
    super(partitions);
  }

  public PartialPartition() {
    super();
  }

  @Override
  public TotalPartition merge(Partitioning partitioning) {
    throw new UnsupportedOperationException("implement");
  }

  @Override
  public boolean canMerge() {
    throw new UnsupportedOperationException("implement");
  }
}