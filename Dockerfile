FROM maven:latest as builder
WORKDIR app
COPY src ./src
COPY pom.xml .
RUN mvn clean package

FROM openshift/java:8
WORKDIR /usr/app
RUN mkdir zeuzCert
COPY ./zeuspext01.cer /usr/app/zeuzCert
RUN keytool -importcert -alias zeuspext01 -keystore "/usr/lib/jvm/java-1.8.0/jre/lib/security/cacerts" -noprompt -storepass changeit -file "/usr/app/zeuzCert/zeuzCert/zeuspext01.cer"
#RUN keytool -importcert -alias zeuspext01 -keystore "//usr/local/openjdk-8/jre/lib/security/cacerts" -noprompt -storepass changeit -file "/zeuzCert/zeuspext01.cer"
COPY --from=builder /app/target/TokenGeneratorBDB-0.0.1-SNAPSHOT.jar .
ENV JAVA_OPTS="-Xmx256m -Xmx512m"
CMD ["java","-jar", "/usr/app/TokenGeneratorBDB-0.0.1-SNAPSHOT.jar"]
