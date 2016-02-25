#!/bin/bash

start=`date +%s`

cd "$( dirname "${BASH_SOURCE[0]}" )"

./start-postgresql.sh
./start-credential.sh

end=`date +%s`
runtime=$((end-start))
echo "start in $runtime seconds"
