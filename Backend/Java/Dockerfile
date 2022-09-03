# Build container

FROM maven:3.8.6-jdk-11 as builder

WORKDIR /app

COPY uploads uploads
COPY src src
COPY pom.xml .

RUN mvn -f ./pom.xml clean package -Dmaven.test.skip=true

# End build


# Run container

FROM openjdk:11-jre-slim

WORKDIR /usr/app

COPY uploads uploads
COPY --from=builder /app/target/Java_Backend_ADTeam1-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

# start app
CMD ["java", "-jar", "Java_Backend_ADTeam1-0.0.1-SNAPSHOT.jar"]