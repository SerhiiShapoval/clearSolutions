FROM openjdk:17-alpine

COPY target/*.jar clearsolutions.jar

ENTRYPOINT ["java", "-jar", "clearsolutions.jar"]