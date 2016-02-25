#!/bin/bash

start=`date +%s`

cd "$( dirname "${BASH_SOURCE[0]}" )"

docker stop some-com-github-toto-castaldi-rest-credential-resource; docker rm some-com-github-toto-castaldi-rest-credential-resource; docker run --name some-com-github-toto-castaldi-rest-credential-resource -v `pwd`:/data --link some-postgres:postgres -p 8080:8080 -d com-github-toto-castaldi-rest-credential-resource
#wait
printf "wait credential start ."
status=1
while [[ status -ne 200 ]]; do
  sleep 1
  status=$(curl -X GET -H "Accept: application/json" -H "x-mashape-proxy-secret: setit" -H "Cache-Control: no-cache" --write-out %{http_code} --silent --output /dev/null "http://localhost:8080/credential/service" )
  printf "."
done
printf " started\n"

end=`date +%s`
runtime=$((end-start))
echo "start credential in $runtime seconds"
