package edu.cmu.cs.mvelezce.explorer.idta.execute;

import edu.cmu.cs.mvelezce.adapter.adapters.Adapter;
import edu.cmu.cs.mvelezce.adapter.adapters.cannotExpandConstraintsDown.BaseCannotExpandConstraintsDownAdapter;
import edu.cmu.cs.mvelezce.adapter.adapters.contextDataTaintsEqual.BaseContextDataTaintsEqualAdapter;
import edu.cmu.cs.mvelezce.adapter.adapters.iGen.BaseIGenAdapter;
import edu.cmu.cs.mvelezce.adapter.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapter.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.adapter.adapters.pngtastic.BasePngtasticAdapter;
import edu.cmu.cs.mvelezce.adapter.adapters.trivial.BaseTrivialAdapter;
import edu.cmu.cs.mvelezce.adapter.utils.Executor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class DynamicAnalysisExecutor {

  private static final String PHOSPHOR_SCRIPTS_DIR =
      "../phosphor/Phosphor/scripts/run-instrumented/implicit-optimized";

  private final String programName;

  public DynamicAnalysisExecutor(String programName) {
    this.programName = programName;
  }

  public void runAnalysis(Set<String> config) throws IOException, InterruptedException {
    ProcessBuilder builder = new ProcessBuilder();

    List<String> commandList = this.buildCommandAsList(config);
    builder.command(commandList);
    builder.directory(new File(PHOSPHOR_SCRIPTS_DIR));

    System.out.println("Running program");
    Process process = builder.start();

    Executor.processOutput(process);
    Executor.processError(process);

    process.waitFor();
  }

  // TODO the access level was changed to hardcode some logic to execute all dynamic examples
  private List<String> buildCommandAsList(Set<String> config) {
    List<String> commandList = new ArrayList<>();

    String programName = this.programName;
    Adapter adapter;

    switch (programName) {
        //      case DynamicRunningExampleAdapter.PROGRAM_NAME:
        //        commandList.add("./examples.sh");
        //        adapter = new DynamicRunningExampleAdapter();
        //        break;
        //      case PhosphorExample2Adapter.PROGRAM_NAME:
        //        commandList.add("./examples.sh");
        //        adapter = new PhosphorExample2Adapter();
        //        break;
        //      case PhosphorExample8Adapter.PROGRAM_NAME:
        //        commandList.add("./examples.sh");
        //        adapter = new PhosphorExample8Adapter();
        //        break;
        //      case PhosphorExample3Adapter.PROGRAM_NAME:
        //        commandList.add("./examples.sh");
        //        adapter = new PhosphorExample3Adapter();
        //        break;
        //      case SimpleExample1Adapter.PROGRAM_NAME:
        //        commandList.add("./examples.sh");
        //        adapter = new SimpleExample1Adapter();
        //        break;
        //      case Example1Adapter.PROGRAM_NAME:
        //        commandList.add("./examples.sh");
        //        adapter = new Example1Adapter();
        //        break;
        //      case MultiFacetsAdapter.PROGRAM_NAME:
        //        commandList.add("./examples.sh");
        //        adapter = new MultiFacetsAdapter();
        //        break;
        //      case SimpleForExample2Adapter.PROGRAM_NAME:
        //        commandList.add("./examples.sh");
        //        adapter = new SimpleForExample2Adapter();
        //        break;
        //      case SimpleForExample4Adapter.PROGRAM_NAME:
        //        commandList.add("./examples.sh");
        //        adapter = new SimpleForExample4Adapter();
        //        break;
        //      case SimpleForExample5Adapter.PROGRAM_NAME:
        //        commandList.add("./examples.sh");
        //        adapter = new SimpleForExample5Adapter();
        //        break;
        //      case SimpleForExample6Adapter.PROGRAM_NAME:
        //        commandList.add("./examples.sh");
        //        adapter = new SimpleForExample6Adapter();
        //        break;
        //      case OrContextAdapter.PROGRAM_NAME:
        //        commandList.add("./examples.sh");
        //        adapter = new OrContextAdapter();
        //        break;
        //      case OrContext2Adapter.PROGRAM_NAME:
        //        commandList.add("./examples.sh");
        //        adapter = new OrContext2Adapter();
        //        break;
        //      case OrContext3Adapter.PROGRAM_NAME:
        //        commandList.add("./examples.sh");
        //        adapter = new OrContext3Adapter();
        //        break;
        //      case OrContext6Adapter.PROGRAM_NAME:
        //        commandList.add("./examples.sh");
        //        adapter = new OrContext6Adapter();
        //        break;
        //      case IfOr2Adapter.PROGRAM_NAME:
        //        commandList.add("./examples.sh");
        //        adapter = new IfOr2Adapter();
        //        break;
        //      case VariabilityContext1Adapter.PROGRAM_NAME:
        //        commandList.add("./examples.sh");
        //        adapter = new VariabilityContext1Adapter();
        //        break;
        //      case VariabilityContext2Adapter.PROGRAM_NAME:
        //        commandList.add("./examples.sh");
        //        adapter = new VariabilityContext2Adapter();
        //        break;
        //      case SubtracesAdapter.PROGRAM_NAME:
        //        commandList.add("./examples.sh");
        //        adapter = new SubtracesAdapter();
        //        break;
        //      case Subtraces2Adapter.PROGRAM_NAME:
        //        commandList.add("./examples.sh");
        //        adapter = new Subtraces2Adapter();
        //        break;
        //      case Subtraces6Adapter.PROGRAM_NAME:
        //        commandList.add("./examples.sh");
        //        adapter = new Subtraces6Adapter();
        //        break;
        //      case Subtraces7Adapter.PROGRAM_NAME:
        //        commandList.add("./examples.sh");
        //        adapter = new Subtraces7Adapter();
        //        break;
        //      case ImplicitAdapter.PROGRAM_NAME:
        //        commandList.add("./examples.sh");
        //        adapter = new ImplicitAdapter();
        //        break;
        //      case Implicit2Adapter.PROGRAM_NAME:
        //        commandList.add("./examples.sh");
        //        adapter = new Implicit2Adapter();
        //        break;
      case BaseTrivialAdapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new BaseTrivialAdapter();
        break;
      case BaseIGenAdapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new BaseIGenAdapter();
        break;
      case BasePngtasticAdapter.PROGRAM_NAME:
        commandList.add("./counter.sh");
        adapter = new BasePngtasticAdapter();
        break;
      case BaseContextDataTaintsEqualAdapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new BaseContextDataTaintsEqualAdapter();
        break;
      case BaseCannotExpandConstraintsDownAdapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new BaseCannotExpandConstraintsDownAdapter();
        break;
        //      case SoundAdapter.PROGRAM_NAME:
        //        commandList.add("./examples.sh");
        //        adapter = new SoundAdapter();
        //        break;
        //      case ConstructorAdapter.PROGRAM_NAME:
        //        commandList.add("./examples.sh");
        //        adapter = new ConstructorAdapter();
        //        break;
        //      case PrevaylerAdapter.PROGRAM_NAME:
        //        commandList.add("./prevayler.sh");
        //        commandList.add(PrevaylerAdapter.PROGRAM_NAME);
        //        adapter = new PrevaylerAdapter();
        //        break;
      case BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME:
        commandList.add("./measureDiskOrderedScan.sh");
        adapter = new BaseMeasureDiskOrderedScanAdapter();
        ((BaseMeasureDiskOrderedScanAdapter) adapter).preProcess();
        break;
      case BaseIndexFilesAdapter.PROGRAM_NAME:
        commandList.add("./indexFiles.sh");
        adapter = new BaseIndexFilesAdapter();
        ((BaseIndexFilesAdapter) adapter).preProcess();
        break;
        //      case NestingAdapter.PROGRAM_NAME:
        //        commandList.add("./examples.sh");
        //        adapter = new NestingAdapter();
        //        break;
      default:
        throw new RuntimeException("Could not find a phosphor script to run " + programName);
    }

    // TODO change the following method to take a Config object
    String[] configArgs = adapter.configurationAsMainArguments(config);
    List<String> configList = Arrays.asList(configArgs);
    commandList.add(adapter.getMainClass());
    commandList.addAll(configList);

    return commandList;
  }
}
