FROM openjdk-21-slim
WORKDIR /app
COPY target/*.jar ./app.jar
CMD ["java", "-jar", "app.jar"]