# Kiora - MicroMarket 🛒

Sistema de gestión para MicroMarket, desarrollado con **Spring Boot 3.4.1** y **Java 21**.

## Requisitos Previos

- **Java 21** (Recomendado: [Azul Zulu](https://www.azul.com/downloads/?version=java-21-lts&package=jdk) o [Eclipse Temurin](https://adoptium.net/temurin/releases/?version=21))
- **MySQL 8.0+**
- **Maven** (usaremos `mvnw` incluido)

## Configuración de Base de Datos

1. Asegúrate de tener MySQL corriendo.
2. Crea una base de datos llamada `micro_market_db`:
   ```sql
   CREATE DATABASE micro_market_db;
   ```
3. El sistema creará las tablas automáticamente al iniciar gracias a `hibernate.ddl-auto: update`.

## Cómo Ejecutar

Desde la terminal en la raíz del proyecto:

```bash
# Windows
./mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

## Guía Detallada
Para una instalación paso a paso, consulta la [Guía de Instalación](./setup_guide.md).
