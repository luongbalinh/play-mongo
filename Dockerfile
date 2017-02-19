FROM java
LABEL Description="This is Play framework in Scala" Version="1.1.0"
LABEL maintainer LUONG Ba Linh<linhluongba@gmail.com>

RUN curl -O http://downloads.typesafe.com/typesafe-activator/1.3.12/typesafe-activator-1.3.12.zip
RUN unzip typesafe-activator-1.3.12.zip -d / && rm typesafe-activator-1.3.12.zip && chmod a+x /activator-1.3.12/activator
ENV PATH=$PATH:/activator-1.3.12

EXPOSE 9000
RUN mkdir /app
WORKDIR /app

CMD ["activator", "~run 9000"]