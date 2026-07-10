# Etapa 1: Compilación con Java 21
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copiar el wrapper de Maven y el pom.xml
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Dar permisos de ejecución a maven wrapper (importante para Linux/Docker)
RUN chmod +x ./mvnw

# Copiar el código fuente
COPY src ./src

# Compilar el proyecto saltando los tests
RUN ./mvnw clean package -DskipTests -B

# Etapa 2: Runtime liviano (solo JRE)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiar el .jar generado en la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto (ajusta si tu app usa uno distinto a 8080)
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]