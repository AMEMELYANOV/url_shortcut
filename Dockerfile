FROM maven:3.8-openjdk-17 as maven
WORKDIR /app
COPY . /app
RUN mvn package -DskipTests

FROM openjdk:17.0.2-jdk
WORKDIR /app
COPY --from=maven /app/target/shortcut.jar app.jar
CMD java -jar app.jar
