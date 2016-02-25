#!/bin/bash
set -e

if [ "$1" = 'tomcat' ]; then
  export CATALINA_OPTS=-Ddb.hostname=$POSTGRES_PORT_5432_TCP_ADDR
  cp /*.war /apache-tomcat-$TOMCAT_VERSION/webapps
  cp /data/config/tomcat7/lib/*.jar /apache-tomcat-$TOMCAT_VERSION/lib
  cp /data/config/tomcat7/context.xml /apache-tomcat-$TOMCAT_VERSION/conf/context.xml
  sed -i 's/127\.0\.0\.1/\$\{db.hostname\}/g' /apache-tomcat-$TOMCAT_VERSION/conf/context.xml
  cd /apache-tomcat-$TOMCAT_VERSION/bin
  ./catalina.sh run
fi

exec "$@"
