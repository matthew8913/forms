# Build static UI
FROM node:23 AS UI_STATIC
WORKDIR /app
COPY . .
WORKDIR /app/ui
RUN npm install
RUN npm fund
RUN npm run build

# Build app JAR
FROM gradle:8-jdk21-corretto AS BUILD
WORKDIR /app
COPY . .
COPY --from=UI_STATIC /app/ui/dist/assets/ /app/src/main/resources/static/assets/
COPY --from=UI_STATIC /app/ui/dist/favicon.ico /app/src/main/resources/static/
COPY --from=UI_STATIC /app/ui/dist/index.html /app/src/main/resources/static/
# First run is slow because full dependance loading takes around 5 min
RUN gradle bootJar


# Run app
FROM amazoncorretto:21-alpine-jdk
WORKDIR /app
COPY --from=BUILD /app/.env /app/
COPY --from=BUILD /app/build/libs/*.jar /app/
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "forms-0.1.jar"]
