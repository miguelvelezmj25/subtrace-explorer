package edu.cmu.cs.mvelezce.explorer.utils;

import edu.cmu.cs.mvelezce.adapter.utils.Executor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class Packager {

  private Packager() {}

  public static void packageJar(String dir) throws InterruptedException, IOException {
    ProcessBuilder builder = new ProcessBuilder();

    List<String> commandList = buildCommandAsList();
    builder.command(commandList);
    builder.directory(new File(dir));
    Process process = builder.start();

    Executor.processOutput(process);
    Executor.processError(process);

    process.waitFor();
  }

  private static List<String> buildCommandAsList() {
    List<String> commands = new ArrayList<>();

    commands.add("mvn");
    commands.add("clean");
    commands.add("package");
    commands.add("-DskipTests");

    return commands;
  }
}
