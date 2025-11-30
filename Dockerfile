# Build stage
FROM gradle:8.8-jdk17 as build
WORKDIR /home/gradle/src
COPY . .
RUN gradle build --no-daemon

# Package stage
FROM eclipse-temurin:17-jre-jammy
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
