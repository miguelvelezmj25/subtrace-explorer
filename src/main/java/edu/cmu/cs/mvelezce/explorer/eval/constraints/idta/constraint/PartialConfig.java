package edu.cmu.cs.mvelezce.explorer.eval.constraints.idta.constraint;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PartialConfig {

  private Map<String, Boolean> partialConfig = new HashMap<>();

  public void addEntry(String option, boolean value) {
    this.partialConfig.put(option, value);
  }

  public Map<String, Boolean> getPartialConfig() {
    return partialConfig;
  }

  void addEntries(Map<String, Boolean> values) {
    this.partialConfig.putAll(values);
  }

  Set<String> getOptions() {
    return this.partialConfig.keySet();
  }

  public Set<String> toConfig() {
    Set<String> config = new HashSet<>();

    for (Map.Entry<String, Boolean> entry : partialConfig.entrySet()) {
      if (entry.getValue()) {
        config.add(entry.getKey());
      }
    }

    return config;
  }

  boolean isSubPartialConfigOf(PartialConfig partialConfig) {
    return partialConfig.partialConfig.entrySet().containsAll(this.partialConfig.entrySet());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PartialConfig that = (PartialConfig) o;

    return partialConfig.equals(that.partialConfig);
  }

  @Override
  public int hashCode() {
    return partialConfig.hashCode();
  }

  @Override
  public String toString() {
    return this.partialConfig.toString();
  }
}
