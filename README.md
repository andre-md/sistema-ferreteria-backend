# Sistema de Gestión para Ferretería — Backend

Backend de un sistema de gestión diseñado para reemplazar el control por papel/lápiz y hojas de Excel en una ferretería real. Maneja productos, proveedores, tipo de cambio, inventario y pedidos con lógica de negocio adaptada a un caso de uso real: precios que dependen del tipo de cambio del dólar, pedidos con adelanto y saldo pendiente, y trazabilidad completa de inventario.

Proyecto desarrollado como práctica personal y como sistema funcional para un negocio real, aplicando Java, Spring Boot y buenas prácticas de arquitectura en capas.

## Stack tecnológico

- **Java 21**
- **Spring Boot 3.3**
- **Spring Security + JWT** (autenticación y autorización por roles)
- **Spring Data JPA / Hibernate**
- **PostgreSQL 16** (vía Docker)
- **Maven**
- **Lombok**
- **Jakarta Bean Validation**

## Características principales

- **Autenticación con JWT** y control de acceso por roles (`ADMIN`, `VENDEDOR`)
- **Catálogo de productos** con categorías, unidad de medida (unidad, kilogramo, metro) y control de stock
- **Múltiples proveedores por producto**, cada uno con su propio precio de costo — permite comparar y encontrar el proveedor más barato
- **Soporte para productos cotizados en dólares**, con conversión automática a soles usando el tipo de cambio más reciente registrado (historial completo, sin sobrescribir valores anteriores)
- **Pedidos con adelanto y saldo pendiente**, no solo pago único — refleja cómo opera el negocio real
- **Descuento y reversión automática de stock**, con historial de movimientos de inventario (entradas, salidas, ajustes) para trazabilidad completa
- **Cancelación de pedidos sin pérdida de historial** (soft-delete): un pedido cancelado conserva toda su información en vez de eliminarse
- **Manejo centralizado de errores** con códigos HTTP semánticamente correctos (404, 409, 422, etc.) y mensajes claros

## Módulos

| Módulo | Descripción |
|---|---|
| Auth | Login con JWT |
| Usuario | Gestión de cuentas (admin/vendedores), cambio de contraseña |
| Categoria | Clasificación de productos |
| Producto | Catálogo, precios, stock |
| Proveedor | Registro de distribuidores |
| ProductoProveedor | Relación producto↔proveedor con precio de costo y conversión de moneda |
| TipoCambio | Historial del tipo de cambio, actualización manual por el administrador |
| MovimientoInventario | Historial de entradas/salidas/ajustes de stock |
| Pedido / DetallePedido | Pedidos con adelanto, estados de pago y entrega, cancelación con reversión de stock |

## Seguridad

- Autenticación stateless con **JWT** (24h de expiración)
- Contraseñas encriptadas con **BCrypt**
- Autorización por rol con `@PreAuthorize`
- Manejo diferenciado de errores de seguridad:
  - **401** cuando no hay autenticación válida (`AuthenticationEntryPoint`)
  - **403** cuando el usuario está autenticado pero no tiene el rol requerido (`AccessDeniedHandler`)

## Cómo levantar el proyecto

### Requisitos
- Java 21
- Docker Desktop
- Maven (o el wrapper incluido en tu IDE)

### 1. Levantar PostgreSQL con Docker

```bash
docker run --name ferreteria-db -e POSTGRES_PASSWORD=tu_password -e POSTGRES_DB=ferreteria_db -p 5432:5432 -d postgres:16
```

### 2. Configurar `application.properties`

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ferreteria_db
spring.datasource.username=postgres
spring.datasource.password=tu_password
spring.jpa.hibernate.ddl-auto=update
jwt.secret=tu_clave_secreta_base64_de_256_bits
```

> No subas tus credenciales reales a un repositorio público — usa variables de entorno o un `application.properties` excluido en `.gitignore`.

### 3. Ejecutar

```bash
mvn spring-boot:run
```

El servidor levanta en `http://localhost:8080`.

## Modelo de datos

El sistema está diseñado alrededor de 9 entidades relacionadas: `Usuario`, `Categoria`, `Producto`, `Proveedor`, `ProductoProveedor`, `TipoCambio`, `MovimientoInventario`, `Pedido` y `DetallePedido`.

## Decisiones de diseño destacadas

- **Precios y montos en `BigDecimal`**, nunca `double`/`float`, para evitar errores de redondeo en dinero
- **`precioUnitario` se congela en cada `DetallePedido`** al momento de la venta — si el precio del producto cambia después, los pedidos históricos no se ven afectados
- **El tipo de cambio se actualiza manualmente**, no se consulta automáticamente de una API externa — refleja que el negocio real actualiza el dólar cuando lo necesita, no en un horario fijo
- **Cancelar un pedido no borra el registro** — lo marca como `CANCELADO`, preservando el historial de adelantos y montos para auditoría

## Estado del proyecto

Backend completo y funcional. Próximos pasos: frontend en Angular (panel interno + catálogo público) y, como fase futura, un asistente/chatbot conectado al catálogo.

---