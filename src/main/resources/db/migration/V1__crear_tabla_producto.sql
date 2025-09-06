CREATE TABLE producto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    url_imagen VARCHAR(512),
    descripcion TEXT,
    precio DECIMAL(10,2) NOT NULL,
    calificacion DECIMAL(2,1),
    especificaciones TEXT
);

