FROM java:8
COPY ./target/seckill-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar", "/app/app.jar"]
