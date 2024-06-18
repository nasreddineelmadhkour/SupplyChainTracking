FROM openjdk:17

ARG NEXUS_URL=http://172.17.20.244:8081/repository/maven-releases/com/pgsintl/SupplyChainTracking/2.2/SupplyChainTracking-2.2.jar

# Use RUN with curl to download the JAR file
RUN apt-get update && apt-get install -y curl \
    && curl -o /SupplyChainTracking-2.2.jar $NEXUS_URL

CMD ["java", "-jar", "/SupplyChainTracking-2.2.jar"]

EXPOSE 8085
