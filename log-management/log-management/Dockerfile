# syntax=docker/dockerfile:1

FROM openjdk:11-jdk
ADD /target/log-management-0.0.1-SNAPSHOT.jar .
EXPOSE 1111
CMD java -jar log-management-0.0.1-SNAPSHOT.jar --envname=prod