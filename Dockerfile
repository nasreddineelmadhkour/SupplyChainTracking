ARG NEXUS_URL=http://172.17.20.244/:8081/repository/maven-releases/com/pgsintl/SupplyChainTracking/2.2/supplychaintracking-2.2.jar
RUN wget -O /supplychaintracking-2.2.jar $NEXUS_URL
CMD ["java", "-jar", "/supplychaintracking-2.2.jar"]
EXPOSE 8085
