FROM openjdk:17
COPY target/*.jar  clearSelutions.jar
ENTRYPOINT ["java", "-jar", "clearSelutions.jar"]