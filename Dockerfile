# Start with a base image that has Java installed
FROM openjdk:17

LABEL maintainer="Nasreddine El Madhkour"

# Using ARG for build-time variables
ARG NEXUS_URL=http://172.17.20.244:8081/repository/maven-releases/com/pgsintl/SupplyChainTracking/2.2/SupplyChainTracking-2.2.jar

# Install curl (if not already installed)
RUN apt-get update && apt-get install -y curl

# Download the JAR file from Nexus repository using curl
RUN curl -o /SupplyChainTracking-2.2.jar $NEXUS_URL

# Define the command to run the application
CMD ["java", "-jar", "/SupplyChainTracking-2.2.jar"]

# Expose the necessary port
EXPOSE 8085

# Optional: Add a health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 CMD curl -f http://172.17.20.244:8085/actuator/health || exit 1
