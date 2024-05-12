
FROM maven:3.9.3-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-jammy
COPY /home/.aws/credentials ~/.aws/credentials
COPY /home/.aws/config ~/.aws/config
WORKDIR /app
COPY --from=build /app/target/*.jar /app/app.jar

EXPOSE 7200
ENTRYPOINT ["java", "-jar", "app.jar"]