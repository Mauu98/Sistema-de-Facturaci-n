INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (1, 'Mauricio', 'Pino', 'mauri@gmail.com', '1998-09-11', ''); 
INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (2, 'Lionel', 'Messi', 'leito@gmail.com', '1987-04-12',''); 
INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (3, 'Cristiano', 'Ronaldo', 'cris@gmail.com', '1985-01-13',''); 
INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (4, 'Neymar', 'Junior', 'ney@gmail.com', '1994-06-04',''); 
INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (5, 'Wayne', 'Rooney', 'rooney@gmail.com', '1980-01-13',''); 
INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (6, 'Angel', 'DiMaria', 'dimaria@gmail.com', '1988-01-03',''); 
INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (7, 'Kylian', 'Mbappe', 'mbappe@gmail.com', '1998-01-12',''); 
INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (8, 'Francesco', 'Totti', 'totti@gmail.com', '1970-03-11',''); 
INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (9, 'Gareth', 'Bale', 'bale@gmail.com', '1986-03-03',''); 
INSERT INTO clientes (id, nombre, apellido, email, create_at, foto) VALUES (10, 'Sergio', 'Aguero', 'aguero@gmail.com', '1989-03-04',''); 


/*Populate tabla Productos */
INSERT INTO Productos(nombre, precio, create_at) VALUES ('Panasonic Pantalla LCD', 25990, NOW());
INSERT INTO Productos(nombre, precio, create_at) VALUES ('Sony Camara Digital', 17000, NOW());
INSERT INTO Productos(nombre, precio, create_at) VALUES ('Apple iPod', 11500, NOW());
INSERT INTO Productos(nombre, precio, create_at) VALUES ('Notebook ASUS 26P', 65000, NOW());
INSERT INTO Productos(nombre, precio, create_at) VALUES ('Bicicleta Tomaselli 2021', 34643, NOW());
INSERT INTO Productos(nombre, precio, create_at) VALUES ('Cajonera con 8 cajones', 12500, NOW());
INSERT INTO Productos(nombre, precio, create_at) VALUES ('Galaxy A10 64GB', 52500, NOW());

/*Populate tabla Facturas */
INSERT INTO facturas(descripcion, observacion, cliente_id, create_at) VALUES ('Facturas equipos de oficina', null, 1, NOW());
INSERT INTO facturas_items(cantidad, factura_id, producto_id) VALUES (1,1,1);
INSERT INTO facturas_items(cantidad, factura_id, producto_id) VALUES (2,1,4);
INSERT INTO facturas_items(cantidad, factura_id, producto_id) VALUES (1,1,5);
INSERT INTO facturas_items(cantidad, factura_id, producto_id) VALUES (1,1,7);

INSERT INTO facturas(descripcion, observacion, cliente_id, create_at) VALUES ('Facturas Bicicleta', 'Alguna nota importante!', 1, NOW());
INSERT INTO facturas_items(cantidad, factura_id, producto_id) VALUES (3,2,6);

ALTER TABLE `db_springboot`.`authorities` CHANGE COLUMN `id` `id` BIGINT(20) NOT NULL AUTO_INCREMENT ;

/*Insertando los USERS*/
INSERT INTO `users` (username, password, enable) VALUES ('admin','$2a$10$XKfSYa/nDV8Ch3yUyEiHFO2Uq6w82u0smPSZTk/0UDDIguzsSgrM.',1);
INSERT INTO `users` (username, password, enable) VALUES ('mauri','$2a$10$LXC7/ouWsijGeKqVHx88t.UrO6plXiPhZvr3Kee.lABH8nSJE3dWO',1);

INSERT INTO `authorities`(user_id, authority) VALUES (1, 'ROLE_ADMIN');
INSERT INTO `authorities`(user_id, authority) VALUES (1, 'ROLE_USER');
INSERT INTO `authorities`(user_id, authority) VALUES (2, 'ROLE_USER');