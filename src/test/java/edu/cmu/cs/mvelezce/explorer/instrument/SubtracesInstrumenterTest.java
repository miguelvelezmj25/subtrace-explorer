package edu.cmu.cs.mvelezce.explorer.instrument;

import edu.cmu.cs.mvelezce.adapter.trivial.TrivialAdapter;
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
    String srcDir = TrivialAdapter.INSTRUMENTED_DIR_PATH;
    String classDir = TrivialAdapter.INSTRUMENTED_CLASS_PATH;
    Instrumenter instrumenter = new SubtracesInstrumenter(programName, srcDir, classDir);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    instrumenter.instrument(args);
  }
}
