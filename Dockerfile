# Use a Java runtime as a parent image
FROM openjdk:11-jre-slim

# Set the working directory to /app
WORKDIR /app

# Copy the WAR file from the build context to the container
COPY target/myapp.war .

# Expose port 8080 for the web application
EXPOSE 8080

# Set the command to run when the container starts
CMD ["java", "-jar", "myapp.war"]
