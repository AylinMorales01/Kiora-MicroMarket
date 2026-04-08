-- 1. Crear tabla de Categorías
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255)
);

-- 2. Crear tabla de Proveedores
CREATE TABLE IF NOT EXISTS providers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tax_id VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(255)
);

-- 3. Crear tabla de Productos
CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    barcode VARCHAR(255) UNIQUE,
    price DOUBLE NOT NULL,
    stock INT NOT NULL,
    active BOOLEAN DEFAULT TRUE NOT NULL,
    category_id BIGINT,
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- 4. Crear tabla intermedia ManyToMany entre Productos y Proveedores
CREATE TABLE IF NOT EXISTS products_providers (
    product_id BIGINT NOT NULL,
    provider_id BIGINT NOT NULL,
    PRIMARY KEY (product_id, provider_id),
    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT fk_provider FOREIGN KEY (provider_id) REFERENCES providers(id)
);

-- 5. Crear tabla de Empleados
CREATE TABLE IF NOT EXISTS employees (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cedula VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL, -- Campo agregado para JWT
    name VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    entry_date DATE,
    salary DOUBLE NOT NULL,
    active BOOLEAN DEFAULT TRUE NOT NULL
);

-- 6. Crear tabla de Ventas
CREATE TABLE IF NOT EXISTS sales (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    date DATETIME NOT NULL,
    subtotal DOUBLE NOT NULL,
    iva DOUBLE NOT NULL,
    total DOUBLE NOT NULL,
    active BOOLEAN DEFAULT TRUE NOT NULL,
    employee_id BIGINT NOT NULL,
    CONSTRAINT fk_sale_employee FOREIGN KEY (employee_id) REFERENCES employees(id)
);

-- 7. Crear tabla de Detalles de Venta
CREATE TABLE IF NOT EXISTS sale_details (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sale_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    unit_price DOUBLE NOT NULL,
    subtotal DOUBLE NOT NULL,
    CONSTRAINT fk_detail_sale FOREIGN KEY (sale_id) REFERENCES sales(id),
    CONSTRAINT fk_detail_product FOREIGN KEY (product_id) REFERENCES products(id)
);