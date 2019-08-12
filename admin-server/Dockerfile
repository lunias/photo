FROM maven:3.6-jdk-12 AS build
COPY . /home/photo
RUN mvn -f /home/photo/admin-server/pom.xml clean package

FROM adoptopenjdk/openjdk12:jdk-12.0.1_12-slim
COPY --from=build /home/photo/admin-server/target/admin-server-0.0.1-SNAPSHOT.jar /usr/local/lib/admin-server.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","/usr/local/lib/admin-server.jar"]
