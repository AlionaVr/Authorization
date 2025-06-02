FROM eclipse-temurin:21-jdk-alpine

EXPOSE 8080

COPY build/libs/Autorization-0.0.1-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "app.jar"]