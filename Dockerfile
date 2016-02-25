FROM ubuntu:14.04.3

ENV TOMCAT_VERSION 7.0.39

RUN apt-get -y update
RUN apt-get install software-properties-common python-software-properties -y
RUN add-apt-repository ppa:openjdk-r/ppa -y
RUN apt-get -y update
RUN apt-get install openjdk-8-jdk -y
RUN apt-get install curl -y

RUN curl -fsSL https://archive.apache.org/dist/tomcat/tomcat-7/v$TOMCAT_VERSION/bin/apache-tomcat-$TOMCAT_VERSION.tar.gz | tar xzf - -C /

COPY target/*.war /
COPY docker-entrypoint.sh /

WORKDIR /data

COPY docker-entrypoint.sh /

ENTRYPOINT ["/docker-entrypoint.sh"]

EXPOSE 8080

CMD ["tomcat"]
