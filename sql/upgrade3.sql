
--
-- Table structure for table `Columna`
--

DROP TABLE IF EXISTS `Columna`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Columna` (
  `id` bigint(20) NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `tableroId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5t3s9tmlcxo05co7xxalympgg` (`tableroId`),
  CONSTRAINT `FK5t3s9tmlcxo05co7xxalympgg` FOREIGN KEY (`tableroId`) REFERENCES `Tablero` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Tarea`
--

DROP TABLE IF EXISTS `Tarea`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Tarea` (
  `id` bigint(20) NOT NULL,
  `fechaCreacion` datetime DEFAULT NULL,
  `fechaLimite` date DEFAULT NULL,
  `terminada` bit(1) DEFAULT NULL,
  `titulo` varchar(255) DEFAULT NULL,
  `columnaId` bigint(20) DEFAULT NULL,
  `tableroId` bigint(20) DEFAULT NULL,
  `usuarioId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK30n0ojdgfxnkm0768tnqddi91` (`columnaId`),
  KEY `FK2r7tsv4xu3bjvst83o8xuspud` (`tableroId`),
  KEY `FKepne2t52y8dmn8l9da0dd7l51` (`usuarioId`),
  CONSTRAINT `FK2r7tsv4xu3bjvst83o8xuspud` FOREIGN KEY (`tableroId`) REFERENCES `Tablero` (`id`),
  CONSTRAINT `FK30n0ojdgfxnkm0768tnqddi91` FOREIGN KEY (`columnaId`) REFERENCES `Columna` (`id`),
  CONSTRAINT `FKepne2t52y8dmn8l9da0dd7l51` FOREIGN KEY (`usuarioId`) REFERENCES `Usuario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
