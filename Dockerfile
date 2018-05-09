FROM openjdk:8-jdk-alpine
ENV PORT 8000
EXPOSE 8000
COPY build/libs/*.jar /opt/app.jar
RUN git clone git@localhost:ci-gitea/config-emailer.git
WORKDIR /opt
CMD ["java", "-jar", "app.jar"]
