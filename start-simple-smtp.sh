#!/bin/bash

start=`date +%s`

cd "$( dirname "${BASH_SOURCE[0]}" )"

docker stop some-com-github-toto-castaldi-simple-smtp-server; docker rm some-com-github-toto-castaldi-simple-smtp-server; docker run --name some-com-github-toto-castaldi-simple-smtp-server -v `pwd`:/data -p 7000:8080 -p 2525:2525 -d com-github-toto-castaldi-simple-smtp-server
#wait
printf "wait simple smtp start ."
status=1
while [[ status -ne 200 ]]; do
  sleep 1
  status=$(curl -X GET -H "Accept: application/json" -H "Cache-Control: no-cache" --write-out %{http_code} --silent --output /dev/null "http://localhost:7000/smtp/status" )
  printf "."
done
printf " started\n"

end=`date +%s`
runtime=$((end-start))
echo "start simple smtp in $runtime seconds"
