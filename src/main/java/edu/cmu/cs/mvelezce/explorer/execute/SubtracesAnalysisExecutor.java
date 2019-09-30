package edu.cmu.cs.mvelezce.explorer.execute;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.adapter.adapters.Adapter;
import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;
import edu.cmu.cs.mvelezce.adapter.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapter.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.adapter.adapters.trivial.BaseTrivialAdapter;
import edu.cmu.cs.mvelezce.adapter.utils.Executor;
import edu.cmu.cs.mvelezce.analysis.dynamic.BaseDynamicAnalysis;
import edu.cmu.cs.mvelezce.explorer.Explorer;
import edu.cmu.cs.mvelezce.explorer.log.SubtracesLogger;
import edu.cmu.cs.mvelezce.utils.ConfigHelper;
import edu.cmu.cs.mvelezce.utils.Options;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Class to execute the programs that have been instrumented with code to perform subtrace analysis.
 */
public class SubtracesAnalysisExecutor extends BaseDynamicAnalysis<Map<Set<String>, List<String>>> {

  private static final String PHOSPHOR_CLASS_PATH =
      "../phosphor/Phosphor/target/Phosphor-0.0.4-SNAPSHOT.jar";

  private final Map<Set<String>, List<String>> configsToTraces = new HashMap<>();

  SubtracesAnalysisExecutor(String programName, Set<String> options) {
    super(programName, options, new HashSet<>());
  }

  public SubtracesAnalysisExecutor(String programName) {
    this(programName, new HashSet<>());
  }

  @Override
  public Map<Set<String>, List<String>> analyze(String[] args)
      throws IOException, InterruptedException {
    Options.getCommandLine(args);

    String outputFile = this.outputDir();
    File file = new File(outputFile);

    Options.checkIfDeleteResult(file);

    if (file.exists()) {
      return this.readFromFile(file);
    }

    Map<Set<String>, List<String>> analysisResults = this.analyze();

    if (Options.checkIfSave()) {
      this.writeToFile(analysisResults);
    }

    return analysisResults;
  }

  @Override
  public Map<Set<String>, List<String>> analyze() throws IOException, InterruptedException {
    Set<Set<String>> configs = ConfigHelper.getConfigurations(this.getOptions());
    Iterator<Set<String>> configsIter = configs.iterator();

    for (int i = 0; configsIter.hasNext(); i++) {
      Set<String> config = configsIter.next();
      this.runProgram(config);
      this.processResults(config, i);
    }

    Files.deleteIfExists(Paths.get(SubtracesLogger.RESULTS_FILE));

    return configsToTraces;
  }

  private void processResults(Set<String> config, int i) throws IOException {
    List<String> trace = this.getTrace();
    this.writeToFile(config, trace, i);
    //    configsToTraces.put(config, trace);
  }

  private void writeToFile(Set<String> config, List<String> trace, int i) throws IOException {
    File file = new File(this.outputDir());
    file.mkdirs();

    ConfigToTraceInfo configToTraceInfo = new ConfigToTraceInfo(config, trace);

    List<ConfigToTraceInfo> infos = new ArrayList<>();
    infos.add(configToTraceInfo);

    ObjectMapper mapper = new ObjectMapper();
    File outputFile = new File(file, i + Options.DOT_JSON);
    mapper.writeValue(outputFile, infos);
  }

  private List<String> getTrace() throws IOException {
    List<String> trace = new ArrayList<>();
    BufferedReader reader = new BufferedReader(new FileReader(SubtracesLogger.RESULTS_FILE));
    String line;

    while ((line = reader.readLine()) != null) {
      trace.add(line);
    }

    reader.close();

    return trace;
  }

  // TODO abstract since it is repeated with SubtraceLabeler
  @Override
  public Map<Set<String>, List<String>> readFromFile(File file) throws IOException {
    Collection<File> files = FileUtils.listFiles(file, null, true);

    for (File resultFile : files) {
      ObjectMapper mapper = new ObjectMapper();
      List<ConfigToTraceInfo> configToTraceInfoList =
          mapper.readValue(resultFile, new TypeReference<List<ConfigToTraceInfo>>() {});

      for (ConfigToTraceInfo configToTraceInfo : configToTraceInfoList) {
        configsToTraces.put(configToTraceInfo.getConfig(), configToTraceInfo.getTrace());
      }
    }

    return configsToTraces;
  }

  // TODO abstract since it is repeated with SubtraceLabeler
  @Override
  public void writeToFile(Map<Set<String>, List<String>> analysisResults) throws IOException {
    System.err.println(
        "Not writing anything since we already wrote the files after each execution");
    //    File file = new File(this.outputDir());
    //    file.mkdirs();
    //
    //    Iterator<Entry<Set<String>, List<String>>> iter = analysisResults.entrySet().iterator();
    //
    //    for (int i = 0; iter.hasNext(); i++) {
    //      Entry<Set<String>, List<String>> entry = iter.next();
    //      ConfigToTraceInfo configToTraceInfo = new ConfigToTraceInfo(entry.getKey(),
    // entry.getValue());
    //
    //      List<ConfigToTraceInfo> infos = new ArrayList<>();
    //      infos.add(configToTraceInfo);
    //
    //      ObjectMapper mapper = new ObjectMapper();
    //      File outputFile = new File(file, i + Options.DOT_JSON);
    //      mapper.writeValue(outputFile, infos);
    //    }
  }

  @Override
  public String outputDir() {
    return Explorer.OUTPUT_DIR + "/execute/traces/java/programs/" + this.getProgramName();
  }

  private void runProgram(Set<String> config) throws IOException, InterruptedException {
    ProcessBuilder builder = new ProcessBuilder();

    List<String> commandList = this.buildCommandAsList(config);
    builder.command(commandList);

    System.out.println("Running program");
    Process process = builder.start();

    Executor.processOutput(process);
    Executor.processError(process);

    process.waitFor();
  }

  private List<String> buildCommandAsList(Set<String> config) {
    List<String> commandList = new ArrayList<>();
    commandList.add("time");
    commandList.add("java");
    commandList.add("-Xmx12g");
    commandList.add("-Xms12g");
    commandList.add("-XX:+UseConcMarkSweepGC");
    //    commandList.add("-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005");
    commandList.add("-cp");

    String programName = this.getProgramName();
    //        + BaseAdapter.PATH_SEPARATOR
    //        + APACHE_COMMONS_PATH;
    Adapter adapter;

    switch (programName) {
        //      case AbstractDynamicRunningExampleAdapter.PROGRAM_NAME:
        //
        // commandList.add(this.getClassPath(AbstractDynamicRunningExampleAdapter.INSTRUMENTED_CLASS_PATH));
        //        adapter = new AbstractDynamicRunningExampleAdapter();
        //        break;
        //      case AbstractSimpleExample1Adapter.PROGRAM_NAME:
        //
        // commandList.add(this.getClassPath(AbstractSimpleExample1Adapter.INSTRUMENTED_CLASS_PATH));
        //        adapter = new AbstractSimpleExample1Adapter();
        //        break;
        //      case AbstractExample1Adapter.PROGRAM_NAME:
        //
        // commandList.add(this.getClassPath(AbstractExample1Adapter.INSTRUMENTED_CLASS_PATH));
        //        adapter = new AbstractExample1Adapter();
        //        break;
        //      case AbstractPhosphorExample2Adapter.PROGRAM_NAME:
        //
        // commandList.add(this.getClassPath(AbstractPhosphorExample2Adapter.INSTRUMENTED_CLASS_PATH));
        //        adapter = new AbstractPhosphorExample2Adapter();
        //        break;
        //      case AbstractOrContextAdapter.PROGRAM_NAME:
        //
        // commandList.add(this.getClassPath(AbstractOrContextAdapter.INSTRUMENTED_CLASS_PATH));
        //        adapter = new AbstractOrContextAdapter();
        //        break;
        //      case AbstractMultiFacetsAdapter.PROGRAM_NAME:
        //
        // commandList.add(this.getClassPath(AbstractMultiFacetsAdapter.INSTRUMENTED_CLASS_PATH));
        //        adapter = new AbstractMultiFacetsAdapter();
        //        break;
        //      case AbstractSimpleForExampleAdapter.PROGRAM_NAME:
        //
        // commandList.add(this.getClassPath(AbstractSimpleForExampleAdapter.INSTRUMENTED_CLASS_PATH));
        //        adapter = new AbstractSimpleForExampleAdapter();
        //        break;
        //      case AbstractSimpleForExample2Adapter.PROGRAM_NAME:
        //
        // commandList.add(this.getClassPath(AbstractSimpleForExample2Adapter.INSTRUMENTED_CLASS_PATH));
        //        adapter = new AbstractSimpleForExample2Adapter();
        //        break;
        //      case AbstractSimpleForExample3Adapter.PROGRAM_NAME:
        //
        // commandList.add(this.getClassPath(AbstractSimpleForExample3Adapter.INSTRUMENTED_CLASS_PATH));
        //        adapter = new AbstractSimpleForExample3Adapter();
        //        break;
        //      case AbstractSimpleForExample4Adapter.PROGRAM_NAME:
        //
        // commandList.add(this.getClassPath(AbstractSimpleForExample4Adapter.INSTRUMENTED_CLASS_PATH));
        //        adapter = new AbstractSimpleForExample4Adapter();
        //        break;
        //      case AbstractSimpleForExample5Adapter.PROGRAM_NAME:
        //
        // commandList.add(this.getClassPath(AbstractSimpleForExample5Adapter.INSTRUMENTED_CLASS_PATH));
        //        adapter = new AbstractSimpleForExample5Adapter();
        //        break;
        //      case AbstractReturnExampleAdapter.PROGRAM_NAME:
        //
        // commandList.add(this.getClassPath(AbstractReturnExampleAdapter.INSTRUMENTED_CLASS_PATH));
        //        adapter = new AbstractReturnExampleAdapter();
        //        break;
        //      case AbstractReturnExample2Adapter.PROGRAM_NAME:
        //
        // commandList.add(this.getClassPath(AbstractReturnExample2Adapter.INSTRUMENTED_CLASS_PATH));
        //        adapter = new AbstractReturnExample2Adapter();
        //        break;
        //      case AbstractReturn2ExampleAdapter.PROGRAM_NAME:
        //
        // commandList.add(this.getClassPath(AbstractReturn2ExampleAdapter.INSTRUMENTED_CLASS_PATH));
        //        adapter = new AbstractReturn2ExampleAdapter();
        //        break;
        //      case AbstractSubtracesAdapter.PROGRAM_NAME:
        //
        // commandList.add(this.getClassPath(AbstractSubtracesAdapter.INSTRUMENTED_CLASS_PATH));
        //        adapter = new AbstractSubtracesAdapter();
        //        break;
        //      case AbstractSubtraces2Adapter.PROGRAM_NAME:
        //
        // commandList.add(this.getClassPath(AbstractSubtraces2Adapter.INSTRUMENTED_CLASS_PATH));
        //        adapter = new AbstractSubtraces2Adapter();
        //        break;
        //      case AbstractSubtraces3Adapter.PROGRAM_NAME:
        //
        // commandList.add(this.getClassPath(AbstractSubtraces3Adapter.INSTRUMENTED_CLASS_PATH));
        //        adapter = new AbstractSubtraces3Adapter();
        //        break;
        //      case AbstractSubtraces4Adapter.PROGRAM_NAME:
        //
        // commandList.add(this.getClassPath(AbstractSubtraces4Adapter.INSTRUMENTED_CLASS_PATH));
        //        adapter = new AbstractSubtraces4Adapter();
        //        break;
        //      case AbstractNestingAdapter.PROGRAM_NAME:
        //
        // commandList.add(this.getClassPath(AbstractNestingAdapter.INSTRUMENTED_CLASS_PATH));
        //        adapter = new AbstractNestingAdapter();
        //        break;
        //      case AbstractImplicitAdapter.PROGRAM_NAME:
        //
        // commandList.add(this.getClassPath(AbstractImplicitAdapter.INSTRUMENTED_CLASS_PATH));
        //        adapter = new AbstractImplicitAdapter();
        //        break;
        //      case AbstractImplicit2Adapter.PROGRAM_NAME:
        //
        // commandList.add(this.getClassPath(AbstractImplicit2Adapter.INSTRUMENTED_CLASS_PATH));
        //        adapter = new AbstractImplicit2Adapter();
        //        break;
        //      case AbstractAndContextAdapter.PROGRAM_NAME:
        //
        // commandList.add(this.getClassPath(AbstractAndContextAdapter.INSTRUMENTED_CLASS_PATH));
        //        adapter = new AbstractAndContextAdapter();
        //        break;
      case BaseTrivialAdapter.PROGRAM_NAME:
        commandList.add(this.getClassPath(BaseTrivialAdapter.INSTRUMENTED_CLASS_PATH));
        adapter = new BaseTrivialAdapter();
        break;
        //      case AbstractPrevaylerAdapter.PROGRAM_NAME:
        //        commandList.add(
        //            this.getClassPath(AbstractPrevaylerAdapter.INSTRUMENTED_CLASS_PATH)
        //                + BaseAdapter.PATH_SEPARATOR
        //                + PrevaylerAdapter.CLASS_PATH);
        //        adapter = new AbstractPrevaylerAdapter();
        //        break;
      case BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME:
        commandList.add(
            this.getClassPath(BaseMeasureDiskOrderedScanAdapter.INSTRUMENTED_CLASS_PATH));
        adapter = new BaseMeasureDiskOrderedScanAdapter();
        ((BaseMeasureDiskOrderedScanAdapter) adapter).preProcess();
        break;
      case BaseIndexFilesAdapter.PROGRAM_NAME:
        commandList.add(this.getClassPath(BaseIndexFilesAdapter.INSTRUMENTED_CLASS_PATH));
        adapter = new BaseIndexFilesAdapter();
        ((BaseIndexFilesAdapter) adapter).preProcess();
        break;
      default:
        throw new RuntimeException("Could not find an adapter for " + programName);
        //        if (this.mainClass != null) {
        //          commandList.add(ccClasspath
        //              + BaseAdapter.PATH_SEPARATOR
        //              + AllDynamicAdapter.INSTRUMENTED_CLASS_PATH);
        //          adapter = new AbstractAllDynamicAdapter(programName, this.mainClass);
        //        }
        //        else {
        //          throw new RuntimeException("Could not find a phosphor script to run " +
        // programName);
        //        }
    }

    commandList.add(adapter.getMainClass());
    String[] configArgs = adapter.configurationAsMainArguments(config);
    List<String> configList = Arrays.asList(configArgs);
    commandList.addAll(configList);

    return commandList;
  }

  private String getClassPath(String instrumentedClassPath) {
    return BaseAdapter.CLASS_PATH
        + BaseAdapter.PATH_SEPARATOR
        + instrumentedClassPath; // + BaseAdapter.PATH_SEPARATOR + PHOSPHOR_CLASS_PATH;
  }
}
