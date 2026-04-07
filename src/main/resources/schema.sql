/* 1. Crear tabla de Usuarios (Módulo de Autenticación)
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);*/

-- 2. Crear tabla de Categorías
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255)
);

-- 3. Crear tabla de Proveedores
CREATE TABLE IF NOT EXISTS providers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tax_id VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(255)
);

-- 4. Crear tabla de Productos (Tiene la llave foránea de categoría)
CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    barcode VARCHAR(255) UNIQUE,
    price DOUBLE NOT NULL,
    stock INT NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    category_id BIGINT,
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- 5. Crear tabla intermedia para la relación ManyToMany (Productos y Proveedores)
CREATE TABLE IF NOT EXISTS products_providers (
    product_id BIGINT NOT NULL,
    provider_id BIGINT NOT NULL,
    PRIMARY KEY (product_id, provider_id),
    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT fk_provider FOREIGN KEY (provider_id) REFERENCES providers(id)
);

-- 6. Crear tabla de Empleados
CREATE TABLE IF NOT EXISTS employees (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cedula VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    role VARCHAR(255),
    entry_date DATE,
    salary DOUBLE,
    active BOOLEAN DEFAULT TRUE
);