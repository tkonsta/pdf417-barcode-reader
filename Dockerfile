FROM adoptopenjdk/openjdk11:alpine-slim
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
