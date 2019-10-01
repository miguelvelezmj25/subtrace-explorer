package edu.cmu.cs.mvelezce.explorer.gt.execute;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConfigToTraceInfo {

  private final Set<String> config;
  private final List<String> trace;

  // Dummy constructor needed for jackson xml
  private ConfigToTraceInfo() {
    this.config = new HashSet<>();
    this.trace = new ArrayList<>();
  }

  public ConfigToTraceInfo(Set<String> config, List<String> trace) {
    this.config = config;
    this.trace = trace;
  }

  public Set<String> getConfig() {
    return config;
  }

  public List<String> getTrace() {
    return trace;
  }
}
