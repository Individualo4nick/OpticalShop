FROM amazoncorretto:17
COPY /build/libs/gateway-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 80
ENTRYPOINT ["java","-jar","/app.jar"]