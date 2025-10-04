FROM openjdk:21
WORKDIR /social-network-app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app_run.jar
ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["java","-jar","app_run.jar"]