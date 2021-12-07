FROM openjdk:11
EXPOSE 8083
ADD target/market_service.jar market_service.jar
ENTRYPOINT ["java", "-jar", "/market_service.jar"]