-- phpMyAdmin SQL Dump
-- version 3.5.2.2
-- http://www.phpmyadmin.net
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 02-07-2013 a las 04:27:34
-- Versión del servidor: 5.5.27
-- Versión de PHP: 5.4.7

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de datos: `architect`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `architecture`
--

CREATE TABLE IF NOT EXISTS `architecture` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `id_group` bigint(20) unsigned NOT NULL,
  `blocked` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `block_reason` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id_group` (`id_group`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=5 ;

--
-- Volcado de datos para la tabla `architecture`
--

INSERT INTO `architecture` (`id`, `name`, `id_group`, `blocked`, `block_reason`) VALUES
(4, 'CTAS', 1, 0, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `architecture_analysis`
--

CREATE TABLE IF NOT EXISTS `architecture_analysis` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `id_architecture` bigint(20) unsigned NOT NULL,
  `id_scenario` bigint(20) unsigned NOT NULL,
  `id_design_bot` bigint(20) unsigned NOT NULL,
  `enable` tinyint(1) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=9 ;

--
-- Volcado de datos para la tabla `architecture_analysis`
--

INSERT INTO `architecture_analysis` (`id`, `id_architecture`, `id_scenario`, `id_design_bot`, `enable`) VALUES
(5, 4, 4, 5, 1),
(6, 4, 5, 5, 1),
(7, 4, 4, 7, 1),
(8, 4, 6, 8, 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `dependency`
--

CREATE TABLE IF NOT EXISTS `dependency` (
  `parent` bigint(20) unsigned NOT NULL,
  `child` bigint(20) unsigned NOT NULL,
  `couplingcost` double(20,2) NOT NULL,
  `id_architecture` bigint(20) unsigned NOT NULL,
  KEY `parent` (`parent`),
  KEY `parent_2` (`parent`),
  KEY `parent_3` (`parent`),
  KEY `parent_4` (`parent`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `dependency`
--

INSERT INTO `dependency` (`parent`, `child`, `couplingcost`, `id_architecture`) VALUES
(14, 9, 0.50, 4),
(14, 16, 0.50, 4),
(11, 15, 0.50, 4),
(11, 13, 0.50, 4),
(16, 13, 0.50, 4),
(16, 15, 0.50, 4),
(13, 10, 0.50, 4),
(13, 9, 0.50, 4),
(10, 20, 0.50, 4),
(18, 19, 0.50, 4),
(17, 13, 0.50, 4),
(14, 13, 0.50, 4),
(13, 15, 0.50, 4),
(14, 11, 0.50, 4);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `design_bot`
--

CREATE TABLE IF NOT EXISTS `design_bot` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `id_quality_attribute` bigint(20) unsigned NOT NULL,
  `id_architecture` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKFE435FC65F068A41` (`id_quality_attribute`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=44 ;

--
-- Volcado de datos para la tabla `design_bot`
--

INSERT INTO `design_bot` (`id`, `name`, `id_quality_attribute`, `id_architecture`) VALUES
(5, 'DB1 - ALL', 1, 4),
(6, 'DB2 - Split', 1, 4),
(7, 'DB3 - Intermediary', 1, 4),
(8, 'DB 4 - Abstract', 1, 4);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `element_change`
--

CREATE TABLE IF NOT EXISTS `element_change` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `id_architecture` bigint(20) unsigned NOT NULL,
  `element_type` varchar(255) NOT NULL,
  `change_number` bigint(20) unsigned NOT NULL,
  `change_type` varchar(255) NOT NULL,
  `old_element` longblob,
  `new_element` longblob,
  `consistent` tinyint(1) NOT NULL,
  `undo_change` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=34 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `group_user`
--

CREATE TABLE IF NOT EXISTS `group_user` (
  `id_user` bigint(20) unsigned NOT NULL,
  `id_group` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id_user`,`id_group`),
  KEY `id_user` (`id_user`),
  KEY `id_group` (`id_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `group_user`
--

INSERT INTO `group_user` (`id_user`, `id_group`) VALUES
(1, 1),
(1, 3),
(2, 1),
(2, 4),
(3, 2),
(3, 5),
(4, 2),
(4, 6),
(5, 1),
(5, 7);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `grupo`
--

CREATE TABLE IF NOT EXISTS `grupo` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `groupname` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=8 ;

--
-- Volcado de datos para la tabla `grupo`
--

INSERT INTO `grupo` (`id`, `groupname`) VALUES
(1, 'grupo1'),
(2, 'grupo2'),
(3, 'pablo'),
(4, 'luis'),
(5, 'gonza'),
(6, 'diego'),
(7, 'andres');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `module`
--

CREATE TABLE IF NOT EXISTS `module` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `id_architecture` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=12 ;

--
-- Volcado de datos para la tabla `module`
--

INSERT INTO `module` (`id`, `name`, `description`, `id_architecture`) VALUES
(7, 'Model', 'Model module for MVC', 4),
(8, 'View', 'View module for MVC', 4),
(9, 'Controller', 'Controller module for MVC', 4);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `quality_attribute`
--

CREATE TABLE IF NOT EXISTS `quality_attribute` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

--
-- Volcado de datos para la tabla `quality_attribute`
--

INSERT INTO `quality_attribute` (`id`, `name`) VALUES
(1, 'Modifiability'),
(2, 'Performance');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `responsibility`
--

CREATE TABLE IF NOT EXISTS `responsibility` (
  `id` bigint(20) unsigned NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `complexity` double(20,2) NOT NULL,
  `executionTime` bigint(20) unsigned NOT NULL,
  `id_architecture` bigint(20) DEFAULT NULL,
  `id_module` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1A04CE2695F7D32C` (`id_architecture`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `responsibility`
--

INSERT INTO `responsibility` (`id`, `name`, `description`, `complexity`, `executionTime`, `id_architecture`, `id_module`) VALUES
(9, 'R1', 'Show itinerary', 0.50, 1, 4, 8),
(10, 'R2', 'Query for data', 0.50, 1, 4, 7),
(11, 'R3', 'Create user profile', 0.50, 1, 4, 7),
(13, 'R4', 'Manage itinerary', 0.50, 1, 4, 7),
(14, 'R5', 'Handle user interactions', 0.50, 1, 4, 9),
(15, 'R6', 'Save data', 0.50, 1, 4, 7),
(16, 'R7', 'Modify user profile', 0.50, 1, 4, 7),
(17, 'R8', 'Manage external devices', 0.50, 1, 4, 9),
(18, 'R9', 'Attach to model', 0.50, 1, 4, 8),
(19, 'R10', 'Register views', 0.50, 1, 4, 7),
(20, 'R11', 'Locate service', 0.50, 1, 4, 7);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `responsibility_scenario`
--

CREATE TABLE IF NOT EXISTS `responsibility_scenario` (
  `id_responsibility` bigint(20) NOT NULL DEFAULT '0',
  `id_scenario` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_responsibility`,`id_scenario`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `responsibility_scenario`
--

INSERT INTO `responsibility_scenario` (`id_responsibility`, `id_scenario`) VALUES
(9, 4),
(10, 4),
(11, 5),
(11, 9),
(11, 11),
(13, 7),
(13, 10),
(14, 9),
(14, 10),
(15, 4),
(15, 11),
(16, 5),
(16, 9),
(17, 6),
(17, 7),
(18, 8),
(19, 8);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `scenario`
--

CREATE TABLE IF NOT EXISTS `scenario` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `stimulus` varchar(255) DEFAULT NULL,
  `source` varchar(255) DEFAULT NULL,
  `enviroment` varchar(255) DEFAULT NULL,
  `artifact` varchar(255) DEFAULT NULL,
  `response` int(2) NOT NULL,
  `measure` bigint(20) unsigned DEFAULT NULL,
  `id_architecture` bigint(20) DEFAULT NULL,
  `id_quality_attribute` bigint(20) NOT NULL,
  `priority` bigint(20) unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `FKD1C5739077A48413` (`id_quality_attribute`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=227 ;

--
-- Volcado de datos para la tabla `scenario`
--

INSERT INTO `scenario` (`id`, `name`, `description`, `stimulus`, `source`, `enviroment`, `artifact`, `response`, `measure`, `id_architecture`, `id_quality_attribute`, `priority`) VALUES
(4, 'M1', 'The addition of new features requires changes in the data format. The implementation of the new format has to be \ndone within 3.5 person days of effort.', '', '', '', '', 1, 28, 4, 1, 1),
(5, 'M2', 'A new variable to the user profile has to be added within 15 person days of effort.', '', '', '', '', 1, 120, 4, 1, 2),
(6, 'M3', 'The driver for a new external device has to be added by a developer within 5.5 person days of effort.', '', '', '', '', 1, 44, 4, 1, 10),
(7, 'P1', 'The system has to manage the external devices (under normal load) and handle the operations in less than 15 milliseconds,', '', '', '', '', 0, 15, 4, 2, 1),
(8, 'P2', 'A view wishes to attach to the model under normal conditions and do so in less than 9 milliseconds.', '', '', '', '', 0, 9, 4, 2, 1),
(9, 'P3', 'The user asks to show the itinerary under normal conditions and the itinerary is shown in less than 16 milliseconds.', '', '', '', '', 0, 16, 4, 2, 1),
(10, 'P4', 'The user asks to show the itinerary under normal conditions and the itinerary is shown in less than 16 milliseconds.', '', '', '', '', 0, 16, 4, 2, 1),
(11, 'P5', 'The user asks to save the current data on the screen under normal conditions and the data is saved in under 4 milliseconds.', '', '', '', '', 0, 8, 4, 2, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tactic`
--

CREATE TABLE IF NOT EXISTS `tactic` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `id_quality_attribute` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKA38256291556DF6F` (`id_quality_attribute`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=6 ;

--
-- Volcado de datos para la tabla `tactic`
--

INSERT INTO `tactic` (`id`, `name`, `id_quality_attribute`) VALUES
(1, 'Insert Intermediary Responsibility', 1),
(2, 'Split Responsibility', 1),
(3, 'Abstract Common Responsibilities', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tactic_designbot`
--

CREATE TABLE IF NOT EXISTS `tactic_designbot` (
  `id_tactic` bigint(20) unsigned NOT NULL,
  `id_design_bot` bigint(20) unsigned NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `tactic_designbot`
--

INSERT INTO `tactic_designbot` (`id_tactic`, `id_design_bot`) VALUES
(3, 5),
(1, 5),
(2, 5),
(2, 6),
(1, 7),
(3, 8);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  `account_expired` tinyint(1) NOT NULL,
  `credentials_expired` tinyint(1) NOT NULL,
  `account_locked` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=6 ;

--
-- Volcado de datos para la tabla `user`
--

INSERT INTO `user` (`id`, `username`, `password`, `enabled`, `account_expired`, `credentials_expired`, `account_locked`) VALUES
(1, 'pablo', 'pablo', 1, 0, 0, 0),
(2, 'luis', 'luis', 1, 0, 0, 0),
(3, 'gonza', 'gonza', 1, 0, 0, 0),
(4, 'diego', 'diego', 1, 0, 0, 0),
(5, 'andres', 'andres', 1, 0, 0, 0);

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `architecture`
--
ALTER TABLE `architecture`
  ADD CONSTRAINT `architecture_ibfk_1` FOREIGN KEY (`id_group`) REFERENCES `grupo` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `group_user`
--
ALTER TABLE `group_user`
  ADD CONSTRAINT `group_user_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `group_user_ibfk_2` FOREIGN KEY (`id_group`) REFERENCES `grupo` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
