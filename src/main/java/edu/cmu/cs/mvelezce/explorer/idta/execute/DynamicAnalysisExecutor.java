package edu.cmu.cs.mvelezce.explorer.idta.execute;

import com.mijecu25.meme.utils.execute.Executor;
import edu.cmu.cs.mvelezce.adapters.Adapter;
import edu.cmu.cs.mvelezce.adapters.canExpandConstraintsDown.BaseCanExpandConstraintsDownAdapter;
import edu.cmu.cs.mvelezce.adapters.canRemoveNestedConstraintsMultipleCallSites.BaseCanRemoveNestedConstraintsMultipleCallSitesAdapter;
import edu.cmu.cs.mvelezce.adapters.cannotExpandConstraintsDown.BaseCannotExpandConstraintsDownAdapter;
import edu.cmu.cs.mvelezce.adapters.cannotRemoveNestedRegions.BaseCannotRemoveNestedRegionsAdapter;
import edu.cmu.cs.mvelezce.adapters.cleanConstraints.BaseCleanConstraintsAdapter;
import edu.cmu.cs.mvelezce.adapters.cleanConstraintsIssue.BaseCleanConstraintsIssueAdapter;
import edu.cmu.cs.mvelezce.adapters.contextDataTaintsEqual.BaseContextDataTaintsEqualAdapter;
import edu.cmu.cs.mvelezce.adapters.convert.BaseConvertAdapter;
import edu.cmu.cs.mvelezce.adapters.iGen.BaseIGenAdapter;
import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.adapters.methodCall.BaseMethodCallAdapter;
import edu.cmu.cs.mvelezce.adapters.multipleReturns.BaseMultipleReturnsAdapter;
import edu.cmu.cs.mvelezce.adapters.multithread.BaseMultithreadAdapter;
import edu.cmu.cs.mvelezce.adapters.overrideJREMethod.BaseOverrideJREMethodAdapter;
import edu.cmu.cs.mvelezce.adapters.performance.BasePerformanceAdapter;
import edu.cmu.cs.mvelezce.adapters.pngtastic.BasePngtasticAdapter;
import edu.cmu.cs.mvelezce.adapters.runBenchC.BaseRunBenchCAdapter;
import edu.cmu.cs.mvelezce.adapters.staticMethodCall.BaseStaticMethodCallAdapter;
import edu.cmu.cs.mvelezce.adapters.subtraces.BaseSubtracesAdapter;
import edu.cmu.cs.mvelezce.adapters.trivial.BaseTrivialAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class DynamicAnalysisExecutor {

  private static final String PHOSPHOR_SCRIPTS_DIR =
      "../phosphor/Phosphor/scripts/run-instrumented/control";

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
    String mainClass;

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
        mainClass = BaseTrivialAdapter.MAIN_CLASS;
        break;
      case BaseIGenAdapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new BaseIGenAdapter();
        mainClass = BaseIGenAdapter.MAIN_CLASS;
        break;
      case BasePngtasticAdapter.PROGRAM_NAME:
        commandList.add("./counter.sh");
        adapter = new BasePngtasticAdapter();
        mainClass = BasePngtasticAdapter.MAIN_CLASS;
        break;
      case BaseContextDataTaintsEqualAdapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new BaseContextDataTaintsEqualAdapter();
        mainClass = BaseContextDataTaintsEqualAdapter.MAIN_CLASS;
        break;
      case BaseCannotExpandConstraintsDownAdapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new BaseCannotExpandConstraintsDownAdapter();
        mainClass = BaseCannotExpandConstraintsDownAdapter.MAIN_CLASS;
        break;
      case BaseCanExpandConstraintsDownAdapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new BaseCanExpandConstraintsDownAdapter();
        mainClass = BaseCanExpandConstraintsDownAdapter.MAIN_CLASS;
        break;
      case BaseStaticMethodCallAdapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new BaseStaticMethodCallAdapter();
        mainClass = BaseStaticMethodCallAdapter.MAIN_CLASS;
        break;
      case BaseMethodCallAdapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new BaseMethodCallAdapter();
        mainClass = BaseMethodCallAdapter.MAIN_CLASS;
        break;
      case BaseMultipleReturnsAdapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new BaseMultipleReturnsAdapter();
        mainClass = BaseMultipleReturnsAdapter.MAIN_CLASS;
        break;
      case BaseSubtracesAdapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new BaseSubtracesAdapter();
        mainClass = BaseSubtracesAdapter.MAIN_CLASS;
        break;
      case BaseCannotRemoveNestedRegionsAdapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new BaseCannotRemoveNestedRegionsAdapter();
        mainClass = BaseCannotRemoveNestedRegionsAdapter.MAIN_CLASS;
        break;
      case BaseCanRemoveNestedConstraintsMultipleCallSitesAdapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new BaseCanRemoveNestedConstraintsMultipleCallSitesAdapter();
        mainClass = BaseCanRemoveNestedConstraintsMultipleCallSitesAdapter.MAIN_CLASS;
        break;
      case BaseCleanConstraintsAdapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new BaseCleanConstraintsAdapter();
        mainClass = BaseCleanConstraintsAdapter.MAIN_CLASS;
        break;
      case BaseCleanConstraintsIssueAdapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new BaseCleanConstraintsIssueAdapter();
        mainClass = BaseCleanConstraintsIssueAdapter.MAIN_CLASS;
        break;
      case BaseOverrideJREMethodAdapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new BaseOverrideJREMethodAdapter();
        mainClass = BaseOverrideJREMethodAdapter.MAIN_CLASS;
        break;
      case BasePerformanceAdapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new BasePerformanceAdapter();
        mainClass = BasePerformanceAdapter.MAIN_CLASS;
        break;
        //      case SoundAdapter.PROGRAM_NAME:
        //        commandList.add("./examples.sh");
        //        adapter = new SoundAdapter();
        //        mainClass = BaseTrivialAdapter.MAIN_CLASS;
        //        break;
        //      case ConstructorAdapter.PROGRAM_NAME:
        //        commandList.add("./examples.sh");
        //        adapter = new ConstructorAdapter();
        //        mainClass = BaseTrivialAdapter.MAIN_CLASS;
        //        break;
        //      case PrevaylerAdapter.PROGRAM_NAME:
        //        commandList.add("./prevayler.sh");
        //        commandList.add(PrevaylerAdapter.PROGRAM_NAME);
        //        adapter = new PrevaylerAdapter();
        //        mainClass = BaseTrivialAdapter.MAIN_CLASS;
        //        break;
      case BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME:
        commandList.add("./measureDiskOrderedScan.sh");
        adapter = new BaseMeasureDiskOrderedScanAdapter();
        ((BaseMeasureDiskOrderedScanAdapter) adapter)
            .preProcess(BaseMeasureDiskOrderedScanAdapter.ORIGINAL_ROOT_DIR);
        mainClass = BaseMeasureDiskOrderedScanAdapter.MAIN_CLASS;
        break;
      case BaseIndexFilesAdapter.PROGRAM_NAME:
        commandList.add("./indexFiles.sh");
        adapter = new BaseIndexFilesAdapter();
        ((BaseIndexFilesAdapter) adapter).preProcess();
        mainClass = BaseIndexFilesAdapter.MAIN_CLASS;
        break;
      case BaseConvertAdapter.PROGRAM_NAME:
        commandList.add("./convert.sh");
        adapter = new BaseConvertAdapter();
        ((BaseConvertAdapter) adapter).preProcess();
        mainClass = BaseConvertAdapter.MAIN_CLASS;
        break;
      case BaseMultithreadAdapter.PROGRAM_NAME:
        commandList.add("./multithread.sh");
        adapter = new BaseMultithreadAdapter();
        mainClass = BaseMultithreadAdapter.MAIN_CLASS;
        break;
      case BaseRunBenchCAdapter.PROGRAM_NAME:
        commandList.add("./runBenchC.sh");
        adapter = new BaseRunBenchCAdapter();
        mainClass = BaseRunBenchCAdapter.MAIN_CLASS;
        break;
        //      case NestingAdapter.PROGRAM_NAME:
        //        commandList.add("./examples.sh");
        //        adapter = new NestingAdapter();
        //        break;
      default:
        throw new RuntimeException("Could not find a phosphor script to run " + programName);
    }

    String[] configArgs = adapter.configurationAsMainArguments(config);
    List<String> configList = Arrays.asList(configArgs);
    commandList.add(mainClass);
    commandList.addAll(configList);

    return commandList;
  }
}
