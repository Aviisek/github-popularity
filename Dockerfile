FROM gradle:jdk25 AS builder

ENV BUILDER_HOME=/home/builder

RUN useradd -ms /bin/bash -m -d $BUILDER_HOME builder
USER builder

COPY --chown=builder:builder . $BUILDER_HOME
WORKDIR $BUILDER_HOME
RUN ./gradlew clean build

FROM eclipse-temurin:25-jre-alpine

WORKDIR /app
COPY --from=builder /home/builder/build/libs/github-popularity-0.0.1.jar /app/github-popularity.jar
EXPOSE 8080
ENTRYPOINT ["java", "-XX:UseSVE=0", "-jar", "/app/github-popularity.jar"]
