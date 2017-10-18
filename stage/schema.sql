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
-- Table structure for table `Persona_Tablero`
--

DROP TABLE IF EXISTS `Persona_Tablero`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Persona_Tablero` (
  `tableros_id` bigint(20) NOT NULL,
  `participantes_id` bigint(20) NOT NULL,
  KEY `FKnghbrhyh7eal30o78h3293n72` (`participantes_id`),
  KEY `FKbpw5yq3ofgud0ra8a916kddjm` (`tableros_id`),
  CONSTRAINT `FKbpw5yq3ofgud0ra8a916kddjm` FOREIGN KEY (`tableros_id`) REFERENCES `Tablero` (`id`),
  CONSTRAINT `FKnghbrhyh7eal30o78h3293n72` FOREIGN KEY (`participantes_id`) REFERENCES `Usuario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Persona_Tablero`
--

LOCK TABLES `Persona_Tablero` WRITE;
/*!40000 ALTER TABLE `Persona_Tablero` DISABLE KEYS */;
INSERT INTO `Persona_Tablero` VALUES (1000,1001),(1000,14),(1001,14);
/*!40000 ALTER TABLE `Persona_Tablero` ENABLE KEYS */;
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
INSERT INTO `Tablero` VALUES (16,'214',14),(1000,'Tablero test 1',1000),(1001,'Tablero test 2',1000);
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
  `titulo` varchar(255) DEFAULT NULL,
  `usuarioId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKepne2t52y8dmn8l9da0dd7l51` (`usuarioId`),
  CONSTRAINT `FKepne2t52y8dmn8l9da0dd7l51` FOREIGN KEY (`usuarioId`) REFERENCES `Usuario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Tarea`
--

LOCK TABLES `Tarea` WRITE;
/*!40000 ALTER TABLE `Tarea` DISABLE KEYS */;
INSERT INTO `Tarea` VALUES (15,'1235',14),(1000,'Renovar DNI',1000),(1001,'Práctica 1 MADS',1000),(1002,'Práctica 2 MADS',1000);
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
INSERT INTO `Usuario` VALUES (14,NULL,'quico14',NULL,'quico14',NULL,'1234'),(1000,'Gutierrez','juan.gutierrez@gmail.com','1993-12-10','juangutierrez','Juan','123456789'),(1001,'Gutierrez Dos','juan.gutierrez@gmail.com','1993-12-10','juangutierrez2','Juan','123456789'),(1002,'Gutierrez Tres','juan.gutierrez@gmail.com','1993-12-10','juangutierrez3','Juan','123456789'),(1100,'Gutierrez','pepe.gutierrez@gmail.com','1996-05-15','pepegutierrez','Pepe','12345');
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
INSERT INTO `hibernate_sequence` VALUES (17),(17);
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

-- Dump completed on 2017-10-18 11:56:50
