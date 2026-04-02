-- Insertar Cargos permitidos (Regla de Negocio Módulo 3)

INSERT INTO categorias (nombre, descripcion) VALUES ('Lácteos', 'Productos derivados de la leche');
INSERT INTO categorias (nombre, descripcion) VALUES ('Aseo', 'Productos de limpieza para el hogar');
INSERT INTO categorias (nombre, descripcion) VALUES ('Granos', 'Arroz, lentejas, frijoles y similares');

-- Insertar Empleados iniciales (Regla de Negocio: ADMINISTRADOR, CAJERO, AUXILIAR)
INSERT INTO empleados (cedula, nombre, cargo, fecha_ingreso, salario) 
VALUES ('1094001', 'Juan Hernandez', 'ADMINISTRADOR', '2024-01-01', 5000000);

INSERT INTO empleados (cedula, nombre, cargo, fecha_ingreso, salario) 
VALUES ('1094002', 'Pedro Perez', 'CAJERO', '2024-02-15', 1300000);