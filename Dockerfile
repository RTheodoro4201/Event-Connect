# Use uma imagem base Java (certifique-se de escolher a versão correta para o seu projeto)
FROM eclipse-temurin:22-jdk

# Defina um argumento para o nome do arquivo JAR
ARG JAR_FILE=target/*.jar

# Crie um diretório para a aplicação dentro do container
RUN mkdir /app

# Copie o arquivo JAR da sua aplicação para o diretório /app dentro do container
COPY ${JAR_FILE} /app/application.jar

# Exponha a porta em que a sua aplicação Spring Boot rodará (a porta padrão é 8080)
EXPOSE 8080

LABEL authors="Rodrigo Theodoro"

# Comando a ser executado quando o container for iniciado
ENTRYPOINT ["java", "-jar", "/app/application.jar"]