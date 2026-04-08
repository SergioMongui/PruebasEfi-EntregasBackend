-- para usuaro
INSERT INTO usuario (nombre, telefono, email, contraseña, rol) VALUES
('María Pavas', '3101234567', 'maria.pavas@mail.com', '456', 'Supervisor'),
('Luis Gómez', '3112345678', 'luis.gomez@mail.com', '123', 'Repartidor'),
('Camila Torres', '3123456789', 'camila.torres@mail.com', 'abc', 'Supervisor'),
('Carlos Ruiz', '3134567890', 'carlos.ruiz@mail.com', '789', 'Repartidor'),
('Laura Díaz', '3145678901', 'laura.diaz@mail.com', 'pass123', 'Supervisor');

-- para orden_envio
INSERT INTO orden_envio (nombre_cliente, direccion, telefono, lista_productos, valor) VALUES
('Pedro Perez', 'Calle 123 #45-67', '3151112233', 'TV, Nevera', 2500000),
('Ana Martínez', 'Carrera 10 #12-34', '3162223344', 'Celular, Cargador', 1200000),
('Jorge Romero', 'Av. Siempre Viva #742', '3173334455', 'Sofá, Mesa', 1800000),
('Diana López', 'Calle 8 #9-10', '3184445566', 'Escritorio, Silla', 900000),
('Miguel Ángel', 'Carrera 7 #8-15', '3195556677', 'Laptop, Audífonos', 3500000);
