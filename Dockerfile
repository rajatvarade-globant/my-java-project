FROM maven:3.8.3-jdk-11-slim AS build
WORKDIR /app
COPY . .
RUN mvn clean package

FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=build /app/target/my-java-project-1.0-SNAPSHOT.jar .
CMD ["java", "-jar", "my-java-project-1.0-SNAPSHOT.jar"]
