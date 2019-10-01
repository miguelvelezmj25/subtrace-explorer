package edu.cmu.cs.mvelezce.explorer.eval.constraints.missing;

import edu.cmu.cs.mvelezce.adapter.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapter.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.adapter.adapters.trivial.BaseTrivialAdapter;
import edu.cmu.cs.mvelezce.explorer.eval.constraints.subtraces.SubtraceOutcomeConstraint;
import edu.cmu.cs.mvelezce.explorer.eval.constraints.subtraces.SubtracesConstraintsAnalyzer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

public class ControlFlowStatementWithMissingConstraintAnalyzerTest {

  private Set<SubtraceOutcomeConstraint> getSubtracesOutcomeConstraint(String programName)
      throws IOException {
    SubtracesConstraintsAnalyzer subtracesConstraintsAnalyzer =
        new SubtracesConstraintsAnalyzer(programName);

    return subtracesConstraintsAnalyzer.analyze(new String[0]);
  }

  @Test
  public void trivial_1() throws Exception {
    String programName = BaseTrivialAdapter.PROGRAM_NAME;
    String missingConstraint = "A";
    Set<SubtraceOutcomeConstraint> subtracesOutcomeConstraint =
        getSubtracesOutcomeConstraint(programName);
    ControlFlowStatementWithMissingConstraintAnalyzer analysis =
        new ControlFlowStatementWithMissingConstraintAnalyzer(
            programName, missingConstraint, subtracesOutcomeConstraint);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    ControlFlowStatementsWithMissingConstraint write = analysis.analyze(args);

    analysis =
        new ControlFlowStatementWithMissingConstraintAnalyzer(programName, missingConstraint);
    args = new String[0];
    ControlFlowStatementsWithMissingConstraint read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void berkeleyDB_3() throws Exception {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    String missingConstraint =
        "((!DUPLICATES && SHAREDCACHE && !REPLICATED && !SEQUENTIAL && JECACHESIZE) || (!JECACHESIZE && ((DUPLICATES && ((!SHAREDCACHE && !REPLICATED && SEQUENTIAL) || (SHAREDCACHE && !REPLICATED && !SEQUENTIAL))) || (!DUPLICATES && ((SHAREDCACHE && REPLICATED && SEQUENTIAL) || (SHAREDCACHE && !REPLICATED && !SEQUENTIAL))))))";
    Set<SubtraceOutcomeConstraint> subtracesOutcomeConstraint =
        getSubtracesOutcomeConstraint(programName);
    ControlFlowStatementWithMissingConstraintAnalyzer analysis =
        new ControlFlowStatementWithMissingConstraintAnalyzer(
            programName, missingConstraint, subtracesOutcomeConstraint);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    ControlFlowStatementsWithMissingConstraint write = analysis.analyze(args);

    analysis =
        new ControlFlowStatementWithMissingConstraintAnalyzer(programName, missingConstraint);
    args = new String[0];
    ControlFlowStatementsWithMissingConstraint read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void berkeleyDB_2() throws Exception {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    String missingConstraint =
        "((JECACHESIZE && ((SHAREDCACHE && REPLICATED && !SEQUENTIAL && DUPLICATES) || (!SHAREDCACHE && REPLICATED && !SEQUENTIAL && !DUPLICATES))) || (DUPLICATES && ((SHAREDCACHE && !REPLICATED && SEQUENTIAL) || (SHAREDCACHE && REPLICATED && !SEQUENTIAL)) && !JECACHESIZE))";
    Set<SubtraceOutcomeConstraint> subtracesOutcomeConstraint =
        getSubtracesOutcomeConstraint(programName);
    ControlFlowStatementWithMissingConstraintAnalyzer analysis =
        new ControlFlowStatementWithMissingConstraintAnalyzer(
            programName, missingConstraint, subtracesOutcomeConstraint);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    ControlFlowStatementsWithMissingConstraint write = analysis.analyze(args);

    analysis =
        new ControlFlowStatementWithMissingConstraintAnalyzer(programName, missingConstraint);
    args = new String[0];
    ControlFlowStatementsWithMissingConstraint read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void berkeleyDB_1() throws Exception {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    String missingConstraint = "(!DUPLICATES && SEQUENTIAL)";
    Set<SubtraceOutcomeConstraint> subtracesOutcomeConstraint =
        getSubtracesOutcomeConstraint(programName);
    ControlFlowStatementWithMissingConstraintAnalyzer analysis =
        new ControlFlowStatementWithMissingConstraintAnalyzer(
            programName, missingConstraint, subtracesOutcomeConstraint);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    ControlFlowStatementsWithMissingConstraint write = analysis.analyze(args);

    analysis =
        new ControlFlowStatementWithMissingConstraintAnalyzer(programName, missingConstraint);
    args = new String[0];
    ControlFlowStatementsWithMissingConstraint read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void lucene_1() throws Exception {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    String missingConstraint =
        "((READER_POOLING && ((COMMIT_ON_CLOSE && !CHECK_PENDING_FLUSH_UPDATE && MERGE_SCHEDULER) || (COMMIT_ON_CLOSE && CHECK_PENDING_FLUSH_UPDATE && !MERGE_SCHEDULER))) || (MERGE_SCHEDULER && CHECK_PENDING_FLUSH_UPDATE && !READER_POOLING))";
    Set<SubtraceOutcomeConstraint> subtracesOutcomeConstraint =
        getSubtracesOutcomeConstraint(programName);
    ControlFlowStatementWithMissingConstraintAnalyzer analysis =
        new ControlFlowStatementWithMissingConstraintAnalyzer(
            programName, missingConstraint, subtracesOutcomeConstraint);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    ControlFlowStatementsWithMissingConstraint write = analysis.analyze(args);

    analysis =
        new ControlFlowStatementWithMissingConstraintAnalyzer(programName, missingConstraint);
    args = new String[0];
    ControlFlowStatementsWithMissingConstraint read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void lucene_2() throws Exception {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    String missingConstraint =
        "((READER_POOLING && ((COMMIT_ON_CLOSE && CHECK_PENDING_FLUSH_UPDATE && MERGE_SCHEDULER) || (COMMIT_ON_CLOSE && !CHECK_PENDING_FLUSH_UPDATE && !MERGE_SCHEDULER))) || (!READER_POOLING && COMMIT_ON_CLOSE))";
    Set<SubtraceOutcomeConstraint> subtracesOutcomeConstraint =
        getSubtracesOutcomeConstraint(programName);
    ControlFlowStatementWithMissingConstraintAnalyzer analysis =
        new ControlFlowStatementWithMissingConstraintAnalyzer(
            programName, missingConstraint, subtracesOutcomeConstraint);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    ControlFlowStatementsWithMissingConstraint write = analysis.analyze(args);

    analysis =
        new ControlFlowStatementWithMissingConstraintAnalyzer(programName, missingConstraint);
    args = new String[0];
    ControlFlowStatementsWithMissingConstraint read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void lucene_3() throws Exception {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    String missingConstraint =
        "((COMMIT_ON_CLOSE && !CHECK_PENDING_FLUSH_UPDATE && MERGE_SCHEDULER && READER_POOLING) || (COMMIT_ON_CLOSE && !CHECK_PENDING_FLUSH_UPDATE && !READER_POOLING))";
    Set<SubtraceOutcomeConstraint> subtracesOutcomeConstraint =
        getSubtracesOutcomeConstraint(programName);
    ControlFlowStatementWithMissingConstraintAnalyzer analysis =
        new ControlFlowStatementWithMissingConstraintAnalyzer(
            programName, missingConstraint, subtracesOutcomeConstraint);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    ControlFlowStatementsWithMissingConstraint write = analysis.analyze(args);

    analysis =
        new ControlFlowStatementWithMissingConstraintAnalyzer(programName, missingConstraint);
    args = new String[0];
    ControlFlowStatementsWithMissingConstraint read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void lucene_4() throws Exception {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    String missingConstraint =
        "((READER_POOLING && ((COMMIT_ON_CLOSE && CHECK_PENDING_FLUSH_UPDATE && MERGE_SCHEDULER) || (!MERGE_SCHEDULER && COMMIT_ON_CLOSE))) || (COMMIT_ON_CLOSE && CHECK_PENDING_FLUSH_UPDATE && !READER_POOLING))";
    Set<SubtraceOutcomeConstraint> subtracesOutcomeConstraint =
        getSubtracesOutcomeConstraint(programName);
    ControlFlowStatementWithMissingConstraintAnalyzer analysis =
        new ControlFlowStatementWithMissingConstraintAnalyzer(
            programName, missingConstraint, subtracesOutcomeConstraint);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    ControlFlowStatementsWithMissingConstraint write = analysis.analyze(args);

    analysis =
        new ControlFlowStatementWithMissingConstraintAnalyzer(programName, missingConstraint);
    args = new String[0];
    ControlFlowStatementsWithMissingConstraint read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void lucene_5() throws Exception {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    String missingConstraint =
        "((COMMIT_ON_CLOSE && !CHECK_PENDING_FLUSH_UPDATE && !MERGE_SCHEDULER && READER_POOLING) || (!READER_POOLING && ((!COMMIT_ON_CLOSE && !CHECK_PENDING_FLUSH_UPDATE && MERGE_SCHEDULER) || (!MERGE_SCHEDULER && (!COMMIT_ON_CLOSE || CHECK_PENDING_FLUSH_UPDATE)))))";
    Set<SubtraceOutcomeConstraint> subtracesOutcomeConstraint =
        getSubtracesOutcomeConstraint(programName);
    ControlFlowStatementWithMissingConstraintAnalyzer analysis =
        new ControlFlowStatementWithMissingConstraintAnalyzer(
            programName, missingConstraint, subtracesOutcomeConstraint);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    ControlFlowStatementsWithMissingConstraint write = analysis.analyze(args);

    analysis =
        new ControlFlowStatementWithMissingConstraintAnalyzer(programName, missingConstraint);
    args = new String[0];
    ControlFlowStatementsWithMissingConstraint read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void lucene_6() throws Exception {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    String missingConstraint =
        "((COMMIT_ON_CLOSE && !CHECK_PENDING_FLUSH_UPDATE && !MERGE_SCHEDULER && READER_POOLING) || (!COMMIT_ON_CLOSE && !CHECK_PENDING_FLUSH_UPDATE && !READER_POOLING))";
    Set<SubtraceOutcomeConstraint> subtracesOutcomeConstraint =
        getSubtracesOutcomeConstraint(programName);
    ControlFlowStatementWithMissingConstraintAnalyzer analysis =
        new ControlFlowStatementWithMissingConstraintAnalyzer(
            programName, missingConstraint, subtracesOutcomeConstraint);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    ControlFlowStatementsWithMissingConstraint write = analysis.analyze(args);

    analysis =
        new ControlFlowStatementWithMissingConstraintAnalyzer(programName, missingConstraint);
    args = new String[0];
    ControlFlowStatementsWithMissingConstraint read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void lucene_7() throws Exception {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    String missingConstraint =
        "(READER_POOLING && ((COMMIT_ON_CLOSE && !CHECK_PENDING_FLUSH_UPDATE && MERGE_SCHEDULER) || (COMMIT_ON_CLOSE && CHECK_PENDING_FLUSH_UPDATE && !MERGE_SCHEDULER)))";
    Set<SubtraceOutcomeConstraint> subtracesOutcomeConstraint =
        getSubtracesOutcomeConstraint(programName);
    ControlFlowStatementWithMissingConstraintAnalyzer analysis =
        new ControlFlowStatementWithMissingConstraintAnalyzer(
            programName, missingConstraint, subtracesOutcomeConstraint);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    ControlFlowStatementsWithMissingConstraint write = analysis.analyze(args);

    analysis =
        new ControlFlowStatementWithMissingConstraintAnalyzer(programName, missingConstraint);
    args = new String[0];
    ControlFlowStatementsWithMissingConstraint read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }
}
