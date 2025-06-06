
FROM eclipse-temurin:24-jdk-alpine
WORKDIR /devicehub
EXPOSE 8080
COPY target/devicehub-0.0.1-SNAPSHOT.jar /devicehub/app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]