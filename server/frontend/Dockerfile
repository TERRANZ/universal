FROM anapsix/alpine-java
VOLUME /tmp
ENV JAVA_OPTS="-Xmx256m"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
ADD "target/frontend-1-jar-with-dependencies.jar" "/app.jar"
EXPOSE 12345
EXPOSE 12346