--
-- Table structure for table `Etiqueta`
--

DROP TABLE IF EXISTS `Etiqueta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Etiqueta` (
  `id` bigint(20) NOT NULL,
  `color` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `tableroId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKqhvnrs00mpblhwhp2ondwu2vo` (`tableroId`),
  CONSTRAINT `FKqhvnrs00mpblhwhp2ondwu2vo` FOREIGN KEY (`tableroId`) REFERENCES `Tablero` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Etiqueta_Tarea`
--

DROP TABLE IF EXISTS `Etiqueta_Tarea`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Etiqueta_Tarea` (
  `etiquetas_id` bigint(20) NOT NULL,
  `tareas_id` bigint(20) NOT NULL,
  PRIMARY KEY (`etiquetas_id`,`tareas_id`),
  KEY `FK16crpvejl60miklj55u6gj5ye` (`tareas_id`),
  CONSTRAINT `FK16crpvejl60miklj55u6gj5ye` FOREIGN KEY (`tareas_id`) REFERENCES `Tarea` (`id`),
  CONSTRAINT `FKs2uv1ib7x49tce3qw115vgj74` FOREIGN KEY (`etiquetas_id`) REFERENCES `Etiqueta` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
