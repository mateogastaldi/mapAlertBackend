CREATE DATABASE IF NOT EXISTS mapAlert;
USE mapAlert;

CREATE TABLE usuarios (
id_usuario BIGINT AUTO_INCREMENT PRIMARY KEY,
usuario VARCHAR(50) NOT NULL UNIQUE,
contrasena VARCHAR(255) NOT NULL,
nombres VARCHAR(50) NOT NULL,
apellidos VARCHAR(50) NOT NULL,
email VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE cliente (
id_usuario BIGINT PRIMARY KEY,
FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
);

CREATE TABLE administrador (
id_usuario BIGINT PRIMARY KEY,
FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
);

CREATE TABLE reporte (
id_reporte BIGINT AUTO_INCREMENT PRIMARY KEY,
id_usuario BIGINT NOT NULL,
latitud VARCHAR(15) NOT NULL,
longitud VARCHAR(15) NOT NULL,
fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
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
'OTRO'
) NOT NULL,
descripcion VARCHAR(255),
FOREIGN KEY (id_usuario) REFERENCES cliente(id_usuario) ON DELETE CASCADE
);


CREATE TABLE validacion_reporte (
id_validacion BIGINT AUTO_INCREMENT PRIMARY KEY,
id_reporte BIGINT NOT NULL,
id_usuario BIGINT NOT NULL,
validacion INT NOT NULL,
FOREIGN KEY (id_reporte) REFERENCES reporte(id_reporte) ON DELETE CASCADE,
FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
);

-- las validaicones pense q sean estrellas, 1 no es real, 5 es lo q dice el reporte. validacion seria un numero del 1 al 5 -->
-- tabla cliente y administrador heredan de usuarios -->
--podria ponerse otro campo en reporte de calificacion?-->