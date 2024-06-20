FROM maven AS build
COPY . .
RUN mvn clean package -DskipTests -Denable-preview

FROM openjdk:21
COPY --from=build /target/jar.jar jar.jar
EXPOSE 8090
ENTRYPOINT ["java","--enable-preview","-jar","jar.jar"]
