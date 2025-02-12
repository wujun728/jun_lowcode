#这是基础镜像
FROM adoptopenjdk/openjdk8-openj9:alpine-slim
ENV LANG en_US.UTF-8
RUN apk add --update ttf-dejavu fontconfig && rm -rf /var/cache/apk/*
VOLUME /tmp
LABEL maintainer="xuyuxiang29@foxmail.com"
#复制jar包到镜像中，并且将名字改成snowy-layui.jar
ADD target/snowy-layui.jar snowy-layui.jar
#在容器启动的时候运行命令，来启动我们的项目（这其实就是一段Linux命令）
ENTRYPOINT ["java", "-Xmx2G", "-Xms200M", "-Xshareclasses", "-Xquickstart", "-Duser.timezone=GMT+08", "-jar","snowy-layui.jar"]