#!/bin/bash

set -e

start=`date +%s`

cd "$( dirname "${BASH_SOURCE[0]}" )"

./start-services.sh
cd system-integration-test
mvn clean install

end=`date +%s`
runtime=$((end-start))
echo "tests in $runtime seconds"
