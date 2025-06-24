# Use a smaller base image for the final application, like Temurin JRE
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Set the working directory for the application inside the container
WORKDIR /app

# Copy the built JAR file from the 'build' stage
COPY ./target/*.jar app.jar

COPY ./src/scripts/startApp.sh startApp.sh
COPY ./src/scripts/startApp.sh setEnv.sh
RUN exec chmod 755 startApp.sh
RUN exec chmod 755 setEnv.sh

# Expose the port on which the Spring Boot application runs (default 8080)
EXPOSE 8080

# Set the entry point to run the Spring Boot application
ENTRYPOINT ["./startApp.sh"]
#CMD ["sh", "-c", "tail -f /dev/null"]
