FROM openjdk:8-jdk-alpine
COPY target/Library-0.0.1-SNAPSHOT.jar Library-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/Library-0.0.1-SNAPSHOT.jar"]