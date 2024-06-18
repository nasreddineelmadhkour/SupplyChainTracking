ARG NEXUS_URL=http://192.168.33.10:8081/repository/maven-releases/tn/esprit/SupplyChainTracking/2.2/SupplyChainTracking-2.2.jar
RUN wget -O /SupplyChainTracking-2.2.jar $NEXUS_URL
CMD ["java", "-jar", "/SupplyChainTracking-2.2.jar"]
EXPOSE 8085
