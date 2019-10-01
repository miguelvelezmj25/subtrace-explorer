package edu.cmu.cs.mvelezce.explorer.log;

import jdk.internal.org.objectweb.asm.Type;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SubtracesLogger {
  public static final String INTERNAL_NAME = Type.getInternalName(SubtracesLogger.class);
  public static final String RESULTS_FILE = "results.ser";
  public static final String FALSE = "FALSE";
  public static final String TRUE = "TRUE";
  public static final String ENTER_DECISION = "Enter";
  public static final String EXIT_DECISION = "Exit";
  public static final String EXIT_DECISION_AT_RETURN = "ExitReturn";
  public static final String ARROW = " --> ";
  private static final byte[] ENTER_DECISION_BYTES = ENTER_DECISION.getBytes();
  private static final byte[] EXIT_DECISION_BYTES = EXIT_DECISION.getBytes();
  private static final byte[] EXIT_DECISION_AT_RETURN_BYTES = EXIT_DECISION_AT_RETURN.getBytes();
  private static final byte[] FALSE_BYTES = FALSE.getBytes();
  private static final byte[] TRUE_BYTES = TRUE.getBytes();
  private static final byte[] ARROW_BYTES = ARROW.getBytes();
  private static final byte[] SPACE_BYTES = " ".getBytes();
  private static final byte[] NEW_LINE_BYTES = "\n".getBytes();
  private static final int EXIT_AT_RETURN_FLAG_COUNT = -1;
  private static final Map<String, String> METHODS_TO_DESCRIPTORS = new HashMap<>();
  private static final File OUTPUT_FILE = new File(RESULTS_FILE);
  private static final FileOutputStream FOS;

  static {
    try {
      FOS = new FileOutputStream(OUTPUT_FILE);
    } catch (FileNotFoundException e) {
      throw new RuntimeException("Could not initialize the file output stream", e);
    }

    Method[] methods = SubtracesLogger.class.getDeclaredMethods();

    for (Method method : methods) {
      METHODS_TO_DESCRIPTORS.put(method.getName(), Type.getMethodDescriptor(method));
    }
  }

  public static String getMethodDescriptor(String methodName) {
    String methodDescriptor = METHODS_TO_DESCRIPTORS.get(methodName);

    if (methodDescriptor == null) {
      throw new RuntimeException(
          "Could not find the method " + methodName + " to add it in the instrumentation");
    }

    return methodDescriptor;
  }

  public static void saveTrace() {
    try {
      FOS.flush();
      FOS.close();
    } catch (IOException ioe) {
      throw new RuntimeException("There was an error serializing the results", ioe);
    }
  }

  private static synchronized void enterDecision(String labelPrefix) {
    writeAction(labelPrefix, ENTER_DECISION_BYTES);
  }

  public static synchronized void exitDecision(String labelPrefix) {
    writeAction(labelPrefix, EXIT_DECISION_BYTES);
  }

  public static synchronized void exitAtReturn(String labelPrefix) {
    writeAction(labelPrefix, EXIT_DECISION_AT_RETURN_BYTES);
  }

  private static void writeAction(String labelPrefix, byte[] action) {
    try {
      FOS.write(Long.toString(Thread.currentThread().getId()).getBytes());
      FOS.write(ARROW_BYTES);
      FOS.write(action);
      FOS.write(SPACE_BYTES);
      FOS.write(labelPrefix.getBytes());
      FOS.write(NEW_LINE_BYTES);
    } catch (IOException ioe) {
      throw new RuntimeException("Could not write to file when entering decision");
    }
  }

  public static synchronized void logIFEQEval(int value, String labelPrefix) {
    enterDecision(labelPrefix);

    byte[] res = TRUE_BYTES;

    if (value == 0) {
      res = FALSE_BYTES;
    }

    try {
      FOS.write(res);
      FOS.write(NEW_LINE_BYTES);
    } catch (IOException ioe) {
      throw new RuntimeException("Could not write res to file: " + Arrays.toString(res));
    }
  }

  public static synchronized void logIFNEEval(int value, String labelPrefix) {
    enterDecision(labelPrefix);

    byte[] res = TRUE_BYTES;

    if (value != 0) {
      res = FALSE_BYTES;
    }

    try {
      FOS.write(res);
      FOS.write(NEW_LINE_BYTES);
    } catch (IOException ioe) {
      throw new RuntimeException("Could not write res to file: " + Arrays.toString(res));
    }
  }

  public static synchronized void logIFLTEval(int value, String labelPrefix) {
    enterDecision(labelPrefix);

    byte[] res = TRUE_BYTES;

    if (value < 0) {
      res = FALSE_BYTES;
    }

    try {
      FOS.write(res);
      FOS.write(NEW_LINE_BYTES);
    } catch (IOException ioe) {
      throw new RuntimeException("Could not write res to file: " + Arrays.toString(res));
    }
  }

  public static synchronized void logIFGEEval(int value, String labelPrefix) {
    enterDecision(labelPrefix);

    byte[] res = TRUE_BYTES;

    if (value >= 0) {
      res = FALSE_BYTES;
    }

    try {
      FOS.write(res);
      FOS.write(NEW_LINE_BYTES);
    } catch (IOException ioe) {
      throw new RuntimeException("Could not write res to file: " + Arrays.toString(res));
    }
  }

  public static synchronized void logIFGTEval(int value, String labelPrefix) {
    enterDecision(labelPrefix);

    byte[] res = TRUE_BYTES;

    if (value > 0) {
      res = FALSE_BYTES;
    }

    try {
      FOS.write(res);
      FOS.write(NEW_LINE_BYTES);
    } catch (IOException ioe) {
      throw new RuntimeException("Could not write res to file: " + Arrays.toString(res));
    }
  }

  public static synchronized void logIFLEEval(int value, String labelPrefix) {
    enterDecision(labelPrefix);

    byte[] res = TRUE_BYTES;

    if (value <= 0) {
      res = FALSE_BYTES;
    }

    try {
      FOS.write(res);
      FOS.write(NEW_LINE_BYTES);
    } catch (IOException ioe) {
      throw new RuntimeException("Could not write res to file: " + Arrays.toString(res));
    }
  }

  public static synchronized void logIF_ICMPEQEval(int v1, int v2, String labelPrefix) {
    enterDecision(labelPrefix);

    byte[] res = TRUE_BYTES;

    if (v1 == v2) {
      res = FALSE_BYTES;
    }

    try {
      FOS.write(res);
      FOS.write(NEW_LINE_BYTES);
    } catch (IOException ioe) {
      throw new RuntimeException("Could not write res to file: " + Arrays.toString(res));
    }
  }

  public static synchronized void logIF_ICMPNEEval(int v1, int v2, String labelPrefix) {
    enterDecision(labelPrefix);

    byte[] res = TRUE_BYTES;

    if (v1 != v2) {
      res = FALSE_BYTES;
    }

    try {
      FOS.write(res);
      FOS.write(NEW_LINE_BYTES);
    } catch (IOException ioe) {
      throw new RuntimeException("Could not write res to file: " + Arrays.toString(res));
    }
  }

  public static synchronized void logIF_ICMPLTEval(int v1, int v2, String labelPrefix) {
    enterDecision(labelPrefix);

    byte[] res = TRUE_BYTES;

    if (v1 < v2) {
      res = FALSE_BYTES;
    }

    try {
      FOS.write(res);
      FOS.write(NEW_LINE_BYTES);
    } catch (IOException ioe) {
      throw new RuntimeException("Could not write res to file: " + Arrays.toString(res));
    }
  }

  public static synchronized void logIF_ICMPGEEval(int v1, int v2, String labelPrefix) {
    enterDecision(labelPrefix);

    byte[] res = TRUE_BYTES;

    if (v1 >= v2) {
      res = FALSE_BYTES;
    }

    try {
      FOS.write(res);
      FOS.write(NEW_LINE_BYTES);
    } catch (IOException ioe) {
      throw new RuntimeException("Could not write res to file: " + Arrays.toString(res));
    }
  }

  public static synchronized void logIF_ICMPGTEval(int v1, int v2, String labelPrefix) {
    enterDecision(labelPrefix);

    byte[] res = TRUE_BYTES;

    if (v1 > v2) {
      res = FALSE_BYTES;
    }

    try {
      FOS.write(res);
      FOS.write(NEW_LINE_BYTES);
    } catch (IOException ioe) {
      throw new RuntimeException("Could not write res to file: " + Arrays.toString(res));
    }
  }

  public static synchronized void logIF_ICMPLEEval(int v1, int v2, String labelPrefix) {
    enterDecision(labelPrefix);

    byte[] res = TRUE_BYTES;

    if (v1 <= v2) {
      res = FALSE_BYTES;
    }

    try {
      FOS.write(res);
      FOS.write(NEW_LINE_BYTES);
    } catch (IOException ioe) {
      throw new RuntimeException("Could not write res to file: " + Arrays.toString(res));
    }
  }

  public static synchronized void logIF_ACMPEQEval(Object o1, Object o2, String labelPrefix) {
    enterDecision(labelPrefix);

    byte[] res = TRUE_BYTES;

    if (o1 == o2) {
      res = FALSE_BYTES;
    }

    try {
      FOS.write(res);
      FOS.write(NEW_LINE_BYTES);
    } catch (IOException ioe) {
      throw new RuntimeException("Could not write res to file: " + Arrays.toString(res));
    }
  }

  public static synchronized void logIF_ACMPNEEval(Object o1, Object o2, String labelPrefix) {
    enterDecision(labelPrefix);

    byte[] res = TRUE_BYTES;

    if (o1 != o2) {
      res = FALSE_BYTES;
    }

    try {
      FOS.write(res);
      FOS.write(NEW_LINE_BYTES);
    } catch (IOException ioe) {
      throw new RuntimeException("Could not write res to file: " + Arrays.toString(res));
    }
  }

  public static synchronized void logIFNULLEval(Object object, String labelPrefix) {
    enterDecision(labelPrefix);

    byte[] res = TRUE_BYTES;

    if (object == null) {
      res = FALSE_BYTES;
    }

    try {
      FOS.write(res);
      FOS.write(NEW_LINE_BYTES);
    } catch (IOException ioe) {
      throw new RuntimeException("Could not write res to file: " + Arrays.toString(res));
    }
  }

  public static synchronized void logIFNONNULLEval(Object object, String labelPrefix) {
    enterDecision(labelPrefix);

    byte[] res = TRUE_BYTES;

    if (object != null) {
      res = FALSE_BYTES;
    }

    try {
      FOS.write(res);
      FOS.write(NEW_LINE_BYTES);
    } catch (IOException ioe) {
      throw new RuntimeException("Could not write res to file: " + Arrays.toString(res));
    }
  }
}
