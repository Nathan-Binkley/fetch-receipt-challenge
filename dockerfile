FROM openjdk:21-oracle AS build
RUN mkdir /opt/app
COPY . /opt/app
WORKDIR /opt/app
RUN microdnf install findutils
ENV deployment "github"
RUN chmod +x ./gradlew
RUN ./gradlew build

FROM openjdk:21-oracle
RUN mkdir /opt/app
WORKDIR /opt/app
COPY --from=build /opt/app/build/libs/fetch-0.0.1-SNAPSHOT.jar /opt/app/fetch.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","fetch.jar"]