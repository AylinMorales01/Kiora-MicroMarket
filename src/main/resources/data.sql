-- Insertar Cargos permitidos (Regla de Negocio Módulo 3)

INSERT INTO categorias (nombre, descripcion) VALUES ('Lácteos', 'Productos derivados de la leche');
INSERT INTO categorias (nombre, descripcion) VALUES ('Aseo', 'Productos de limpieza para el hogar');
INSERT INTO categorias (nombre, descripcion) VALUES ('Granos', 'Arroz, lentejas, frijoles y similares');

-- Insertar Empleados iniciales (Regla de Negocio: ADMINISTRADOR, CAJERO, AUXILIAR)
INSERT INTO employees (cedula, name, role, entry_date, salary, active)
VALUES ('1094001', 'Juan Hernandez', 'ADMINISTRADOR', '2024-01-01', 5000000, true);

INSERT INTO employees (cedula, name, role, entry_date, salary, active)
VALUES ('1094002', 'Pedro Perez', 'CAJERO', '2024-02-15', 1300000, true);

-- Insertar Productos de prueba
INSERT INTO products (name, description, barcode, price, stock, active, category_id)
VALUES ('Leche Entera', 'Bolsa de leche entera 1L', '770123456', 3500.0, 50, true, 1);

INSERT INTO products (name, description, barcode, price, stock, active, category_id)
VALUES ('Jabon Rey', 'Barra de jabon', '770987654', 1500.0, 100, true, 2);
INSERT INTO products (name, description, barcode, price, stock, active, category_id)
