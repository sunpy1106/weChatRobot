FROM maven:3.8.4-jdk-8-slim as builder
RUN apt-get update && \
    apt-get upgrade -y && \
    apt-get install -y git
RUN git clone https://github.com/sunpy1106/weChatRobot.git
RUN cd weChatRobot
COPY . /src/weChatRobot
RUN mkdir -p  /usr/share/maven/ref
COPY ./docker/settings-docker.xml /usr/share/maven/ref/
WORKDIR /src/weChatRobot
RUN mvn package -s /usr/share/maven/ref/settings-docker.xml dependency:resolve

FROM openjdk:8-alpine

USER root

COPY --from=builder /src/weChatRobot/robot-web/target/weChatRobot.jar /weChatRobot/
COPY ./docker/start.sh /weChatRobot/

RUN chmod +x /weChatRobot/start.sh

EXPOSE 8080
WORKDIR /weChatRobot
ENTRYPOINT [ "sh", "start.sh" ]
