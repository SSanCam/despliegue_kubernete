# Usa una imagen base de Tomcat
FROM tomcat:9.0-jdk17-openjdk-slim

# Establece el directorio de trabajo
WORKDIR /usr/local/tomcat/webapps/

# Copia el archivo WAR de tu aplicación al contenedor
COPY build/libs/ApiLol-0.0.1-SNAPSHOT.war ./ROOT.war

# Expone el puerto en el que Tomcat escucha
EXPOSE 8080

# Comando para ejecutar Tomcat
CMD ["catalina.sh", "run"]