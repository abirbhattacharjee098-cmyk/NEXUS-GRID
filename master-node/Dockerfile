FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY common-lib common-lib
COPY master-node master-node
COPY worker-node worker-node
RUN mvn clean package -pl common-lib,master-node -am -DskipTests

FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp
COPY --from=build /app/master-node/target/master-node-1.0.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
