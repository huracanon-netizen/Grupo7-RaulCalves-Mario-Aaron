

  SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
  SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
  SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
  

  CREATE DATABASE agencia_viajes;
  USE agencia_viajes;

  CREATE TABLE Cliente (
    idCliente INT PRIMARY KEY AUTO_INCREMENT NOT NULL UNIQUE,
    nomCliente VARCHAR(50) NULL,
    apCliente VARCHAR(50) NULL,
    Email VARCHAR(100) NULL,
    Telefono VARCHAR(20) NULL,
    Contraseña VARCHAR(20) NULL,
    nomUsuario VARCHAR(30) NULL
  );

  CREATE TABLE Destino (
    idDestino INT PRIMARY KEY AUTO_INCREMENT NOT NULL UNIQUE,
    nombre VARCHAR(100) NULL,
    pais VARCHAR(50) NULL,
    descripcion MEDIUMTEXT NULL,
    precio_base DECIMAL(10,2) NULL
  );

  -- ✔️ CORRECCIÓN 1: renombrado de Viajes → Viaje (las FK lo referenciaban como Viaje)
  CREATE TABLE Viaje (
    idViaje INT AUTO_INCREMENT NOT NULL,
    fecha_inicio DATE NULL,
    fecha_fin DATE NULL,
    precio_total DECIMAL(10,2) NULL,
    Id_Destino INT NOT NULL,
    Id_Cliente INT NOT NULL,
    PRIMARY KEY (idViaje, Id_Destino, Id_Cliente),
    INDEX fk_Viaje_Destino1_idx (Id_Destino ASC),
    INDEX fk_Viaje_Cliente1_idx (Id_Cliente ASC),
    FOREIGN KEY (Id_Destino) REFERENCES Destino (idDestino)
      ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (Id_Cliente) REFERENCES Cliente (idCliente)
      ON DELETE CASCADE ON UPDATE CASCADE
  );

  CREATE TABLE Reserva (
    id_Reserva INT AUTO_INCREMENT NOT NULL,
    Fecha_Reserva DATETIME NULL,
    estado VARCHAR(50) NULL,
    num_personas INT NULL,
    precio_total DECIMAL(10,2) NULL,
    Cliente_idCliente INT NOT NULL,
    Viaje_idViaje INT NOT NULL,
    Viaje_Id_Destino INT NOT NULL,
    Viaje_Id_Cliente INT NOT NULL,
    PRIMARY KEY (id_Reserva, Cliente_idCliente, Viaje_idViaje, Viaje_Id_Destino, Viaje_Id_Cliente),
    INDEX fk_Reserva_Cliente1_idx (Cliente_idCliente ASC),
    INDEX fk_Reserva_Viaje1_idx (Viaje_idViaje ASC, Viaje_Id_Destino ASC, Viaje_Id_Cliente ASC),
    FOREIGN KEY (Cliente_idCliente) REFERENCES Cliente (idCliente)
      ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (Viaje_idViaje, Viaje_Id_Destino, Viaje_Id_Cliente) REFERENCES Viaje (idViaje, Id_Destino, Id_Cliente)
      ON DELETE CASCADE ON UPDATE CASCADE
  );

  CREATE TABLE Hotel (
    idHotel INT AUTO_INCREMENT NOT NULL,
    nombre VARCHAR(100) NULL,
    direccion VARCHAR(150) NULL,
    categoria INT NULL,
    Id_Destino INT NOT NULL,
    PRIMARY KEY (idHotel, Id_Destino),
    INDEX fk_Hotel_Destino1_idx (Id_Destino ASC),
    FOREIGN KEY (Id_Destino) REFERENCES Destino (idDestino)
      ON DELETE CASCADE ON UPDATE CASCADE
  );

  CREATE TABLE Actividad (
    idActividad INT AUTO_INCREMENT NOT NULL,
    nombre VARCHAR(100) NULL,
    descripcion LONGTEXT NULL,
    precio DECIMAL(10,2) NULL,
    Id_Destino INT NOT NULL,
    PRIMARY KEY (idActividad, Id_Destino),
    INDEX fk_Actividad_Destino1_idx (Id_Destino ASC),
    FOREIGN KEY (Id_Destino) REFERENCES Destino (idDestino)
      ON DELETE CASCADE ON UPDATE CASCADE
  );

  CREATE TABLE Transporte (
    id_Transporte INT AUTO_INCREMENT NOT NULL,
    tipo VARCHAR(50) NULL,
    Empresa VARCHAR(100) NULL,
    Origen VARCHAR(100) NULL,
    Destino VARCHAR(100) NULL,
    Fecha_salida DATETIME NULL,
    Fecha_llegada DATETIME NULL,
    Precio DECIMAL(10,2) NULL,
    Viaje_idViaje INT NOT NULL,
    Viaje_Id_Destino INT NOT NULL,
    Viaje_Id_Cliente INT NOT NULL,
    PRIMARY KEY (id_Transporte, Viaje_idViaje, Viaje_Id_Destino, Viaje_Id_Cliente),
    INDEX fk_Transporte_Viaje1_idx (Viaje_idViaje ASC, Viaje_Id_Destino ASC, Viaje_Id_Cliente ASC),
    FOREIGN KEY (Viaje_idViaje, Viaje_Id_Destino, Viaje_Id_Cliente) REFERENCES Viaje (idViaje, Id_Destino, Id_Cliente)
      ON DELETE CASCADE ON UPDATE CASCADE
  );

  CREATE TABLE Habitacion (
    idHabitacion INT AUTO_INCREMENT NOT NULL,
    tipo VARCHAR(50) NULL,
    precio_noche DECIMAL(10,2) NULL,
    Capacidad INT NULL,
    disponible TINYINT NULL,
    Id_Hotel INT NOT NULL,
    Hotel_Id_Destino INT NOT NULL,
    PRIMARY KEY (idHabitacion, Id_Hotel, Hotel_Id_Destino),
    INDEX fk_Habitacion_Hotel1_idx (Id_Hotel ASC, Hotel_Id_Destino ASC),
    FOREIGN KEY (Id_Hotel, Hotel_Id_Destino) REFERENCES Hotel (idHotel, Id_Destino)
      ON DELETE CASCADE ON UPDATE CASCADE
  );

  CREATE TABLE Reseña (
    id_Reseña INT AUTO_INCREMENT NOT NULL,
    Puntuacion INT NULL,
    Comentario LONGTEXT NULL,
    fecha DATE NULL,
    Cliente_idCliente INT NOT NULL,
    Destino_idDestino INT NOT NULL,
    PRIMARY KEY (id_Reseña, Cliente_idCliente, Destino_idDestino),
    INDEX fk_Reseñas_Cliente1_idx (Cliente_idCliente ASC),
    INDEX fk_Reseñas_Destino1_idx (Destino_idDestino ASC),
    FOREIGN KEY (Cliente_idCliente) REFERENCES Cliente (idCliente)
      ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (Destino_idDestino) REFERENCES Destino (idDestino)
      ON DELETE CASCADE ON UPDATE CASCADE
  );

  CREATE TABLE Empleado (
    id_Empleado INT AUTO_INCREMENT PRIMARY KEY NOT NULL UNIQUE,
    Nombre VARCHAR(50) NULL,
    Apellidos VARCHAR(80) NULL,
    Telefono VARCHAR(20) NULL,
    Correo VARCHAR(100) NULL,
    Puesto VARCHAR(50) NULL
  );

  CREATE TABLE Agente (
    id_Agente INT AUTO_INCREMENT NOT NULL,
    nombre VARCHAR(50) NULL,
    telefono VARCHAR(20) NULL,
    email VARCHAR(100) NULL,
    Empleado_id_Empleado INT NOT NULL,
    PRIMARY KEY (id_Agente, Empleado_id_Empleado),
    INDEX fk_Agente_Empleado1_idx (Empleado_id_Empleado ASC),
    FOREIGN KEY (Empleado_id_Empleado) REFERENCES Empleado (id_Empleado)
      ON DELETE CASCADE ON UPDATE CASCADE
  );

  CREATE TABLE Viaje_has_Actividad (
    Viaje_idViaje INT NOT NULL,
    Viaje_Id_Destino INT NOT NULL,
    Viaje_Id_Cliente INT NOT NULL,
    Actividad_idActividad INT NOT NULL,
    Actividad_Id_Destino INT NOT NULL,
    PRIMARY KEY (Viaje_idViaje, Viaje_Id_Destino, Viaje_Id_Cliente, Actividad_idActividad, Actividad_Id_Destino),
    INDEX fk_Viaje_has_Actividad_Actividad1_idx (Actividad_idActividad ASC, Actividad_Id_Destino ASC),
    INDEX fk_Viaje_has_Actividad_Viaje1_idx (Viaje_idViaje ASC, Viaje_Id_Destino ASC, Viaje_Id_Cliente ASC),
    FOREIGN KEY (Viaje_idViaje, Viaje_Id_Destino, Viaje_Id_Cliente)
      REFERENCES Viaje (idViaje, Id_Destino, Id_Cliente)
      ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (Actividad_idActividad, Actividad_Id_Destino)
      REFERENCES Actividad (idActividad, Id_Destino)
      ON DELETE CASCADE ON UPDATE CASCADE
  );

  CREATE TABLE Vuelo (
    id_Vuelo INT AUTO_INCREMENT NOT NULL,
    Aerolinea VARCHAR(100) NULL,
    Numero_vuelo VARCHAR(20) NULL,
    Transporte_id_Transporte INT NOT NULL,
    Transporte_Viaje_idViaje INT NOT NULL,
    Transporte_Viaje_Id_Destino INT NOT NULL,
    Transporte_Viaje_Id_Cliente INT NOT NULL,
    PRIMARY KEY (id_Vuelo, Transporte_id_Transporte, Transporte_Viaje_idViaje, Transporte_Viaje_Id_Destino,
  Transporte_Viaje_Id_Cliente),
    INDEX fk_Vuelo_Transporte1_idx (Transporte_id_Transporte ASC, Transporte_Viaje_idViaje ASC,
  Transporte_Viaje_Id_Destino ASC, Transporte_Viaje_Id_Cliente ASC),
    FOREIGN KEY (Transporte_id_Transporte, Transporte_Viaje_idViaje, Transporte_Viaje_Id_Destino,
  Transporte_Viaje_Id_Cliente)
      REFERENCES Transporte (id_Transporte, Viaje_idViaje, Viaje_Id_Destino, Viaje_Id_Cliente)
      ON DELETE CASCADE ON UPDATE CASCADE
  );

  CREATE TABLE Pago (
    id_Pago INT AUTO_INCREMENT NOT NULL,
    Importe DECIMAL(10,2) NULL,
    Metodo_pago VARCHAR(50) NULL,
    fecha_pago DATE NULL,
    estado_pago VARCHAR(50) NULL,
    Reserva_id_Reserva INT NOT NULL,
    Reserva_Cliente_idCliente INT NOT NULL,
    Reserva_Viaje_idViaje INT NOT NULL,
    Reserva_Viaje_Id_Destino INT NOT NULL,
    Reserva_Viaje_Id_Cliente INT NOT NULL,
    PRIMARY KEY (id_Pago, Reserva_id_Reserva, Reserva_Cliente_idCliente, Reserva_Viaje_idViaje,
  Reserva_Viaje_Id_Destino, Reserva_Viaje_Id_Cliente),
    INDEX fk_Pago_Reserva1_idx (Reserva_id_Reserva ASC, Reserva_Cliente_idCliente ASC, Reserva_Viaje_idViaje ASC,
  Reserva_Viaje_Id_Destino ASC, Reserva_Viaje_Id_Cliente ASC),
    FOREIGN KEY (Reserva_id_Reserva, Reserva_Cliente_idCliente, Reserva_Viaje_idViaje, Reserva_Viaje_Id_Destino,
  Reserva_Viaje_Id_Cliente)
      REFERENCES Reserva (id_Reserva, Cliente_idCliente, Viaje_idViaje, Viaje_Id_Destino, Viaje_Id_Cliente)
      ON DELETE CASCADE ON UPDATE CASCADE
  );

  -- ✔️ CORRECCIÓN 2: fehca_inicio → fecha_inicio
  CREATE TABLE Promociones (
    id_Promociones INT AUTO_INCREMENT PRIMARY KEY NOT NULL UNIQUE,
    codigo VARCHAR(50) NULL,
    descuento DECIMAL(10,2) NULL,
    fecha_inicio DATE NULL,
    fecha_fin DATE NULL
  );

  CREATE TABLE Promociones_has_Viaje (
    Promociones_id_Promociones INT NOT NULL,
    Viaje_idViaje INT NOT NULL,
    Viaje_Id_Destino INT NOT NULL,
    Viaje_Id_Cliente INT NOT NULL,
    PRIMARY KEY (Promociones_id_Promociones, Viaje_idViaje, Viaje_Id_Destino, Viaje_Id_Cliente),
    INDEX fk_Promociones_has_Viaje_Viaje1_idx (Viaje_idViaje ASC, Viaje_Id_Destino ASC, Viaje_Id_Cliente ASC),
    INDEX fk_Promociones_has_Viaje_Promociones1_idx (Promociones_id_Promociones ASC),
    FOREIGN KEY (Promociones_id_Promociones) REFERENCES Promociones (id_Promociones)
      ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (Viaje_idViaje, Viaje_Id_Destino, Viaje_Id_Cliente) REFERENCES Viaje (idViaje, Id_Destino, Id_Cliente)
      ON DELETE CASCADE ON UPDATE CASCADE
  );

  SET SQL_MODE=@OLD_SQL_MODE;
  SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
  SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
  
  
  -- =====================================================
  -- ÍNDICES
  -- =====================================================

  -- Acelera el login buscando por Email
  CREATE INDEX idx_cliente_email ON Cliente(Email);

  -- Acelera consultas de viajes filtradas por fecha
  CREATE INDEX idx_viaje_fecha ON Viaje(fecha_inicio);

  -- Acelera filtros de reservas por estado (Pendiente, Confirmada, Cancelada)
  CREATE INDEX idx_reserva_estado ON Reserva(estado);


  -- =====================================================
  -- VISTAS
  -- =====================================================

  -- Vista 1: Reservas completas con datos del cliente, viaje y destino
  CREATE VIEW vista_reservas_completas AS
  SELECT
      r.id_Reserva,
      r.Fecha_Reserva,
      r.estado,
      r.num_personas,
      r.precio_total        AS precio_reserva,
      c.idCliente,
      c.nomCliente,
      c.apCliente,
      c.Email,
      d.nombre              AS destino,
      d.pais,
      v.fecha_inicio,
      v.fecha_fin
  FROM Reserva r
  JOIN Cliente c ON r.Cliente_idCliente = c.idCliente
  JOIN Viaje   v ON r.Viaje_idViaje     = v.idViaje
                AND r.Viaje_Id_Destino  = v.Id_Destino
                AND r.Viaje_Id_Cliente  = v.Id_Cliente
  JOIN Destino d ON v.Id_Destino        = d.idDestino;


  -- Vista 2: Destinos ordenados por número de viajes (popularidad)
  CREATE VIEW vista_destinos_populares AS
  SELECT
      d.idDestino,
      d.nombre,
      d.pais,
      d.precio_base,
      COUNT(v.idViaje) AS total_viajes
  FROM Destino d
  LEFT JOIN Viaje v ON d.idDestino = v.Id_Destino
  GROUP BY d.idDestino, d.nombre, d.pais, d.precio_base
  ORDER BY total_viajes DESC;


  -- Vista 3: Resumen por cliente — número de reservas y gasto total
  CREATE VIEW vista_clientes_resumen AS
  SELECT
      c.idCliente,
      c.nomCliente,
      c.apCliente,
      c.Email,
      c.nomUsuario,
      COUNT(r.id_Reserva)          AS total_reservas,
      COALESCE(SUM(r.precio_total), 0) AS total_gastado
  FROM Cliente c
  LEFT JOIN Reserva r ON c.idCliente = r.Cliente_idCliente
  GROUP BY c.idCliente, c.nomCliente, c.apCliente, c.Email, c.nomUsuario;


  -- =====================================================
  -- TRIGGERS
  -- =====================================================

  DELIMITER //

  -- Trigger 1: BEFORE INSERT Reserva
  -- Asigna fecha automática, valida estado y calcula precio_total
  CREATE TRIGGER trg_before_insert_reserva
  BEFORE INSERT ON Reserva
  FOR EACH ROW
  BEGIN
      DECLARE v_precio DECIMAL(10,2);

      -- Fecha automática si no se proporciona
      IF NEW.Fecha_Reserva IS NULL THEN
          SET NEW.Fecha_Reserva = NOW();
      END IF;

      -- Estado por defecto y validación
      IF NEW.estado IS NULL THEN
          SET NEW.estado = 'Pendiente';
      ELSEIF NEW.estado NOT IN ('Pendiente', 'Confirmada', 'Cancelada') THEN
          SIGNAL SQLSTATE '45000'
          SET MESSAGE_TEXT = 'Estado no válido. Use: Pendiente, Confirmada o Cancelada';
      END IF;

      -- Calcular precio_total automáticamente si no se indicó
      IF NEW.precio_total IS NULL OR NEW.precio_total = 0 THEN
          SELECT d.precio_base INTO v_precio
          FROM Destino d
          JOIN Viaje v ON d.idDestino = v.Id_Destino
          WHERE v.idViaje      = NEW.Viaje_idViaje
            AND v.Id_Destino   = NEW.Viaje_Id_Destino
            AND v.Id_Cliente   = NEW.Viaje_Id_Cliente
          LIMIT 1;
          SET NEW.precio_total = COALESCE(v_precio, 0) * COALESCE(NEW.num_personas, 1);
      END IF;
  END //


  -- Trigger 2: BEFORE UPDATE Reserva
  -- Impide reactivar una reserva cancelada y pone precio a 0 al cancelar
  CREATE TRIGGER trg_before_update_reserva
  BEFORE UPDATE ON Reserva
  FOR EACH ROW
  BEGIN
      IF OLD.estado = 'Cancelada' AND NEW.estado != 'Cancelada' THEN
          SIGNAL SQLSTATE '45000'
          SET MESSAGE_TEXT = 'No se puede reactivar una reserva cancelada';
      END IF;

      IF NEW.estado = 'Cancelada' AND OLD.estado != 'Cancelada' THEN
          SET NEW.precio_total = 0.00;
      END IF;
  END //


  -- Trigger 3: BEFORE INSERT Reseña
  -- Valida que la puntuación esté entre 1 y 5 y asigna fecha actual
  CREATE TRIGGER trg_before_insert_resena
  BEFORE INSERT ON Reseña
  FOR EACH ROW
  BEGIN
      IF NEW.Puntuacion IS NOT NULL AND (NEW.Puntuacion < 1 OR NEW.Puntuacion > 5) THEN
          SIGNAL SQLSTATE '45000'
          SET MESSAGE_TEXT = 'La puntuación debe estar entre 1 y 5';
      END IF;

      IF NEW.fecha IS NULL THEN
          SET NEW.fecha = CURDATE();
      END IF;
  END //


  -- Trigger 4: BEFORE UPDATE Reseña
  -- Valida la puntuación también al editarla
  CREATE TRIGGER trg_before_update_resena
  BEFORE UPDATE ON Reseña
  FOR EACH ROW
  BEGIN
      IF NEW.Puntuacion IS NOT NULL AND (NEW.Puntuacion < 1 OR NEW.Puntuacion > 5) THEN
          SIGNAL SQLSTATE '45000'
          SET MESSAGE_TEXT = 'La puntuación debe estar entre 1 y 5';
      END IF;
  END //


  -- Trigger 5: BEFORE INSERT Cliente
  -- Valida que el email contenga '@' antes de registrar
  CREATE TRIGGER trg_validar_email_cliente
  BEFORE INSERT ON Cliente
  FOR EACH ROW
  BEGIN
      IF NEW.Email IS NOT NULL AND NEW.Email NOT LIKE '%@%' THEN
          SIGNAL SQLSTATE '45000'
          SET MESSAGE_TEXT = 'El formato del email no es válido';
      END IF;
  END //

  DELIMITER ;


  -- =====================================================
  -- PROCEDIMIENTOS ALMACENADOS
  -- =====================================================

  DELIMITER //

  -- Procedimiento 1: Login de cliente
  -- Devuelve resultado=1 si las credenciales son correctas, 0 si no
  CREATE PROCEDURE sp_login_cliente(
      IN  p_email      VARCHAR(100),
      IN  p_contrasena VARCHAR(20),
      OUT p_resultado  INT,
      OUT p_idCliente  INT,
      OUT p_nombre     VARCHAR(50)
  )
  BEGIN
      SELECT idCliente, nomCliente
      INTO   p_idCliente, p_nombre
      FROM   Cliente
      WHERE  Email = p_email AND Contraseña = p_contrasena
      LIMIT 1;

      IF p_idCliente IS NOT NULL THEN
          SET p_resultado = 1;
      ELSE
          SET p_resultado = 0;
          SET p_nombre    = NULL;
      END IF;
  END //


  -- Procedimiento 2: Registrar nuevo cliente
  -- Devuelve resultado=1 si ok, -1 si el email ya existe
  CREATE PROCEDURE sp_registrar_cliente(
      IN  p_nombre     VARCHAR(50),
      IN  p_apellido   VARCHAR(50),
      IN  p_email      VARCHAR(100),
      IN  p_telefono   VARCHAR(20),
      IN  p_contrasena VARCHAR(20),
      IN  p_nomUsuario VARCHAR(30),
      OUT p_resultado  INT,
      OUT p_idCliente  INT
  )
  BEGIN
      DECLARE v_existe INT DEFAULT 0;

      SELECT COUNT(*) INTO v_existe FROM Cliente WHERE Email = p_email;

      IF v_existe > 0 THEN
          SET p_resultado = -1;
          SET p_idCliente = NULL;
      ELSE
          INSERT INTO Cliente (nomCliente, apCliente, Email, Telefono, Contraseña, nomUsuario)
          VALUES (p_nombre, p_apellido, p_email, p_telefono, p_contrasena, p_nomUsuario);
          SET p_idCliente = LAST_INSERT_ID();
          SET p_resultado = 1;
      END IF;
  END //


  -- Procedimiento 3: Obtener todos los destinos disponibles
  CREATE PROCEDURE sp_obtener_destinos()
  BEGIN
      SELECT idDestino, nombre, pais, descripcion, precio_base
      FROM   Destino
      ORDER  BY nombre ASC;
  END //


  -- Procedimiento 4: Obtener todas las reservas de un cliente
  CREATE PROCEDURE sp_reservas_cliente(
      IN p_idCliente INT
  )
  BEGIN
      SELECT
          r.id_Reserva,
          r.Fecha_Reserva,
          r.estado,
          r.num_personas,
          r.precio_total,
          d.nombre      AS destino,
          d.pais,
          v.fecha_inicio,
          v.fecha_fin
      FROM Reserva r
      JOIN Viaje   v ON r.Viaje_idViaje    = v.idViaje
                    AND r.Viaje_Id_Destino = v.Id_Destino
                    AND r.Viaje_Id_Cliente = v.Id_Cliente
      JOIN Destino d ON v.Id_Destino       = d.idDestino
      WHERE r.Cliente_idCliente = p_idCliente
      ORDER BY r.Fecha_Reserva DESC;
  END //


  -- Procedimiento 5: Crear una nueva reserva
  -- precio_total se calcula solo gracias al trigger trg_before_insert_reserva
  CREATE PROCEDURE sp_crear_reserva(
      IN  p_idCliente   INT,
      IN  p_idViaje     INT,
      IN  p_idDestino   INT,
      IN  p_numPersonas INT,
      OUT p_resultado   INT,
      OUT p_idReserva   INT
  )
  BEGIN
      DECLARE EXIT HANDLER FOR SQLEXCEPTION
      BEGIN
          SET p_resultado = -1;
          SET p_idReserva = NULL;
          ROLLBACK;
      END;

      START TRANSACTION;

      INSERT INTO Reserva (
          Fecha_Reserva, estado, num_personas, precio_total,
          Cliente_idCliente,
          Viaje_idViaje, Viaje_Id_Destino, Viaje_Id_Cliente
      )
      VALUES (
          NOW(), 'Pendiente', p_numPersonas, 0,
          p_idCliente,
          p_idViaje, p_idDestino, p_idCliente
      );

      SET p_idReserva = LAST_INSERT_ID();
      SET p_resultado = 1;

      COMMIT;
  END //

  DELIMITER ;
  INSERT INTO Cliente (nomCliente, apCliente, Email, Telefono, Contraseña, nomUsuario)
  VALUES ('Mario', 'Lopez', 'mario@skyroute.com', '600000000', '1234', 'mario');
  
  ALTER TABLE Cliente ADD COLUMN record_juego INT DEFAULT 0;

  DELIMITER //
  CREATE PROCEDURE sp_guardar_record(
      IN p_idCliente  INT,
      IN p_puntuacion INT
  )
  BEGIN
      -- Solo actualiza si la nueva puntuación supera el record actual
      UPDATE Cliente
      SET record_juego = p_puntuacion
      WHERE idCliente = p_idCliente
        AND p_puntuacion > COALESCE(record_juego, 0);
  END //
  DELIMITER ;
  
  SELECT nomCliente, apCliente, Email, record_juego
  FROM Cliente
  WHERE record_juego > 0
  ORDER BY record_juego DESC;
  INSERT INTO Destino (nombre, pais, descripcion, precio_base) VALUES
  ('Roma',      'Italia',                'La Ciudad Eterna, cuna del Imperio Romano.',         1200.00),
  ('Tokio',     'Japón',                 'Metrópolis futurista con tradición milenaria.',       1800.00),
  ('Paris',     'Francia',               'La Ciudad de la Luz, capital de la moda y el arte.', 1100.00),
  ('Dubai',     'Emiratos Arabes Unidos','Ciudad del lujo y la arquitectura moderna.',          1500.00),
  ('NuevaYork', 'Estados Unidos',        'La Gran Manzana, ciudad que nunca duerme.',           1600.00),
  ('Londres',   'Reino Unido',           'Capital histórica a orillas del Támesis.',            1300.00),
  ('Bali',      'Indonesia',             'Isla de los dioses, playas y templos.',               1000.00),
  ('Tailandia', 'Tailandia',             'Tierra de sonrisas, templos y playas tropicales.',    950.00),
  ('Maldivas',  'Maldivas',              'Paraíso de atolones y aguas cristalinas.',            2200.00);
  
  SELECT
      v.idViaje,
      c.nomCliente,
      c.nomUsuario,
      d.nombre AS destino,
      v.fecha_inicio,
      v.fecha_fin,
      v.precio_total
  FROM Viaje v
  JOIN Cliente c ON v.Id_Cliente = c.idCliente
  JOIN Destino d ON v.Id_Destino = d.idDestino
  ORDER BY v.idViaje DESC;

SELECT
      v.idViaje,
      c.nomUsuario,
      d.nombre              AS destino,
      v.fecha_inicio,
      v.fecha_fin,
      v.precio_total,
      r.estado              AS estado_reserva,
      h.nombre              AS hotel,
      h.categoria           AS estrellas,
      t.Empresa             AS piloto,
      t.tipo                AS avion,
      vl.Aerolinea,
      vl.Numero_vuelo
  FROM Viaje v
  JOIN Cliente    c  ON v.Id_Cliente       = c.idCliente
  JOIN Destino    d  ON v.Id_Destino       = d.idDestino
  LEFT JOIN Reserva   r  ON r.Viaje_idViaje    = v.idViaje
                        AND r.Viaje_Id_Destino = v.Id_Destino
                        AND r.Viaje_Id_Cliente = v.Id_Cliente
  LEFT JOIN Transporte t  ON t.Viaje_idViaje    = v.idViaje
                        AND t.Viaje_Id_Destino  = v.Id_Destino
                        AND t.Viaje_Id_Cliente  = v.Id_Cliente
  LEFT JOIN Vuelo     vl ON vl.Transporte_id_Transporte      = t.id_Transporte
                        AND vl.Transporte_Viaje_idViaje       = t.Viaje_idViaje
  LEFT JOIN Hotel     h  ON h.Id_Destino = v.Id_Destino
  ORDER BY v.idViaje DESC;