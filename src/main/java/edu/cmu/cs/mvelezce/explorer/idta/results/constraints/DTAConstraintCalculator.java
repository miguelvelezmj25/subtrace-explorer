package edu.cmu.cs.mvelezce.explorer.idta.results.constraints;

import de.fosd.typechef.featureexpr.FeatureExpr;
import de.fosd.typechef.featureexpr.sat.*;
import edu.cmu.cs.mvelezce.explorer.idta.IDTA;
import edu.cmu.cs.mvelezce.explorer.idta.constraint.Constraint;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partitioning;
import edu.cmu.cs.mvelezce.explorer.utils.ConstraintUtils;
import org.sat4j.core.Vec;
import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.IVec;
import org.sat4j.specs.TimeoutException;
import scala.collection.Iterator;
import scala.collection.JavaConverters;
import scala.collection.JavaConverters$;

import java.util.*;

public final class DTAConstraintCalculator {

  private static final Set<Constraint> CONSTRAINTS_EXPLORED = new HashSet<>();

  private final ConstraintExplorator constraintExplorator;

  public DTAConstraintCalculator(Collection<String> options) {
    if (IDTA.USE_BDD) {
      this.constraintExplorator = new BDDConstraintExplorator(options);
    } else {
      this.constraintExplorator = new SATConstraintExplorator();
    }
  }

  public static Set<Constraint> deriveIDTAConstraints(Collection<Partitioning> partitionings) {
    long start = System.nanoTime();
    Set<Partition> allPartitions = getAllPartitions(partitionings);
    Set<Constraint> constraints = new HashSet<>();

    for (Partition partition : allPartitions) {
      constraints.add(new Constraint(partition.getFeatureExpr()));
    }

    long end = System.nanoTime();
    System.out.println("Derive partitions: " + (end - start) / 1E9);

    return constraints;
  }

  private static Set<Partition> getAllPartitions(Collection<Partitioning> partitionings) {
    Set<Partition> partitions = new HashSet<>();
    for (Partitioning partitioning : partitionings) {
      partitions.addAll(partitioning.getPartitions());
    }

    return partitions;
  }

  public Set<Constraint> getConstraintsToExplore(
      Set<Constraint> executedConfigs, Set<Constraint> currentConstraints) {
    currentConstraints.removeAll(CONSTRAINTS_EXPLORED);
    System.out.println("Explored " + executedConfigs.size());
    System.out.println("Current " + currentConstraints.size());

    long start = System.nanoTime();
    Set<Constraint> constraintsToExplore =
        this.constraintExplorator.calculate(executedConfigs, currentConstraints);
    long end = System.nanoTime();
    System.out.println("Constraints to explore: " + (end - start) / 1E9);

    return constraintsToExplore;
  }

  private interface ConstraintExplorator {
    Set<Constraint> calculate(Set<Constraint> executedConfigs, Set<Constraint> currentConstraints);
  }

  private static class BDDConstraintExplorator implements ConstraintExplorator {
    private final Collection<String> options;

    public BDDConstraintExplorator(Collection<String> options) {
      this.options = options;
    }

    @Override
    public Set<Constraint> calculate(
        Set<Constraint> executedConfigs, Set<Constraint> currentConstraints) {
      Set<Constraint> exploredConstraints = new HashSet<>();

      for (Constraint executedConfig : executedConfigs) {
        scala.collection.immutable.Set<String> config =
            JavaConverters.asScalaSet(
                    ConstraintUtils.toConfig(executedConfig.getFeatureExpr(), this.options))
                .toSet();

        for (Constraint currentConstraint : currentConstraints) {
          if (exploredConstraints.contains(currentConstraint)) {
            continue;
          }

          if (currentConstraint.getFeatureExpr().evaluate(config)) {
            exploredConstraints.add(currentConstraint);
            CONSTRAINTS_EXPLORED.add(currentConstraint);
          }
        }
      }

      Set<Constraint> constraintsToExplore = new HashSet<>(currentConstraints);
      constraintsToExplore.removeAll(exploredConstraints);

      return constraintsToExplore;
    }
  }

  private static class SATConstraintExplorator implements ConstraintExplorator {
    private static final Map<Constraint, Set<Constraint>> CONSTRAINTS_TO_UNSAT_CONFIGS =
        new HashMap<>();
    private static final Map<FeatureExpr, ISolver> SOLVERS = new HashMap<>();
    private static final Map<FeatureExpr, IVec> ALL_CLAUSES = new HashMap<>();
    private static final Map<FeatureExpr, Map<String, Integer>> NAMES_TO_INTS = new HashMap<>();

    @Override
    public Set<Constraint> calculate(
        Set<Constraint> executedConfigs, Set<Constraint> currentConstraints) {
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

      System.out.println("Sat Inner loop " + (time / 1E9));

      return constraintsToExplore;
    }

    private void clearSatConstraint(Constraint currentConstraint) {
      CONSTRAINTS_EXPLORED.add(currentConstraint);
      SOLVERS.remove(currentConstraint.getFeatureExpr());
      ALL_CLAUSES.remove(currentConstraint.getFeatureExpr());
      NAMES_TO_INTS.remove(currentConstraint.getFeatureExpr());
      CONSTRAINTS_TO_UNSAT_CONFIGS.remove(currentConstraint);
    }

    private ISolver getSolverWithClauses(
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

    private Map<String, Integer> getNamesToInts(FeatureExpr currentConstraint) {
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

    private IVec getAllClauses(FeatureExpr currentConstraint, Map<String, Object> namesToInts) {
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

    private VecInt getExecutedAssumptions(
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

    private <K, V> scala.collection.immutable.Map<K, V> convertToImmutableScalaMap(Map<K, V> m) {
      return JavaConverters$.MODULE$
          .mapAsScalaMapConverter(m)
          .asScala()
          .toMap(scala.Predef$.MODULE$.conforms());
    }
  }
}
