FROM maven:3.6.0-jdk-8-slim as build
COPY . /src/weChatRobot
COPY ./settings-docker.xml /src/weChatRobot/
WORKDIR /src/weChatRobot

RUN mvn -s /src/weChatRobot/settings-docker.xml clean package

FROM openjdk:8-alpine

USER root

COPY --from=build /src/weChatRobot/robot-web/target/weChatRobot.jar /weChatRobot/
COPY start.sh /weChatRobot/

RUN chmod +x /weChatRobot/start.sh

EXPOSE 80
WORKDIR /weChatRobot
ENTRYPOINT [ "sh", "start.sh" ]
