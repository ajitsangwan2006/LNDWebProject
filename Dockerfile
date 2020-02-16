FROM alpine/git

#maintainer
MAINTAINER ajitsangwan2006@gmail.com

WORKDIR /app
RUN git clone https://github.com/ajitsangwan2006/LNDWebProject.git

FROM maven:3.5.2-jdk-11-alpine
WORKDIR /app/LNDWebProject
RUN mvn install
RUN javac rest/src/com/app/Main.java
#expose port 8080
EXPOSE 8080
CMD ["java", "rest/src/com/app/Main"]