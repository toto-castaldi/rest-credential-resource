#!/bin/bash

start=`date +%s`

cd "$( dirname "${BASH_SOURCE[0]}" )"

docker stop some-postgres; docker rm some-postgres; docker run -p 5432:5432 --name some-postgres -e POSTGRES_PASSWORD=developer -e POSTGRES_USER=developer -e POSTGRES_DB=developer -d postgres:9.4.5

#wait
echo "SELECT 1;" > ping.sql
printf "wait postgres start ."
status=1
while [[ status -ne 0 ]]; do
  sleep 1
  docker run -v `pwd`:/data -it --link some-postgres:postgres --rm postgres sh -c 'export PGPASSWORD=developer && export PGUSER=developer && psql -h $POSTGRES_PORT_5432_TCP_ADDR developer < /data/ping.sql > /dev/null 2>&1'
  status=$?
  printf "."
done
printf " started\n"

end=`date +%s`
runtime=$((end-start))
echo "start postgres in $runtime seconds"
