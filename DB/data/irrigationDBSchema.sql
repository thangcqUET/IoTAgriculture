-- MySQL dump 10.13  Distrib 5.7.32, for Linux (x86_64)
--
-- Host: sv-procon.uet.vnu.edu.vn    Database: irrigation_database
-- ------------------------------------------------------
-- Server version	5.7.32-0ubuntu0.18.04.1

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
-- Table structure for table `Controlling`
--

DROP TABLE IF EXISTS `Controlling`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Controlling` (
  `ControllingID` bigint(20) NOT NULL,
  `DeviceID` bigint(20) DEFAULT NULL,
  `PlotID` int(11) DEFAULT NULL,
  `AmountOfWater` float DEFAULT NULL,
  `WateringDuration` int(11) DEFAULT NULL,
  `TimeOfControl` datetime DEFAULT NULL,
  PRIMARY KEY (`ControllingID`),
  KEY `DeviceID` (`DeviceID`),
  KEY `PlotID` (`PlotID`),
  CONSTRAINT `Controlling_ibfk_1` FOREIGN KEY (`DeviceID`) REFERENCES `Devices` (`DeviceID`) ON UPDATE CASCADE,
  CONSTRAINT `Controlling_ibfk_2` FOREIGN KEY (`PlotID`) REFERENCES `Plots` (`PlotID`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DeviceTypes`
--

DROP TABLE IF EXISTS `DeviceTypes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DeviceTypes` (
  `DeviceTypeID` int(11) NOT NULL AUTO_INCREMENT,
  `DeviceType` varchar(50) NOT NULL,
  PRIMARY KEY (`DeviceTypeID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Devices`
--

DROP TABLE IF EXISTS `Devices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Devices` (
  `DeviceID` bigint(20) NOT NULL,
  `DeviceTypeID` int(11) DEFAULT NULL,
  `DeviceName` varchar(50) DEFAULT NULL,
  `Status` tinyint(1) DEFAULT NULL,
  `PlotID` int(11) DEFAULT NULL,
  PRIMARY KEY (`DeviceID`),
  KEY `DeviceTypeID` (`DeviceTypeID`),
  KEY `PlotID` (`PlotID`),
  CONSTRAINT `Devices_ibfk_1` FOREIGN KEY (`DeviceTypeID`) REFERENCES `DeviceTypes` (`DeviceTypeID`) ON UPDATE CASCADE,
  CONSTRAINT `Devices_ibfk_2` FOREIGN KEY (`PlotID`) REFERENCES `Plots` (`PlotID`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FarmTypes`
--

DROP TABLE IF EXISTS `FarmTypes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `FarmTypes` (
  `FarmTypeID` int(11) NOT NULL AUTO_INCREMENT,
  `FarmType` varchar(50) NOT NULL,
  PRIMARY KEY (`FarmTypeID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Farms`
--

DROP TABLE IF EXISTS `Farms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Farms` (
  `FarmID` int(11) NOT NULL AUTO_INCREMENT,
  `LocateID` varchar(50) DEFAULT NULL,
  `Area` double DEFAULT NULL,
  `FarmTypeID` int(11) DEFAULT NULL,
  `Status` tinyint(1) DEFAULT NULL,
  `UserID` int(11) DEFAULT NULL,
  PRIMARY KEY (`FarmID`),
  KEY `UserID` (`UserID`),
  KEY `FarmTypeID` (`FarmTypeID`),
  KEY `LocateID` (`LocateID`),
  CONSTRAINT `Farms_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `Users` (`UserID`) ON UPDATE CASCADE,
  CONSTRAINT `Farms_ibfk_2` FOREIGN KEY (`FarmTypeID`) REFERENCES `FarmTypes` (`FarmTypeID`) ON UPDATE CASCADE,
  CONSTRAINT `Farms_ibfk_3` FOREIGN KEY (`LocateID`) REFERENCES `Locates` (`LocateID`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Locates`
--

DROP TABLE IF EXISTS `Locates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Locates` (
  `LocateID` varchar(50) NOT NULL,
  `LocateName` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`LocateID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PlotTypes`
--

DROP TABLE IF EXISTS `PlotTypes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PlotTypes` (
  `PlotTypeID` int(11) NOT NULL AUTO_INCREMENT,
  `PlotType` varchar(50) NOT NULL,
  PRIMARY KEY (`PlotTypeID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Plots`
--

DROP TABLE IF EXISTS `Plots`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Plots` (
  `PlotID` int(11) NOT NULL AUTO_INCREMENT,
  `Area` double DEFAULT NULL,
  `PlotTypeID` int(11) DEFAULT NULL,
  `FarmID` int(11) DEFAULT NULL,
  PRIMARY KEY (`PlotID`),
  KEY `FarmID` (`FarmID`),
  KEY `PlotTypeID` (`PlotTypeID`),
  CONSTRAINT `Plots_ibfk_1` FOREIGN KEY (`FarmID`) REFERENCES `Farms` (`FarmID`) ON UPDATE CASCADE,
  CONSTRAINT `Plots_ibfk_2` FOREIGN KEY (`PlotTypeID`) REFERENCES `PlotTypes` (`PlotTypeID`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Sensing`
--

DROP TABLE IF EXISTS `Sensing`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Sensing` (
  `SensingID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DeviceID` bigint(20) DEFAULT NULL,
  `PlotID` int(11) DEFAULT NULL,
  `SoilMoisture` float DEFAULT NULL,
  `Humidity` float DEFAULT NULL,
  `Temperature` float DEFAULT NULL,
  `LightLevel` int(11) DEFAULT NULL,
  `TimeOfMeasurement` datetime DEFAULT NULL,
  PRIMARY KEY (`SensingID`),
  KEY `PlotID` (`PlotID`),
  KEY `DeviceID` (`DeviceID`),
  CONSTRAINT `Sensing_ibfk_1` FOREIGN KEY (`PlotID`) REFERENCES `Plots` (`PlotID`) ON UPDATE CASCADE,
  CONSTRAINT `Sensing_ibfk_2` FOREIGN KEY (`DeviceID`) REFERENCES `Devices` (`DeviceID`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=41410 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Users`
--

DROP TABLE IF EXISTS `Users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Users` (
  `UserID` int(11) NOT NULL AUTO_INCREMENT,
  `UserName` varchar(50) NOT NULL,
  `UPassword` varchar(50) NOT NULL,
  PRIMARY KEY (`UserID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `WeatherForecastAtATimes`
--

DROP TABLE IF EXISTS `WeatherForecastAtATimes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `WeatherForecastAtATimes` (
  `WeatherForecastID` int(11) NOT NULL,
  `ForecastTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `EpochTime` bigint(20) DEFAULT NULL,
  `ForecastStatus` varchar(50) DEFAULT NULL,
  `IsDayLight` tinyint(1) DEFAULT NULL,
  `Temperature` float DEFAULT NULL,
  `WindSpeed` float DEFAULT NULL,
  `RelativeHumidity` float DEFAULT NULL,
  `RainProbability` tinyint(4) DEFAULT NULL,
  `PrecipitationProbability` tinyint(4) DEFAULT NULL,
  `RainValue` float DEFAULT NULL,
  `CloudCover` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`WeatherForecastID`,`ForecastTime`),
  CONSTRAINT `WeatherForecastAtATimes_ibfk_1` FOREIGN KEY (`WeatherForecastID`) REFERENCES `WeatherForecasts` (`WeatherForecastID`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `WeatherForecasts`
--

DROP TABLE IF EXISTS `WeatherForecasts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `WeatherForecasts` (
  `WeatherForecastID` int(11) NOT NULL AUTO_INCREMENT,
  `LocateID` varchar(50) NOT NULL,
  `CurrentTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`WeatherForecastID`),
  KEY `LocateID` (`LocateID`),
  CONSTRAINT `WeatherForecasts_ibfk_1` FOREIGN KEY (`LocateID`) REFERENCES `Locates` (`LocateID`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=763 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping events for database 'irrigation_database'
--

--
-- Dumping routines for database 'irrigation_database'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-01-30 23:33:42
