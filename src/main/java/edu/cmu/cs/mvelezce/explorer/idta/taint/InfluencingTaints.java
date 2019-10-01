package edu.cmu.cs.mvelezce.explorer.idta.taint;

import java.util.HashSet;
import java.util.Set;

public class InfluencingTaints {

  private final Set<String> context = new HashSet<>();
  private final Set<String> condition = new HashSet<>();

  private InfluencingTaints() {}

  public InfluencingTaints(Set<String> context, Set<String> condition) {
    this.context.addAll(context);
    this.condition.addAll(condition);
  }

  public Set<String> getContext() {
    return context;
  }

  public Set<String> getCondition() {
    return condition;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    InfluencingTaints taints = (InfluencingTaints) o;

    if (!context.equals(taints.context)) {
      return false;
    }
    return condition.equals(taints.condition);
  }

  @Override
  public int hashCode() {
    int result = context.hashCode();
    result = 31 * result + condition.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return this.context + " , " + this.condition;
  }
}
