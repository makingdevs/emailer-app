FROM openjdk:8-jdk-alpine
ENV PORT 8000
EXPOSE 8000
COPY build/libs/*.jar /opt/app.jar
COPY .config/config-emailer/config.json /opt/
WORKDIR /opt
CMD ["java", "-jar", "app.jar"]
