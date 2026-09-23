FROM openjdk:17-ea-11-jdk-slim
WORKDIR /app
COPY ./build/libs/dailytalk-0.0.1-SNAPSHOT.jar app.jar
ENV TZ=Asia/Seoul
CMD ["java", "-jar", "app.jar"]
EXPOSE 7713


# FROM adoptopenjdk:11-jdk-hotspot
#
# WORKDIR /app
#
# COPY ./build/libs/sFL-0.0.1-SNAPSHOT.jar app.jar
#
# CMD ["java", "-jar", "app.jar"]
#
# EXPOSE 7713