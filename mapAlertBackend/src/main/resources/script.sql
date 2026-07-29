CREATE DATABASE IF NOT EXISTS mapAlert;
USE mapAlert;

CREATE TABLE usuarios (
  id_usuario BIGINT AUTO_INCREMENT PRIMARY KEY,
  usuario VARCHAR(50) NOT NULL UNIQUE,
  contrasena VARCHAR(255) NOT NULL,
  nombres VARCHAR(50) NOT NULL,
  apellidos VARCHAR(50) NOT NULL,
  email VARCHAR(100) NOT NULL UNIQUE,
  rol VARCHAR(20) NOT NULL,
  activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE reporte (
  id_reporte BIGINT AUTO_INCREMENT PRIMARY KEY,
  id_usuario BIGINT NOT NULL,
  latitud DOUBLE NOT NULL,
  longitud DOUBLE NOT NULL,
  fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  fecha_desactivacion DATETIME,
  calle VARCHAR(100),
  numero_calle INT,
  ciudad VARCHAR(50) NOT NULL,
  provincia VARCHAR(50) NOT NULL,
  pais VARCHAR(50) NOT NULL,
  tipo_reporte ENUM(
    'BACHE',
    'ACCIDENTE',
    'CALLE_SIN_LUZ',
    'CORTE_DE_LUZ',
    'BASURA',
    'FALTA_DE_AGUA',
    'OTRO'
  ) NOT NULL,
  descripcion VARCHAR(255),
  activo BOOLEAN NOT NULL DEFAULT TRUE,
  FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
);

CREATE TABLE voto_reporte (
  id_voto_reporte BIGINT AUTO_INCREMENT PRIMARY KEY,
  reporte_id BIGINT NOT NULL,
  usuario_id BIGINT NOT NULL,
  tipo_voto VARCHAR(20) NOT NULL,
  fecha_voto DATETIME NOT NULL,
  FOREIGN KEY (reporte_id) REFERENCES reporte(id_reporte) ON DELETE CASCADE,
  FOREIGN KEY (usuario_id) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
);

CREATE TABLE calificacion (
  id_calificacion BIGINT AUTO_INCREMENT PRIMARY KEY,
  reporte_id BIGINT NOT NULL,
  usuario_id BIGINT NOT NULL,
  puntaje INT NOT NULL,
  FOREIGN KEY (reporte_id) REFERENCES reporte(id_reporte) ON DELETE CASCADE,
  FOREIGN KEY (usuario_id) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
);

INSERT INTO usuarios (
    usuario,
    contrasena,
    nombres,
    apellidos,
    email,
    rol,
    activo
) VALUES (
    'emma',
    '123456',
    'Emma',
    'Mazzuferi',
    'emma@gmail.com',
    'USUARIO',
    TRUE
);