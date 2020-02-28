#!/usr/bin/env bash

NULL="null"
PROGRAMS="indexFilesLarge"
SpecificIDTA="specificIDTA"
BASE=$(pwd)

cd "$BASE"/../ || exit

program=$NULL
approach=$NULL

run() {
  local program=$1
  local approach=$2

  echo ""
  echo "Running" "$program" "$approach"
  echo ""

  if [ "$approach" == $SpecificIDTA ]; then
    mvn test -Dtest=edu.cmu.cs.mvelezce.explorer.idta.SpecificConfigsIDTATest#"$program"
  fi

  echo ""
  echo "Done with" "$program" "$approach"
  echo ""

  cd .. || exit
}

for entry in "$@"; do
  if [[ $PROGRAMS =~ (^|[[:space:]])$entry($|[[:space:]]) ]]; then
    program=$entry
    approach=$NULL

    continue
  else
    approach=$entry
  fi

  if [ "$program" == $NULL ]; then
    echo "The program is null"

    continue
  fi

  if [ "$approach" == $NULL ]; then
    echo "The approach is null"

    continue
  fi

  if [ "$approach" != $SpecificIDTA ]; then
    echo "Could not find approach" "$approach"
    program=$NULL
    approach=$NULL
  else
    run $program $approach
  fi

done
