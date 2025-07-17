FROM openjdk:17.0.2
WORKDIR /app
COPY build/libs/project-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]