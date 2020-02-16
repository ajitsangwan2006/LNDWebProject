FROM adoptopenjdk/maven-openjdk11-openj9

#maintainer
MAINTAINER ajitsangwan2006@gmail.com

# install git
RUN apt-get update && \
    apt-get upgrade -y && \
    apt-get install -y git

WORKDIR /app
RUN git clone https://github.com/ajitsangwan2006/LNDWebProject.git

WORKDIR /app/LNDWebProject
RUN mvn package

#expose port 8080
EXPOSE 8080
CMD ["java", "-jar", "/app/LNDWebProject/rest/target/rest-1.0-SNAPSHOT.jar"]

