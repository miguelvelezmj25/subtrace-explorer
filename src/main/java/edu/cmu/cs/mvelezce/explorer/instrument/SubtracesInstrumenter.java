package edu.cmu.cs.mvelezce.explorer.instrument;

import edu.cmu.cs.mvelezce.explorer.instrument.transform.SubtracesMethodTransformer;
import edu.cmu.cs.mvelezce.explorer.utils.Packager;
import edu.cmu.cs.mvelezce.instrumenter.instrument.BaseInstrumenter;
import edu.cmu.cs.mvelezce.instrumenter.transform.methodnode.MethodTransformer;
import edu.cmu.cs.mvelezce.utils.Options;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class SubtracesInstrumenter extends BaseInstrumenter {

  public static final String DIRECTORY =
      Options.DIRECTORY + "/analysis/spec/instrument/java/programs";

  SubtracesInstrumenter(String programName, String srcDir, String classDir) {
    super(programName, srcDir, classDir);

    System.err.println(
        "Check what are the cases that we are not currently handling when instrumenting for subtraces");
  }

  @Override
  public void instrument(String[] args)
      throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException,
          InvocationTargetException {
    Options.getCommandLine(args);

    File outputFile = new File(DIRECTORY + "/" + this.getProgramName());
    Options.checkIfDeleteResult(outputFile);

    if (outputFile.exists()) {
      return;
    }

    if (Options.checkIfDeleteResult()) {
      this.compile();
    }

    if (Options.checkIfSave()) {
      this.instrument();
    }
  }

  @Override
  public void instrument()
      throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    MethodTransformer transformer =
        new SubtracesMethodTransformer.Builder(this.getProgramName(), this.getClassDir())
            .setDebug(false)
            .build();
    transformer.transformMethods();
  }

  @Override
  public void compile() throws IOException, InterruptedException {
    Packager.packageJar(this.getSrcDir());
  }
}