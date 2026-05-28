USE agencia_viajes;

SET FOREIGN_KEY_CHECKS=0;

-- CLIENTE
INSERT INTO Cliente (idCliente, nomCliente, apCliente, Email, Telefono, Fecha_Registro) VALUES
(1, 'Juan', 'Pérez', 'juan.perez@email.com', '600111222', '2025-01-10'),
(2, 'María', 'Gómez', 'maria.gomez@email.com', '600333444', '2025-02-15'),
(3, 'Carlos', 'López', 'carlos.lopez@email.com', '600555666', '2025-03-20');

-- DESTINO
INSERT INTO Destino (idDestino, nombre, pais, descripcion, precio_base) VALUES
(1, 'París', 'Francia', 'Viaje cultural y romántico por la ciudad de la luz.', 850.00),
(2, 'Roma', 'Italia', 'Destino histórico con monumentos y gastronomía.', 720.00),
(3, 'Lisboa', 'Portugal', 'Ciudad costera con encanto y clima suave.', 540.00);

-- EMPLEADO
INSERT INTO Empleado (id_Empleado, Nombre, Apellidos, Telefono, Correo, Puesto) VALUES
(1, 'Laura', 'Martín Ruiz', '611111111', 'laura@agencia.com', 'Directora'),
(2, 'Pedro', 'Sánchez Mora', '622222222', 'pedro@agencia.com', 'Asesor'),
(3, 'Ana', 'Torres Gil', '633333333', 'ana@agencia.com', 'Asesor');

-- AGENTE
INSERT INTO Agente (id_Agente, nombre, telefono, email, Empleado_id_Empleado) VALUES
(1, 'Agente Laura', '611111111', 'laura@agencia.com', 1),
(2, 'Agente Pedro', '622222222', 'pedro@agencia.com', 2),
(3, 'Agente Ana', '633333333', 'ana@agencia.com', 3);

-- PROMOCIONES
INSERT INTO Promociones (id_Promociones, codigo, descuento, fehca_inicio, fecha_fin) VALUES
(1, 'VERANO10', 10.00, '2026-06-01', '2026-08-31'),
(2, 'ESCAPADA15', 15.00, '2026-04-01', '2026-05-31'),
(3, 'NAVIDAD20', 20.00, '2026-12-01', '2026-12-31');

-- VIAJES
INSERT INTO Viajes (idViaje, fecha_inicio, fecha_fin, precio_total, Id_Destino, Id_Cliente) VALUES
(1, '2026-06-10', '2026-06-17', 940.00, 1, 1),
(2, '2026-07-05', '2026-07-12', 810.00, 2, 2),
(3, '2026-08-20', '2026-08-27', 620.00, 3, 3);

-- RESERVA
INSERT INTO Reserva (id_Reserva, Fecha_Reserva, estado, num_personas, precio_total, Cliente_idCliente, Viaje_idViaje, Viaje_Id_Destino, Viaje_Id_Cliente) VALUES
(1, '2026-04-01 10:30:00', 'Confirmada', 2, 1880.00, 1, 1, 1, 1),
(2, '2026-04-02 12:00:00', 'Pendiente', 1, 810.00, 2, 2, 2, 2),
(3, '2026-04-03 16:15:00', 'Confirmada', 3, 1860.00, 3, 3, 3, 3);

-- PAGO
INSERT INTO Pago (id_Pago, Importe, Metodo_pago, fecha_pago, estado_pago, Reserva_id_Reserva, Reserva_Cliente_idCliente, Reserva_Viaje_idViaje, Reserva_Viaje_Id_Destino, Reserva_Viaje_Id_Cliente) VALUES
(1, 1880.00, 'Tarjeta', '2026-04-01', 'Pagado', 1, 1, 1, 1, 1),
(2, 810.00, 'Transferencia', '2026-04-02', 'Pendiente', 2, 2, 2, 2, 2),
(3, 1860.00, 'Bizum', '2026-04-03', 'Pagado', 3, 3, 3, 3, 3);

-- HOTEL
INSERT INTO Hotel (idHotel, nombre, direccion, categoria, Id_Destino) VALUES
(1, 'Hotel París Central', '12 Rue de Rivoli', 4, 1),
(2, 'Roma Imperial Hotel', 'Via Cavour 45', 4, 2),
(3, 'Lisboa Ocean View', 'Avenida da Liberdade 100', 3, 3);

-- HABITACION
INSERT INTO Habitacion (idHabitacion, tipo, precio_noche, Capacidad, disponible, Id_Hotel, Hotel_Id_Destino) VALUES
(1, 'Doble', 120.00, 2, 1, 1, 1),
(2, 'Individual', 85.00, 1, 1, 2, 2),
(3, 'Suite', 180.00, 4, 1, 3, 3);

-- ACTIVIDAD
INSERT INTO Actividad (idActividad, nombre, descripcion, precio, Id_Destino) VALUES
(1, 'Tour por el Sena', 'Paseo en barco por el río Sena.', 45.00, 1),
(2, 'Visita al Coliseo', 'Entrada guiada al Coliseo romano.', 35.00, 2),
(3, 'Ruta por Alfama', 'Recorrido cultural por el barrio de Alfama.', 25.00, 3);

-- VIAJE_HAS_ACTIVIDAD
INSERT INTO Viaje_has_Actividad (Viaje_idViaje, Viaje_Id_Destino, Actividad_idActividad, Actividad_Id_Destino) VALUES
(1, 1, 1, 1),
(2, 2, 2, 2),
(3, 3, 3, 3);

-- TRANSPORTE
INSERT INTO Transporte (id_Transporte, tipo, Empresa, Origen, Destino, Fecha_salida, Fecha_llegada, Precio, Viaje_idViaje, Viaje_Id_Destino, Viaje_Id_Cliente) VALUES
(1, 'Avión', 'Iberia', 'Madrid', 'París', '2026-06-10 08:00:00', '2026-06-10 10:15:00', 180.00, 1, 1, 1),
(2, 'Avión', 'Alitalia', 'Madrid', 'Roma', '2026-07-05 09:00:00', '2026-07-05 11:20:00', 160.00, 2, 2, 2),
(3, 'Avión', 'TAP Air', 'Madrid', 'Lisboa', '2026-08-20 07:30:00', '2026-08-20 08:45:00', 120.00, 3, 3, 3);

-- VUELO
INSERT INTO Vuelo (id_Vuelo, Aerolinea, Numero_vuelo, Transporte_id_Transporte, Transporte_Viaje_idViaje, Transporte_Viaje_Id_Destino, Transporte_Viaje_Id_Cliente) VALUES
(1, 'Iberia', 'IB1234', 1, 1, 1, 1),
(2, 'Alitalia', 'AZ5678', 2, 2, 2, 2),
(3, 'TAP Air', 'TP9012', 3, 3, 3, 3);

-- RESEÑA
INSERT INTO Reseña (id_Reseña, Puntuacion, Comentario, fecha, Cliente_idCliente, Destino_idDestino) VALUES
(1, 5, 'Experiencia excelente, muy recomendable.', '2026-06-20', 1, 1),
(2, 4, 'Muy bonito, repetiría sin duda.', '2026-07-15', 2, 2),
(3, 5, 'Viaje perfecto y muy bien organizado.', '2026-08-30', 3, 3);

-- PROMOCIONES_HAS_VIAJE
INSERT INTO Promociones_has_Viaje (Promociones_id_Promociones, Viaje_idViaje, Viaje_Id_Destino, Viaje_Id_Cliente) VALUES
(1, 1, 1, 1),
(2, 2, 2, 2),
(3, 3, 3, 3);

SET FOREIGN_KEY_CHECKS=1;


select *
from destino;