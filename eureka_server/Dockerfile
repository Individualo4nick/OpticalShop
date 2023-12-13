FROM eclipse-temurin:18-jdk
COPY /build/libs/eureka_server-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 80
ENTRYPOINT ["java","-jar","/app.jar"]