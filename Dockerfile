FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/rpginventory-0.0.1-SNAPSHOT.jar /app/rpginventory.jar

ENTRYPOINT ["java", "-jar", "/app/rpginventory.jar"]
