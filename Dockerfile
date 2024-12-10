# Build stage
FROM gradle:8-jdk21-corretto AS BUILD
WORKDIR /app
COPY . .
# First run is slow because full dependance loading takes around 5 min
RUN gradle bootJar


# Run stage
FROM amazoncorretto:21-alpine-jdk
WORKDIR /app
COPY --from=BUILD /app/.env /app/
COPY --from=BUILD /app/build/libs/*.jar /app/
ENTRYPOINT ["java","-jar", "forms-0.1.jar"]
