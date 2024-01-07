# Stage 1: Build the application
FROM openjdk:17-jdk-alpine as builder

WORKDIR /app

RUN apk --update add findutils

COPY build.gradle .
COPY settings.gradle .
COPY gradlew .
COPY gradle/ gradle/

COPY src/ src/
#COPY . .

RUN ./gradlew build

EXPOSE 8080

CMD ["java", "-jar", "build/libs/fitness-center-0.0.1-SNAPSHOT.jar"]
