FROM maven:latest as builder
WORKDIR app
COPY src ./src
COPY pom.xml .
COPY zeuspext01.cer .
RUN mvn clean package

FROM openshift/java:8
WORKDIR app
COPY --from=builder /app/target/TokenGeneratorBDB-0.0.1-SNAPSHOT.jar .
COPY --from=builder /app/zeuspext01.cer .
RUN keytool -importcert -alias zeuspext01 -keystore "${JAVA_HOME}/jre/lib/security/cacerts" -noprompt -storepass changeit -file "zeuspext01.cer"
#RUN keytool -importcert -alias zeuspext01 -keystore "//usr/local/openjdk-8/jre/lib/security/cacerts" -noprompt -storepass changeit -file "/zeuzCert/zeuspext01.cer"
ENV JAVA_OPTS="-Xmx150m -Xmx75m"
CMD ["java","-jar", "TokenGeneratorBDB-0.0.1-SNAPSHOT.jar"]
