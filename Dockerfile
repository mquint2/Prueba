FROM maven:latest as builder
WORKDIR app
COPY src ./src
COPY pom.xml .
COPY certificados ./certificados
RUN mvn clean package

FROM openshift/java:8
WORKDIR app
COPY --from=builder /app/target/WsRestDataGestionUsuarioLDAP-0.0.1-SNAPSHOT.jar .
COPY --from=builder /app/certificados/banbta-RootCA.cer .
COPY --from=builder /app/certificados/BANBTA-CA-EMISORA.cer .
COPY --from=builder /app/certificados/zeuspext01.cer .
USER root
RUN keytool -importcert -alias banbta-RootCA -keystore "${JAVA_HOME}/jre/lib/security/cacerts" -noprompt -storepass changeit -file "banbta-RootCA.cer"
RUN keytool -importcert -alias BANBTA-CA-EMISORA -keystore "${JAVA_HOME}/jre/lib/security/cacerts" -noprompt -storepass changeit -file "BANBTA-CA-EMISORA.cer"
RUN keytool -importcert -alias zeuspext01 -keystore "${JAVA_HOME}/jre/lib/security/cacerts" -noprompt -storepass changeit -file "zeuspext01.cer"
CMD ["java", "-jar", "WsRestDataGestionUsuarioLDAP-0.0.1-SNAPSHOT.jar"]
