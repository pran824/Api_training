FROM openjdk:17
EXPOSE 10000
ADD target/dockerdemo-0.0.1-SNAPSHOT.jar dockerdemo-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/dockerdemo-0.0.1-SNAPSHOT.jar"]