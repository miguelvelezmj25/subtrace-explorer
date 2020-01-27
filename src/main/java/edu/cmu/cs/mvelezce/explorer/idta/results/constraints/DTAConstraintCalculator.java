package edu.cmu.cs.mvelezce.explorer.idta.results.constraints;

import de.fosd.typechef.featureexpr.FeatureExpr;
import de.fosd.typechef.featureexpr.sat.*;
import edu.cmu.cs.mvelezce.explorer.idta.constraint.Constraint;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partitioning;
import org.sat4j.core.Vec;
import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.IVec;
import org.sat4j.specs.TimeoutException;
import scala.collection.Iterator;
import scala.collection.JavaConverters$;

import java.util.*;

public final class DTAConstraintCalculator {

  private static final Map<Constraint, Set<Constraint>> CONSTRAINTS_TO_UNSAT_CONFIGS =
      new HashMap<>();
  private static final Map<FeatureExpr, ISolver> SOLVERS = new HashMap<>();
  private static final Map<FeatureExpr, IVec> ALL_CLAUSES = new HashMap<>();
  private static final Map<FeatureExpr, Map<String, Integer>> NAMES_TO_INTS = new HashMap<>();
  private static final Set<Constraint> CONSTRAINTS_EXPLORED = new HashSet<>();

  private DTAConstraintCalculator() {}

  public static Set<Constraint> deriveIDTAConstraints(Collection<Partitioning> partitionings) {
    Set<Partition> allPartitions = getAllPartitions(partitionings);
    Set<Constraint> constraints = new HashSet<>();

    for (Partition partition : allPartitions) {
      constraints.add(new Constraint(partition.getFeatureExpr()));
    }

    return constraints;
  }

  private static Set<Partition> getAllPartitions(Collection<Partitioning> partitionings) {
    Set<Partition> partitions = new HashSet<>();
    for (Partitioning partitioning : partitionings) {
      partitions.addAll(partitioning.getPartitions());
    }

    return partitions;
  }

  public static Set<Constraint> getConstraintsToExplore(
      Set<Constraint> executedConfigs, Set<Constraint> currentConstraints) {
    currentConstraints.removeAll(CONSTRAINTS_EXPLORED);
    System.out.println("Explored " + executedConfigs.size());
    System.out.println("Current " + currentConstraints.size());
    Set<Constraint> constraintsToExplore = new HashSet<>();

    long time = 0;

    for (Constraint currentConstraint : currentConstraints) {
      boolean explored = false;

      long start = System.nanoTime();
      Map<String, Integer> namesToInts = getNamesToInts(currentConstraint.getFeatureExpr());
      ISolver solver = getSolverWithClauses(currentConstraint.getFeatureExpr(), namesToInts);
      long end = System.nanoTime();
      time += (end - start);

      CONSTRAINTS_TO_UNSAT_CONFIGS.putIfAbsent(currentConstraint, new HashSet<>());
      Set<Constraint> unsatConfigs = CONSTRAINTS_TO_UNSAT_CONFIGS.get(currentConstraint);

      for (Constraint executedConfig : executedConfigs) {
        if (unsatConfigs.contains(executedConfig)) {
          continue;
        }

        VecInt assumptions =
            getExecutedAssumptions(new HashMap<>(namesToInts), executedConfig.getFeatureExpr());

        boolean isSat;

        try {
          isSat = solver.isSatisfiable(assumptions);
        } catch (TimeoutException te) {
          throw new RuntimeException(te);
        }

        if (isSat) {
          explored = true;
          clearSatConstraint(currentConstraint);

          break;
        } else {
          unsatConfigs.add(executedConfig);
        }
      }

      if (!explored) {
        constraintsToExplore.add(currentConstraint);
      }
    }

    System.out.println("Inner loop " + (time / 1E9));

    return constraintsToExplore;
  }

  private static void clearSatConstraint(Constraint currentConstraint) {
    CONSTRAINTS_EXPLORED.add(currentConstraint);
    SOLVERS.remove(currentConstraint.getFeatureExpr());
    ALL_CLAUSES.remove(currentConstraint.getFeatureExpr());
    NAMES_TO_INTS.remove(currentConstraint.getFeatureExpr());
    CONSTRAINTS_TO_UNSAT_CONFIGS.remove(currentConstraint);
  }

  private static ISolver getSolverWithClauses(
      FeatureExpr currentConstraint, Map<String, Integer> namesToInts) {
    ISolver solver = SOLVERS.get(currentConstraint);

    if (solver != null) {
      return solver;
    }

    solver = SolverFactory.newDefault();
    IVec allClauses = getAllClauses(currentConstraint, new HashMap<>(namesToInts));

    try {
      solver.addAllClauses(allClauses);
    } catch (ContradictionException ce) {
      throw new RuntimeException(ce);
    }

    SOLVERS.put(currentConstraint, solver);

    return solver;
  }

  private static Map<String, Integer> getNamesToInts(FeatureExpr currentConstraint) {
    Map<String, Integer> namesToInts = NAMES_TO_INTS.get(currentConstraint);

    if (namesToInts != null) {
      return namesToInts;
    }

    namesToInts = new HashMap<>();
    NAMES_TO_INTS.put(currentConstraint, namesToInts);
    int lastId = 0;
    Iterator<SATFeatureExpr> clauses =
        CNFHelper.getCNFClauses((SATFeatureExpr) currentConstraint).toIterator();

    while (clauses.hasNext()) {
      SATFeatureExpr clause = clauses.next();
      Iterator<DefinedExpr> literals = CNFHelper.getDefinedExprs(clause).iterator();

      while (literals.hasNext()) {
        DefinedExpr literal = literals.next();

        if (!namesToInts.containsKey(literal.satName())) {
          lastId++;
          namesToInts.put(literal.satName(), lastId);
        }
      }
    }

    return namesToInts;
  }

  private static IVec getAllClauses(
      FeatureExpr currentConstraint, Map<String, Object> namesToInts) {
    IVec iVec = ALL_CLAUSES.get(currentConstraint);

    if (iVec != null) {
      return iVec;
    }

    iVec = new Vec();
    scala.collection.immutable.Map<String, Object> scalaNamesToInts =
        convertToImmutableScalaMap(namesToInts);
    Iterator<SATFeatureExpr> clauses =
        CNFHelper.getCNFClauses((SATFeatureExpr) currentConstraint).toIterator();

    while (clauses.hasNext()) {
      SATFeatureExpr clause = clauses.next();
      VecInt clauseVec = SatSolver.getClauseVec(scalaNamesToInts, clause);
      iVec.push(clauseVec);
    }

    ALL_CLAUSES.put(currentConstraint, iVec);

    return iVec;
  }

  private static VecInt getExecutedAssumptions(
      Map<String, Object> namesToInts, FeatureExpr exploredConstraint) {
    VecInt assumptions = new VecInt();
    Iterator<SATFeatureExpr> cnfClauses =
        CNFHelper.getCNFClauses((SATFeatureExpr) exploredConstraint).toIterator();

    while (cnfClauses.hasNext()) {
      SATFeatureExpr clause = cnfClauses.next();

      if (CNFHelper.isLiteral(clause)) {
        String satName = CNFHelper.getDefinedExpr(clause).satName();

        if (namesToInts.containsKey(satName)) {
          int option = (int) namesToInts.get(satName);

          if (clause instanceof Not) {
            option = -1 * option;
          }

          assumptions.push(option);
        }
      }
    }

    return assumptions;
  }

  private static <K, V> scala.collection.immutable.Map<K, V> convertToImmutableScalaMap(
      Map<K, V> m) {
    return JavaConverters$.MODULE$
        .mapAsScalaMapConverter(m)
        .asScala()
        .toMap(scala.Predef$.MODULE$.conforms());
  }
}
