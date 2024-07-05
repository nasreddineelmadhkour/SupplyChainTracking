# Use a base image with JDK and Maven installed
FROM openjdk:17

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven wrapper script and make it executable
COPY mvnw .
COPY mvnw.cmd .
COPY pom.xml .

# Change permissions to make Maven wrapper executable
RUN chmod +x mvnw

# Download the JAR file from Nexus
ARG NEXUS_URL=http://172.17.20.244:8081/repository/maven-releases/com/pgsintl/SupplyChainTracking/3.3/SupplyChainTracking-3.3.jar
RUN curl -o /SupplyChainTracking-3.3.jar $NEXUS_URL

# Expose the port your application runs on
EXPOSE 8085

# Run the application
CMD ["java", "-jar", "/SupplyChainTracking-3.3.jar"]
