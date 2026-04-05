# Guía de Instalación - Kiora MicroMarket 🛠️

Este documento describe cómo instalar y configurar el entorno de desarrollo para el proyecto Kiora MicroMarket, asegurando que todos los desarrolladores utilicen **Spring Boot 3.4.1** y **Java 21**.

## 1. Instalación de Java 21 LTS ☕

Es obligatorio usar **Java 21** para compatibilidad con Spring Boot 3.4.1.

### Pasos:
1.  **Descarga**: Descarga [Azul Zulu JDK 21](https://www.azul.com/downloads/?version=java-21-lts&package=jdk) o [Eclipse Temurin 21](https://adoptium.net/temurin/releases/?version=21).
2.  **Instalación**: Sigue el instalador estándar para Windows.
3.  **Verificación**: Abre una terminal (CMD o PowerShell) y ejecuta:
    ```bash
    java -version
    ```
    *Deberías ver una salida indicando `openjdk version "21.0.x"`.*

## 2. Configuración de la Base de Datos (MySQL) 🗄️

El proyecto usa **MySQL** y requiere una base de datos específica.

### Pasos:
1.  **Instalar MySQL Server**: Recomendamos [MySQL Installer](https://dev.mysql.com/downloads/installer/).
2.  **Crear Base de Datos**: Abre MySQL Workbench o tu cliente preferido y ejecuta:
    ```sql
    CREATE DATABASE micro_market_db;
    ```
3.  **Ajustar Credenciales**:
    - Abre el archivo [application.yaml](src/main/resources/application.yaml).
    - Modifica `username` y `password` según tu configuración local de MySQL.

## 3. Configuración del IDE (VS Code / IntelliJ) 💻

### VS Code:
Instala las siguientes extensiones:
- **Extension Pack for Java** (Microsoft)
- **Spring Boot Extension Pack** (VMware)
- **Lombok Annotations Support for VS Code** (Gabe House)

### Configuración de Lombok:
Si ves errores en los DTOs o Entidades (como métodos `get/set` no encontrados), asegúrate de:
1. Tener la extensión de Lombok instalada.
2. Reiniciar el Language Server de Java (`Ctrl+Shift+P` -> `Java: Clean Language Server Workspace`).

## 4. Ejecución del Proyecto 🚀

Para compilar y arrancar el servidor:

```bash
# En Windows (Root del proyecto)
./mvnw.cmd clean spring-boot:run
```

**Nota**: El archivo `data.sql` se cargará automáticamente la primera vez que inicies el servidor, insertando los empleados y categorías de prueba.

---
*Para soporte adicional, contacta al equipo de desarrollo.*
