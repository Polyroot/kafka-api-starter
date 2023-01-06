FROM registry.ahml.ru/elka/ci-cd/liberica-openjdk-alpine-musl:17.0.1-12
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]