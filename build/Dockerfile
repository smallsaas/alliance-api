FROM zelejs/allin-web:alpine-m2 AS M2

#builk-stage
FROM maven:3.6.0-jdk-11-slim AS build

# init .m2 from alpine-m2 image first
WORKDIR /root/.m2
COPY --from=M2 /root/.m2 /root/m2
RUN --mount=type=cache,id=m2_cache,target=/root/.m2,rw cp -r /root/m2/* /root/.m2

## mvn package
WORKDIR /usr/src

ADD ./alliance-api ./alliance-api
ADD ./alliance-core ./alliance-core
ADD ./alliance-friend ./alliance-friend
ADD ./alliance-rpc ./alliance-rpc
ADD ./bonus ./bonus

COPY ./pom.xml  ./pom.xml

# # To package the application
RUN --mount=type=cache,id=m2_cache,target=/root/.m2,rw mvn -DskipStandalone=false package -Dmaven.test.skip=true

## final
WORKDIR /usr/src/alliance-api

CMD ["sh", "-c", "/bin/cp target/*-standalone.jar /webapps"]

