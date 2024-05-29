FROM ubuntu
RUN apt update
RUN apt install -y ncat
EXPOSE 8081
ENTRYPOINT ["ncat", "127.0.0.1", "8081"]
