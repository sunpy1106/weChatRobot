FROM maven:3.8.4-jdk-8-slim as builder
USER root

COPY . /src/weChatRobot

WORKDIR /src/weChatRobot
RUN mvn package  dependency:resolve

FROM openjdk:8-alpine

USER root

COPY --from=builder /src/weChatRobot/robot-web/target/weChatRobot.jar /weChatRobot/
COPY start.sh /weChatRobot/

RUN chmod +x /weChatRobot/start.sh

EXPOSE 8080
WORKDIR /weChatRobot
ENTRYPOINT [ "sh", "start.sh" ]
