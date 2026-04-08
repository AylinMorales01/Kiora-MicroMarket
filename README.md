# 🛒 Kiora MicroMarket — Módulo 4: Facturación y Ventas

Sistema de gestión de ventas para micro-mercados desarrollado con **Spring Boot 3**, **MySQL** y arquitectura en capas.

---

## 🚀 Tecnologías

| Tecnología | Versión |
|------------|---------|
| Java | 21 |
| Spring Boot | 3.4.1 |
| Spring Data JPA / Hibernate | 6.x |
| MySQL | 8.x |
| Lombok | 1.18.36 |
| Maven | 3.6+ |

---

## ⚙️ Configuración y ejecución

### Requisitos previos
- Java 21 instalado
- MySQL corriendo en `localhost:3306`
- Usuario `root` sin contraseña (o ajustar `application.yaml`)

### Pasos para correr el proyecto

```bash
# 1. Clonar el repositorio
git clone https://github.com/AylinMorales01/Kiora-MicroMarket.git
cd Kiora-MicroMarket

# 2. Cambiar a la rama del módulo
git checkout feature/facturacion-ventas

# 3. Ejecutar
./mvnw spring-boot:run
```

El servidor inicia en: **`http://localhost:8080`**

> La base de datos `micromarket` se crea automáticamente con `createDatabaseIfNotExist=true`.  
> Las tablas se crean/actualizan automáticamente con `ddl-auto: update`.

---

## 🗄️ Estructura del módulo

```
src/main/java/com/kiora/micromarket/
├── controller/
│   └── SaleController.java          # Endpoints REST
├── service/
│   └── SaleService.java             # Lógica de negocio
├── repository/
│   └── SaleRepository.java          # Acceso a BD
├── entity/
│   ├── Sale.java                    # Tabla: sales
│   └── SaleDetail.java              # Tabla: sale_details
├── dto/
│   ├── request/
│   │   ├── SaleRequestDTO.java
│   │   └── SaleDetailRequestDTO.java
│   └── response/
│       ├── SaleResponseDTO.java
│       └── SaleDetailResponseDTO.java
└── excepcion/
    ├── ResourceNotFoundException.java    # → HTTP 404
    └── InsufficientStockException.java   # → HTTP 400
```

---

## 🔌 Endpoints del Módulo de Ventas

Base URL: `http://localhost:8080/api/v1/sales`

### `POST /api/v1/sales` — Crear una venta

**Request body:**
```json
{
  "employeeId": 1,
  "details": [
    { "productId": 1, "quantity": 2 },
    { "productId": 2, "quantity": 1 }
  ]
}
```

**Response `201 Created`:**
```json
{
  "id": 1,
  "date": "2026-04-07T21:20:00",
  "employeeName": "Juan Pérez",
  "active": true,
  "subtotal": 50000.0,
  "iva": 9500.0,
  "total": 59500.0,
  "details": [
    {
      "productId": 1,
      "productName": "Leche",
      "quantity": 2,
      "unitPrice": 20000.0,
      "subtotal": 40000.0
    },
    {
      "productId": 2,
      "productName": "Pan",
      "quantity": 1,
      "unitPrice": 10000.0,
      "subtotal": 10000.0
    }
  ]
}
```

---

### `GET /api/v1/sales` — Listar todas las ventas

**Response `200 OK`:** Array de `SaleResponseDTO`

---

### `GET /api/v1/sales/{id}` — Obtener venta por ID

**Response `200 OK`:** `SaleResponseDTO`  
**Response `404 Not Found`:** `"Venta no encontrada con ID: {id}"`

---

### `DELETE /api/v1/sales/{id}` — Anular una venta

**Response `204 No Content`:** Venta anulada y stock restaurado  
**Response `404 Not Found`:** Venta no existe  
**Response `400 Bad Request`:** Venta ya estaba anulada

---

## 🧠 Lógica de negocio

### Creación de venta (`createSale`)

```
1. Verificar que el empleado existe          → 404 si no
2. Por cada producto en los detalles:
   a. Verificar que existe y está activo     → 404 si no
   b. Verificar stock suficiente             → 400 si insuficiente
   c. Descontar stock del producto
   d. Calcular subtotal = cantidad × precio
3. Calcular totales:
   subtotal = Σ subtotales de todos los detalles
   IVA      = subtotal × 19%
   total    = subtotal + IVA
4. Guardar venta con todos sus detalles (CASCADE)
```

### Anulación de venta (`cancelSale`)

```
1. Verificar que la venta existe             → 404 si no
2. Verificar que la venta está activa        → 400 si ya anulada
3. Marcar venta como active = false          (soft delete)
4. Restaurar stock de cada producto
```

> **⚠️ Importante:** Las ventas **nunca se eliminan** de la BD. Se marcan como inactivas para mantener el historial contable.

---

## 📐 Fórmulas de facturación

```
subtotal_linea = cantidad × precio_unitario
subtotal_venta = Σ subtotal_linea
IVA            = subtotal_venta × 0.19   (19%)
total          = subtotal_venta + IVA
```

El `precio_unitario` se guarda en el detalle al momento de la venta, por lo que **cambios futuros en el precio del producto no afectan facturas pasadas**.

---

## 🧪 Colección Postman

Importar el archivo:
```
Kiora_Module_4_Facturacion_Ventas.postman_collection.json
```

La variable `base_url` ya está configurada con `http://localhost:8080`.

**Orden recomendado para probar:**
1. Crear un empleado (`POST /api/v1/employees/create`)
2. Crear una categoría (`POST /api/v1/categories/create`)
3. Crear un producto (`POST /api/v1/products/create`)
4. Crear una venta (`POST /api/v1/sales`)
5. Consultar la venta (`GET /api/v1/sales/{id}`)
6. Anular la venta (`DELETE /api/v1/sales/{id}`)

---

## 🔴 Errores y respuestas

| Código | Situación |
|--------|-----------|
| `201` | Venta creada exitosamente |
| `200` | Consulta exitosa |
| `204` | Venta anulada exitosamente |
| `400` | Stock insuficiente / Venta ya anulada / Validación fallida |
| `404` | Empleado, producto o venta no encontrado |

---

## 👤 Autor

Módulo desarrollado para el sistema **Kiora MicroMarket**  
Rama: `feature/facturacion-ventas`
