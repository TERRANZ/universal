FROM openjdk:8-jre-slim
RUN mkdir /app
EXPOSE 12345 12346
COPY build/libs/*.jar /app/app.jar
ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/app.jar"]