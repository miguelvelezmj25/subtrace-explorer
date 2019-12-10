package edu.cmu.cs.mvelezce.explorer.idta.results.parser;

import edu.columbia.cs.psl.phosphor.runtime.Taint;

import java.io.Serializable;
import java.util.Objects;

public class DecisionTaints<T> implements Serializable {

  private static final long serialVersionUID = 1L;

  private final String decision;
  private final Taint<T> execCtxTaints;
  private final Taint<T> conditionTaints;

  public DecisionTaints(String decision, Taint<T> execCtxTaints, Taint<T> conditionTaints) {
    this.decision = decision;
    this.execCtxTaints = execCtxTaints;
    this.conditionTaints = conditionTaints;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    DecisionTaints<?> that = (DecisionTaints<?>) o;

    if (!decision.equals(that.decision)) {
      return false;
    }
    if (!Objects.equals(execCtxTaints, that.execCtxTaints)) {
      return false;
    }

    return Objects.equals(conditionTaints, that.conditionTaints);
  }

  @Override
  public int hashCode() {
    int result = decision.hashCode();
    result = 31 * result + (execCtxTaints != null ? execCtxTaints.hashCode() : 0);
    result = 31 * result + (conditionTaints != null ? conditionTaints.hashCode() : 0);
    return result;
  }

  public String getDecision() {
    return decision;
  }

  public Taint<T> getExecCtxTaints() {
    return execCtxTaints;
  }

  public Taint<T> getConditionTaints() {
    return conditionTaints;
  }
}
