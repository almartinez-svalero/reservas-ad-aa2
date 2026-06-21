-- Datos de prueba para PostgreSQL.
-- Ejecutar después de arrancar la aplicación para que Hibernate haya creado las tablas.

INSERT INTO restaurantes (nombre, direccion, telefono, aforo_maximo, valoracion_media, activo, fecha_apertura)
VALUES ('BJ Reservas Centro', 'Calle Alfonso I 10, Zaragoza', '+34976000001', 80, 4.5, true, '2022-03-15');

INSERT INTO clientes (nombre, email, telefono, numero_visitas, saldo_pendiente, vip, fecha_registro)
VALUES ('Laura Gómez', 'laura.gomez@email.com', '+34666000111', 5, 0.0, true, '2024-01-10');

INSERT INTO mesas (numero, capacidad, ubicacion, reservable, suplemento_terraza, fecha_alta, restaurante_id)
VALUES (1, 4, 'Salón principal', true, 0.0, '2024-01-01', 1);

INSERT INTO empleados (nombre, email, puesto, sueldo_hora, activo, fecha_contratacion, restaurante_id)
VALUES ('Carlos Ruiz', 'carlos.ruiz@bjreservas.com', 'Encargado de sala', 12.5, true, '2023-05-20', 1);

INSERT INTO reservas (codigo, fecha_reserva, hora_reserva, numero_personas, importe_senal, confirmada, observaciones, cliente_id, mesa_id, empleado_id)
VALUES ('RES-001', '2026-01-15', '21:00', 4, 20.0, true, 'Cena familiar', 1, 1, 1);
