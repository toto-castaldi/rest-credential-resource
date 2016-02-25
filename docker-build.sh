#/bin/bash

cd "$( dirname "${BASH_SOURCE[0]}" )"

mvn clean install
docker build -t com-github-toto-castaldi-rest-credential-resource .
