#!/bin/bash
set -e

if [ "$1" = 'tomcat' ]; then
  export CATALINA_OPTS=-Ddb.hostname=$POSTGRES_PORT_5432_TCP_ADDR
  export CATALINA_OPTS=$CATALINA_OPTS' -DMAIL_PORT=2525 -DMAIL_AUTHENTICATE=false -DMAIL_SSL_ON_CONNECT=false'
  export CATALINA_OPTS=$CATALINA_OPTS' -Ddb.username='
  export CATALINA_OPTS=$CATALINA_OPTS$POSTGRES_ENV_POSTGRES_USER' -Ddb.password='
  export CATALINA_OPTS=$CATALINA_OPTS$POSTGRES_ENV_POSTGRES_PASSWORD' -Ddb.port=5432 -Ddb.schema='
  export CATALINA_OPTS=$CATALINA_OPTS$POSTGRES_ENV_POSTGRES_DB' -DMAIL_HOST='
  export CATALINA_OPTS=$CATALINA_OPTS$COM_GITHUB_TOTO_CASTALDI_SIMPLE_SMTP_SERVER_PORT_2525_TCP_ADDR

  cp /*.war /apache-tomcat-$TOMCAT_VERSION/webapps
  cp /data/config/tomcat7/lib/*.jar /apache-tomcat-$TOMCAT_VERSION/lib
  cp /data/config/tomcat7/context.xml /apache-tomcat-$TOMCAT_VERSION/conf/context.xml
  #sed -i 's/127\.0\.0\.1/\$\{db.hostname\}/g' /apache-tomcat-$TOMCAT_VERSION/conf/context.xml
  cd /apache-tomcat-$TOMCAT_VERSION/bin
  ./catalina.sh run
fi

exec "$@"
