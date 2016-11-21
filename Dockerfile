FROM nimmis/java-centos:openjdk-7-jre
WORKDIR /opt
COPY entrypoint.sh /tmp/
RUN chmod u+x /tmp/entrypoint.sh
COPY target/wiremock-request-to-body-transformer-1.0.jar /tmp/
RUN chmod u+x /tmp/wiremock-request-to-body-transformer-1.0.jar
COPY 404.vm /tmp/
RUN chmod u+r /tmp/404.vm
COPY setup.sh /tmp/setup.sh
RUN chmod u+x /tmp/setup.sh
RUN sh /tmp/setup.sh
EXPOSE 80
ENTRYPOINT ["/tmp/entrypoint.sh"]
