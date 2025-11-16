FROM openjdk:17.0.1-jdk-slim

WORKDIR /app


COPY target/vinconomy-api.jar vinconomy_server.jar

# Actually useless - just to telegraph what port is being utilized by the service
EXPOSE 8080

ENTRYPOINT ["java","-jar","vinconomy_server.jar"]