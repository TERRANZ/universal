FROM eclipse-temurin:21-jre
RUN mkdir /app
EXPOSE 12345 12346
COPY build/libs/*.jar /app/app.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/app.jar"]
