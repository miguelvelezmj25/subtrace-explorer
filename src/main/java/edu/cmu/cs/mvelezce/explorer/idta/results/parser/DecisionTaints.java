package edu.cmu.cs.mvelezce.explorer.idta.results.parser;

import edu.columbia.cs.psl.phosphor.runtime.Taint;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Objects;

public class DecisionTaints implements Serializable {

  private static final long serialVersionUID = 1L;

  private final String decision;
  private final Taint controlTaints;
  private final Taint dataTaints;

  public DecisionTaints(
      String decision, @Nullable Taint controlTaints, @Nullable Taint dataTaints) {
    this.decision = decision;
    this.controlTaints = controlTaints;
    this.dataTaints = dataTaints;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    DecisionTaints that = (DecisionTaints) o;

    if (!decision.equals(that.decision)) {
      return false;
    }
    if (!Objects.equals(controlTaints, that.controlTaints)) {
      return false;
    }

    return Objects.equals(dataTaints, that.dataTaints);
  }

  @Override
  public int hashCode() {
    int result = decision.hashCode();
    result = 31 * result + (controlTaints != null ? controlTaints.hashCode() : 0);
    result = 31 * result + (dataTaints != null ? dataTaints.hashCode() : 0);
    return result;
  }

  public String getDecision() {
    return decision;
  }

  @Nullable
  public Taint getControlTaints() {
    return controlTaints;
  }

  @Nullable
  public Taint getDataTaints() {
    return dataTaints;
  }
}
