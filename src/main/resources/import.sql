INSERT INTO `usuariox` (username, password, enabled, nombre, apellido, email) VALUES ('pepe','$2a$10$LbLt5f9q1343z9Rcj4k/bOx6PqjUut2DdLlNR9p4OIqqX1PaFytRW',1, 'Pepe', 'Pepe','pepe@gmail.com');
INSERT INTO `usuariox` (username, password, enabled, nombre, apellido, email) VALUES ('admin','$2a$10$eetiP6eE17DNRRQSl7P3qes6XMxHydf.u/RrH38znr204Nx8gvMoO',1, 'Admin', 'Admin','admin@gmail.com');

INSERT INTO `roles` (nombre) VALUES ('ROLE_USER');
INSERT INTO `roles` (nombre) VALUES ('ROLE_ADMIN');

INSERT INTO `usuarios_roles` (usuario_id, role_id) VALUES (1, 1);
INSERT INTO `usuarios_roles` (usuario_id, role_id) VALUES (2, 2);
INSERT INTO `usuarios_roles` (usuario_id, role_id) VALUES (2, 1);