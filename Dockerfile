FROM maven:latest as builder
WORKDIR app
COPY src ./src
COPY pom.xml .
COPY zeuspext01.cer .
RUN mvn clean package

FROM openshift/java:8
WORKDIR app
COPY --from=builder app/target/TokenGeneratorBDB-0.0.1-SNAPSHOT.jar .
COPY --from=builder app/zeuspext01.cer .
RUN keytool -importcert -alias zeuspext01 -keystore "/usr/lib/jvm/java-1.8.0/jre/lib/security/cacerts" -noprompt -storepass changeit -file "app/zeuspext01.cer"
#RUN keytool -importcert -alias zeuspext01 -keystore "//usr/local/openjdk-8/jre/lib/security/cacerts" -noprompt -storepass changeit -file "/zeuzCert/zeuspext01.cer"
ENV JAVA_OPTS="-Xmx256m -Xmx512m"
CMD ["java","-jar", "app/TokenGeneratorBDB-0.0.1-SNAPSHOT.jar"]
