package edu.cmu.cs.mvelezce.explorer.eval.constraints.subtraces;

import com.google.common.base.Objects;
import de.fosd.typechef.featureexpr.FeatureExpr;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SubtraceOutcomeConstraint {

  private final UUID subtraceLabelUUID;
  private final Map<String, FeatureExpr> outcomesToConstraints = new HashMap<>();

  SubtraceOutcomeConstraint(String subtraceLabelUUID) {
    this.subtraceLabelUUID = UUID.fromString(subtraceLabelUUID);
  }

  public UUID getSubtraceLabelUUID() {
    return subtraceLabelUUID;
  }

  public Map<String, FeatureExpr> getOutcomesToConstraints() {
    return outcomesToConstraints;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SubtraceOutcomeConstraint that = (SubtraceOutcomeConstraint) o;
    return Objects.equal(subtraceLabelUUID, that.subtraceLabelUUID)
        && Objects.equal(outcomesToConstraints, that.outcomesToConstraints);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(subtraceLabelUUID, outcomesToConstraints);
  }
}
