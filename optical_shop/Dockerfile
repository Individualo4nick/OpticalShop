FROM amazoncorretto:17
RUN mkdir -p /app/images
COPY /build/libs/optical_shop-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8888
RUN chown -R 1000:1000 /app/images
ENTRYPOINT ["java", "-jar", "/app.jar"]