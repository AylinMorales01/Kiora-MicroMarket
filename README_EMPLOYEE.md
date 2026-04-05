# Guía de Ejecución y Pruebas - Módulo de Empleados

Este documento detalla cómo poner en marcha el proyecto localmente y cómo probar las funcionalidades CRUD del Módulo de Empleados sin necesidad de una interfaz gráfica.

## 1. Requisitos Previos
- **Java 21** instalado.
- **MySQL** instalado y en ejecución.
- Configurar el archivo `src/main/resources/application.properties` con tus credenciales de base de datos (DB_URL, DB_USERNAME, DB_PASSWORD).

## 2. Cómo Correr el Proyecto Localmente

Para iniciar el servidor de Spring Boot, ejecuta el siguiente comando en la raíz del proyecto:

```bash
mvn spring-boot:run
```

El servidor iniciará por defecto en el puerto **8080**.

---

## 3. Pruebas del Módulo de Empleados (Curl)

Puedes usar Postman o copiar estos comandos en tu terminal (Linux/macOS o PowerShell en Windows) para probar cada endpoint.

### A. Crear un Empleado (POST)
```bash
curl -X POST http://localhost:8080/api/employees \
-H "Content-Type: application/json" \
-d '{
  "cedula": "1001",
  "name": "Aylin Morales",
  "role": "ADMINISTRADOR",
  "entryDate": "2024-01-01",
  "salary": 3500000.0
}'
```

### B. Listar Todos los Empleados Activos (GET)
```bash
curl -X GET http://localhost:8080/api/employees
```

### C. Editar un Empleado (PUT)
*Reemplaza `{id}` por el ID generado al crear (ej: 1).*
```bash
curl -X PUT http://localhost:8080/api/employees/1 \
-H "Content-Type: application/json" \
-d '{
  "cedula": "1001",
  "name": "Aylin Morales Actualizado",
  "role": "ADMINISTRADOR",
  "entryDate": "2024-01-01",
  "salary": 4000000.0
}'
```

### D. Filtrar por Cargo (GET)
```bash
curl -X GET http://localhost:8080/api/employees/role/ADMINISTRADOR
```

### E. Filtrar por Rango de Fechas (GET)
```bash
curl -X GET "http://localhost:8080/api/employees/date-range?start=2023-01-01&end=2024-12-31"
```

### F. Eliminar un Empleado (DELETE - Borrado Lógico)
*Reemplaza `{id}` con el ID del empleado.*
```bash
curl -X DELETE http://localhost:8080/api/employees/1
```
*Si intentas listar de nuevo, el empleado ya no aparecerá, pero seguirá en la base de datos con `active = false`.*

---

## 4. Notas Técnicas
- **Lógica de Borrado**: Se utiliza borrado lógico (`active = false`), por lo que los datos no se pierden físicamente.
- **Cargos Permitidos**: `ADMINISTRADOR`, `CAJERO`, `AUXILIAR`.
- **Formato de Fecha**: `YYYY-MM-DD`.
