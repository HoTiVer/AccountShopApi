FROM openjdk:17-jdk-alpine

ADD target/AccountShopApi.jar AccountShopApi.jar

ENTRYPOINT ["java","-jar","/AccountShopApi.jar"]