FROM alpine/git as clone
WORKDIR /app
RUN git clone https://github.com/ajitsangwan2006/LNDWebProject.git
FROM maven:3.5-jdk-8-alpine as build
WORKDIR /app/LNDWebProject
RUN mvn install
FROM openjdk:8-jre-alpine
WORKDIR /app/LNDWebProject
COPY --from=build /app/LNDWebProject/rest/target/rest-1.0-SNAPSHOT.jar /app/LNDWebProject
CMD [“java -jar rest-1.0-SNAPSHOT.jar”]
