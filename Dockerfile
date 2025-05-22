FROM maven:3.9.8-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml .
COPY api/pom.xml api/
COPY data/pom.xml data/
COPY client/pom.xml client/
COPY service/pom.xml service/
COPY auth/pom.xml auth/
COPY updater/pom.xml updater/

RUN mvn dependency:go-offline -B

COPY src src
COPY api/src api/src
COPY data/src data/src
COPY client/src client/src
COPY service/src service/src
COPY auth/src auth/src
COPY updater/src updater/src

RUN mvn clean package -DskipTests -pl api -am

FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=builder /app/api/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"] 