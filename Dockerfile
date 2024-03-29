ARG JAVA_VERSION=15
FROM openjdk:${JAVA_VERSION}
COPY target/ees-intranet-ms-users.jar ees-intranet-ms-users.jar
EXPOSE 9190
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","/ees-intranet-ms-users.jar"]