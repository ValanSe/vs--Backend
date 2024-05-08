#FROM bellsoft/liberica-openjdk-alpine:17
#VOLUME /tmp
#ARG JAR_FILE=build/libs/*.jar
#COPY ${JAR_FILE} app.jar
#COPY keystore.p12 keystore.p12
#EXPOSE 8443
#ENTRYPOINT ["java", "-jar", "/app.jar"]

# Dockerfile

# JDK Image Start
FROM bellsoft/liberica-openjdk-alpine:17

# 인자 설정 - JAR_File
ARG JAR_FILE=build/libs/*.jar

# jar 파일 복제
COPY ${JAR_FILE} app.jar

# 실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]