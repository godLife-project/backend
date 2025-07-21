FROM openjdk:17-jdk-slim
WORKDIR /app
COPY build/libs/project-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "app.jar"]