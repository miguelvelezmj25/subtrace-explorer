package edu.cmu.cs.mvelezce.explorer.idta.partition;

import java.util.Collection;
import java.util.Set;

/** Does not cover the entire configuration space */
public class PartialPartition extends Partitioning {

  public PartialPartition(Collection<String> options, Set<Partition> partitions) {
    super(options, partitions);
  }

  public PartialPartition(Collection<String> options) {
    super(options);
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
