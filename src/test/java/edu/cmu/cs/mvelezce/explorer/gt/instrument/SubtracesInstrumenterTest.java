package edu.cmu.cs.mvelezce.explorer.gt.instrument;

import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.adapters.pngtastic.BasePngtasticAdapter;
import edu.cmu.cs.mvelezce.adapters.trivial.BaseTrivialAdapter;
import edu.cmu.cs.mvelezce.instrumenter.instrument.Instrumenter;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class SubtracesInstrumenterTest {

  @Test
  public void instrumentTraces()
      throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException,
          InvocationTargetException {
    String programName = "phosphorExamples";
    String mainClass = BaseTrivialAdapter.MAIN_CLASS;
    String srcDir = BaseTrivialAdapter.INSTRUMENTED_DIR_PATH;
    String classDir = BaseTrivialAdapter.INSTRUMENTED_CLASS_PATH;
    Instrumenter instrumenter = new SubtracesInstrumenter(programName, mainClass, srcDir, classDir);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    instrumenter.instrument(args);
  }

  @Test
  public void instrumentMeasureDiskOrderedScan()
      throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException,
          InvocationTargetException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    String mainClass = BaseMeasureDiskOrderedScanAdapter.MAIN_CLASS;
    String srcDir = BaseMeasureDiskOrderedScanAdapter.INSTRUMENTED_DIR_PATH;
    String classDir = BaseMeasureDiskOrderedScanAdapter.INSTRUMENTED_CLASS_PATH;
    Instrumenter instrumenter = new SubtracesInstrumenter(programName, mainClass, srcDir, classDir);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    instrumenter.instrument(args);
  }

  @Test
  public void pngtasticCounter()
      throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException,
          InvocationTargetException {
    String programName = BasePngtasticAdapter.PROGRAM_NAME;
    String mainClass = BasePngtasticAdapter.MAIN_CLASS;
    String srcDir = BasePngtasticAdapter.INSTRUMENTED_DIR_PATH;
    String classDir = BasePngtasticAdapter.INSTRUMENTED_CLASS_PATH;
    Instrumenter instrumenter = new SubtracesInstrumenter(programName, mainClass, srcDir, classDir);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    instrumenter.instrument(args);
  }

  @Test
  public void instrumentIndexFiles()
      throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException,
          InvocationTargetException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    String mainClass = BaseIndexFilesAdapter.MAIN_CLASS;
    String srcDir = BaseIndexFilesAdapter.INSTRUMENTED_DIR_PATH;
    String classDir = BaseIndexFilesAdapter.INSTRUMENTED_CLASS_PATH;
    Instrumenter instrumenter = new SubtracesInstrumenter(programName, mainClass, srcDir, classDir);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    instrumenter.instrument(args);
  }
}
