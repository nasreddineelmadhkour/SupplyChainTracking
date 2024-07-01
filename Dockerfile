FROM openjdk:17

ARG NEXUS_URL=http://172.17.20.244:8081/repository/maven-releases/com/pgsintl/SupplyChainTracking/2.3/SupplyChainTracking-2.3.jar

# Use curl to download the JAR file directly
RUN curl -o /SupplyChainTracking-2.3.jar $NEXUS_URL

CMD ["java", "-jar", "/SupplyChainTracking-2.3.jar"]

EXPOSE 8085
