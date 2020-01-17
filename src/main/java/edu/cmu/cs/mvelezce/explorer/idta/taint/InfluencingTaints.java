package edu.cmu.cs.mvelezce.explorer.idta.taint;

import com.google.common.base.Objects;

import java.util.HashSet;
import java.util.Set;

public class InfluencingTaints {

  private final Set<String> config = new HashSet<>();
  private final Set<String> controlTaints = new HashSet<>();
  private final Set<String> dataTaints = new HashSet<>();

  private InfluencingTaints() {}

  public InfluencingTaints(Set<String> config, Set<String> controlTaints, Set<String> dataTaints) {
    this.config.addAll(config);
    this.controlTaints.addAll(controlTaints);
    this.dataTaints.addAll(dataTaints);
  }

  public Set<String> getConfig() {
    return config;
  }

  public Set<String> getControlTaints() {
    return controlTaints;
  }

  public Set<String> getDataTaints() {
    return dataTaints;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    InfluencingTaints that = (InfluencingTaints) o;
    return Objects.equal(config, that.config)
        && Objects.equal(controlTaints, that.controlTaints)
        && Objects.equal(dataTaints, that.dataTaints);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(config, controlTaints, dataTaints);
  }

  @Override
  public String toString() {
    return this.config + " , " + this.controlTaints + " , " + this.dataTaints;
  }
}
