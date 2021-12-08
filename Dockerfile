FROM openjdk:11
EXPOSE 8082
ADD target/market_service.jar market_service.jar
ENTRYPOINT ["java", "-jar", "/market_service.jar"]
