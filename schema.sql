-- MySQL dump 10.13  Distrib 5.7.19, for Linux (x86_64)
--
-- Host: localhost    Database: mads
-- ------------------------------------------------------
-- Server version	5.7.19

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Comentario`
--

DROP TABLE IF EXISTS `Comentario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Comentario` (
  `id` bigint(20) NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `creador` bigint(20) DEFAULT NULL,
  `tareaId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKnidpgo0e2gp3sf00kckd5yv4j` (`creador`),
  KEY `FKqaf2c0nv49vyle281btvqbluw` (`tareaId`),
  CONSTRAINT `FKnidpgo0e2gp3sf00kckd5yv4j` FOREIGN KEY (`creador`) REFERENCES `Usuario` (`id`),
  CONSTRAINT `FKqaf2c0nv49vyle281btvqbluw` FOREIGN KEY (`tareaId`) REFERENCES `Tarea` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Comentario`
--

LOCK TABLES `Comentario` WRITE;
/*!40000 ALTER TABLE `Comentario` DISABLE KEYS */;
INSERT INTO `Comentario` VALUES (1000,'Es correcto',1000,1000),(1001,'Comentario 2',1000,1000);
/*!40000 ALTER TABLE `Comentario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Grupo`
--

DROP TABLE IF EXISTS `Grupo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Grupo` (
  `id` bigint(20) NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `administradorId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKj56w9u3eh2udx6cyuwyxkamhs` (`administradorId`),
  CONSTRAINT `FKj56w9u3eh2udx6cyuwyxkamhs` FOREIGN KEY (`administradorId`) REFERENCES `Usuario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Grupo`
--

LOCK TABLES `Grupo` WRITE;
/*!40000 ALTER TABLE `Grupo` DISABLE KEYS */;
INSERT INTO `Grupo` VALUES (1000,'Grupo test 1',1000),(1001,'Grupo test 2',1000);
/*!40000 ALTER TABLE `Grupo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Persona_Grupo`
--

DROP TABLE IF EXISTS `Persona_Grupo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Persona_Grupo` (
  `grupos_id` bigint(20) NOT NULL,
  `participantes_id` bigint(20) NOT NULL,
  PRIMARY KEY (`grupos_id`,`participantes_id`),
  KEY `FK90268hocb3nlewnk0t5h37n9i` (`participantes_id`),
  CONSTRAINT `FK90268hocb3nlewnk0t5h37n9i` FOREIGN KEY (`participantes_id`) REFERENCES `Usuario` (`id`),
  CONSTRAINT `FKtp4pocn95lrc2lt27sn4m0l50` FOREIGN KEY (`grupos_id`) REFERENCES `Grupo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Persona_Grupo`
--

LOCK TABLES `Persona_Grupo` WRITE;
/*!40000 ALTER TABLE `Persona_Grupo` DISABLE KEYS */;
/*!40000 ALTER TABLE `Persona_Grupo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Persona_Tablero`
--

DROP TABLE IF EXISTS `Persona_Tablero`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Persona_Tablero` (
  `tableros_id` bigint(20) NOT NULL,
  `participantes_id` bigint(20) NOT NULL,
  PRIMARY KEY (`tableros_id`,`participantes_id`),
  KEY `FKnghbrhyh7eal30o78h3293n72` (`participantes_id`),
  CONSTRAINT `FKbpw5yq3ofgud0ra8a916kddjm` FOREIGN KEY (`tableros_id`) REFERENCES `Tablero` (`id`),
  CONSTRAINT `FKnghbrhyh7eal30o78h3293n72` FOREIGN KEY (`participantes_id`) REFERENCES `Usuario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Persona_Tablero`
--

LOCK TABLES `Persona_Tablero` WRITE;
/*!40000 ALTER TABLE `Persona_Tablero` DISABLE KEYS */;
/*!40000 ALTER TABLE `Persona_Tablero` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Size`
--

DROP TABLE IF EXISTS `Size`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Size` (
  `id` bigint(20) NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Size`
--

LOCK TABLES `Size` WRITE;
/*!40000 ALTER TABLE `Size` DISABLE KEYS */;
INSERT INTO `Size` VALUES (1000,'Small'),(1001,'Medium'),(1002,'Large');
/*!40000 ALTER TABLE `Size` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Size_Tablero`
--

DROP TABLE IF EXISTS `Size_Tablero`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Size_Tablero` (
  `tableros_id` bigint(20) NOT NULL,
  `tareaSize_id` bigint(20) NOT NULL,
  PRIMARY KEY (`tableros_id`,`tareaSize_id`),
  KEY `FKasq6lp9gy5bjsk933m2uhx33b` (`tareaSize_id`),
  CONSTRAINT `FKasq6lp9gy5bjsk933m2uhx33b` FOREIGN KEY (`tareaSize_id`) REFERENCES `Size` (`id`),
  CONSTRAINT `FKdiesu7o5lj0xmodfrbfjrrixs` FOREIGN KEY (`tableros_id`) REFERENCES `Tablero` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Size_Tablero`
--

LOCK TABLES `Size_Tablero` WRITE;
/*!40000 ALTER TABLE `Size_Tablero` DISABLE KEYS */;
/*!40000 ALTER TABLE `Size_Tablero` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Tablero`
--

DROP TABLE IF EXISTS `Tablero`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Tablero` (
  `id` bigint(20) NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `administradorId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKq82919iay2b8h77msdj8289p0` (`administradorId`),
  CONSTRAINT `FKq82919iay2b8h77msdj8289p0` FOREIGN KEY (`administradorId`) REFERENCES `Usuario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Tablero`
--

LOCK TABLES `Tablero` WRITE;
/*!40000 ALTER TABLE `Tablero` DISABLE KEYS */;
INSERT INTO `Tablero` VALUES (1000,'Tablero test 1',1000),(1001,'Tablero test 2',1000);
/*!40000 ALTER TABLE `Tablero` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Tarea`
--

DROP TABLE IF EXISTS `Tarea`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Tarea` (
  `id` bigint(20) NOT NULL,
  `fechaCreacion` datetime DEFAULT NULL,
  `fechaLimite` datetime DEFAULT NULL,
  `terminada` bit(1) NOT NULL,
  `titulo` varchar(255) DEFAULT NULL,
  `sizeId` bigint(20) DEFAULT NULL,
  `tableroId` bigint(20) DEFAULT NULL,
  `usuarioId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKm7hqht5iupgvdf5y0g1arn2di` (`sizeId`),
  KEY `FK2r7tsv4xu3bjvst83o8xuspud` (`tableroId`),
  KEY `FKepne2t52y8dmn8l9da0dd7l51` (`usuarioId`),
  CONSTRAINT `FK2r7tsv4xu3bjvst83o8xuspud` FOREIGN KEY (`tableroId`) REFERENCES `Tablero` (`id`),
  CONSTRAINT `FKepne2t52y8dmn8l9da0dd7l51` FOREIGN KEY (`usuarioId`) REFERENCES `Usuario` (`id`),
  CONSTRAINT `FKm7hqht5iupgvdf5y0g1arn2di` FOREIGN KEY (`sizeId`) REFERENCES `Size` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Tarea`
--

LOCK TABLES `Tarea` WRITE;
/*!40000 ALTER TABLE `Tarea` DISABLE KEYS */;
INSERT INTO `Tarea` VALUES (29,'2017-12-16 12:37:04',NULL,'\0','Renovar DNI',NULL,NULL,28),(1000,'2017-09-09 16:32:00','2017-12-05 00:00:00','\0','Renovar DNI',1000,1000,1000),(1001,'2017-09-29 23:43:00','2017-12-09 00:00:00','\0','Práctica 1 MADS',1001,1001,1000),(1002,'2017-09-15 01:15:00','2017-11-29 00:00:00','\0','Práctica 2 MADS',1000,1000,1000);
/*!40000 ALTER TABLE `Tarea` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Usuario`
--

DROP TABLE IF EXISTS `Usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Usuario` (
  `id` bigint(20) NOT NULL,
  `apellidos` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `fechaNacimiento` date DEFAULT NULL,
  `login` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Usuario`
--

LOCK TABLES `Usuario` WRITE;
/*!40000 ALTER TABLE `Usuario` DISABLE KEYS */;
INSERT INTO `Usuario` VALUES (28,NULL,'juangutierrez@gmail.com',NULL,'juangutierrez',NULL,NULL),(1000,'Gutierrez','juan.gutierrez@gmail.com','1993-12-10','juangutierrez','Juan','123456789'),(1001,'Gutierrez Dos','juan.gutierrez@gmail.com','1993-12-10','juangutierrez2','Juan','123456789'),(1002,'Gutierrez Tres','juan.gutierrez@gmail.com','1993-12-10','juangutierrez3','Juan','123456789'),(1100,'Gutierrez','pepe.gutierrez@gmail.com','1996-05-15','pepegutierrez','Pepe','12345');
/*!40000 ALTER TABLE `Usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hibernate_sequence`
--

DROP TABLE IF EXISTS `hibernate_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequence`
--

LOCK TABLES `hibernate_sequence` WRITE;
/*!40000 ALTER TABLE `hibernate_sequence` DISABLE KEYS */;
INSERT INTO `hibernate_sequence` VALUES (30),(30),(30),(30),(30),(30);
/*!40000 ALTER TABLE `hibernate_sequence` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-12-16 12:38:31
