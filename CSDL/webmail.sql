-- MySQL dump 10.11
--
-- Host: localhost    Database: webmail
-- ------------------------------------------------------
-- Server version	5.0.45-log

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
-- Table structure for table `big_data_info`
--

DROP TABLE IF EXISTS `big_data_info`;
CREATE TABLE `big_data_info` (
  `id` bigint(20) NOT NULL auto_increment,
  `big_content` longblob,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=96440 DEFAULT CHARSET=utf8;

--
-- Table structure for table `big_meta_info`
--

DROP TABLE IF EXISTS `big_meta_info`;
CREATE TABLE `big_meta_info` (
  `id` bigint(20) NOT NULL auto_increment,
  `big_meta_format` varchar(255) default NULL,
  `big_meta_application` varchar(255) default NULL,
  `big_meta_ref_indexer` varchar(64) default NULL,
  `big_meta_update_time` datetime default NULL,
  `big_meta_parent_uuid` varchar(64) default NULL,
  `big_meta_indexer` int(11) default NULL,
  `big_meta_uuid` varchar(64) NOT NULL,
  `big_meta_virus_check` int(11) default NULL,
  `big_meta_virus_message` varchar(256) default NULL,
  `big_meta_content` bigint(20) default NULL,
  `big_meta_cache_time` datetime default NULL,
  PRIMARY KEY  (`id`),
  KEY `FKA386609BE74600C` (`big_meta_content`),
  CONSTRAINT `FKA386609BE74600C` FOREIGN KEY (`big_meta_content`) REFERENCES `big_data_info` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=96409 DEFAULT CHARSET=utf8;

--
-- Table structure for table `follow_up`
--

DROP TABLE IF EXISTS `follow_up`;
CREATE TABLE `follow_up` (
  `id` bigint(20) NOT NULL auto_increment,
  `mail_follow_up_name` varchar(255) default NULL,
  `mail_follow_up_date` datetime default NULL,
  `mail_follow_up_owner` varchar(64) default NULL,
  `mail_follow_up_mail_header` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FKD3BF55E97426A8B4` (`mail_follow_up_mail_header`),
  CONSTRAINT `FKD3BF55E97426A8B4` FOREIGN KEY (`mail_follow_up_mail_header`) REFERENCES `mail_header` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;

--
-- Table structure for table `mail_account_configure_info`
--

DROP TABLE IF EXISTS `mail_account_configure_info`;
CREATE TABLE `mail_account_configure_info` (
  `id` bigint(20) NOT NULL auto_increment,
  `mail_configure_owner` varchar(42) NOT NULL,
  `mail_configure_receiver` mediumblob,
  `mail_configure_smtp_default` varchar(128) NOT NULL,
  `mail_configure_acc_fullname` varchar(64) default NULL,
  `mail_configure_refresh` int(11) default NULL,
  `mail_configure_digital_alg` varchar(255) default NULL,
  `mail_configure_multipart` char(1) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1551 DEFAULT CHARSET=utf8;

--
-- Table structure for table `mail_address_book_info`
--

DROP TABLE IF EXISTS `mail_address_book_info`;
CREATE TABLE `mail_address_book_info` (
  `id` bigint(20) NOT NULL auto_increment,
  `owner` bigint(20) default NULL,
  `displayName` varchar(32) default NULL,
  `groupName` varchar(64) default NULL,
  `fullName` varchar(32) default NULL,
  `emailAddress` varchar(128) default NULL,
  `city` varchar(48) default NULL,
  `department` varchar(128) default NULL,
  `district` varchar(48) default NULL,
  `faxNumber` varchar(16) default NULL,
  `firstName` varchar(32) default NULL,
  `homeNumber` varchar(16) default NULL,
  `lastName` varchar(32) default NULL,
  `mobileNumber` varchar(16) default NULL,
  `pagerNumber` varchar(16) default NULL,
  `workNumber` varchar(16) default NULL,
  `postalAddress` varchar(256) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `mail_error`
--

DROP TABLE IF EXISTS `mail_error`;
CREATE TABLE `mail_error` (
  `id` bigint(20) NOT NULL auto_increment,
  `owner` varchar(255) default NULL,
  `uid` varchar(255) default NULL,
  `account` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

--
-- Table structure for table `mail_filter`
--

DROP TABLE IF EXISTS `mail_filter`;
CREATE TABLE `mail_filter` (
  `id` bigint(20) NOT NULL auto_increment,
  `mail_filter_name` varchar(255) NOT NULL,
  `mail_filter_clause` longblob NOT NULL,
  `mail_folder_filter` bigint(20) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FKAD833007DA96A3F` (`mail_folder_filter`),
  CONSTRAINT `FKAD833007DA96A3F` FOREIGN KEY (`mail_folder_filter`) REFERENCES `mail_folder` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8;

--
-- Table structure for table `mail_folder`
--

DROP TABLE IF EXISTS `mail_folder`;
CREATE TABLE `mail_folder` (
  `id` bigint(20) NOT NULL auto_increment,
  `mail_folder_name` varchar(255) NOT NULL,
  `mail_folder_type` varchar(255) default NULL,
  `mail_folder_owner` varchar(42) NOT NULL,
  `mail_folder_parent_id` bigint(20) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9377 DEFAULT CHARSET=utf8;

--
-- Table structure for table `mail_folderuid_content`
--

DROP TABLE IF EXISTS `mail_folderuid_content`;
CREATE TABLE `mail_folderuid_content` (
  `id` bigint(20) NOT NULL auto_increment,
  `uid` varchar(128) default NULL,
  `username` varchar(64) default NULL,
  `account` varchar(128) default NULL,
  `mailhost` varchar(128) default NULL,
  `headerID` bigint(20) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `mail_header`
--

DROP TABLE IF EXISTS `mail_header`;
CREATE TABLE `mail_header` (
  `id` bigint(20) NOT NULL auto_increment,
  `mail_header_priority` varchar(255) NOT NULL,
  `mail_header_type` varchar(255) NOT NULL,
  `mail_header_size` int(11) default NULL,
  `mail_header_owner` varchar(42) NOT NULL,
  `mail_header_flag` varchar(255) NOT NULL,
  `mail_header_subject` varchar(998) default NULL,
  `mail_header_uid` varchar(48) default NULL,
  `mail_header_sender` varchar(256) default NULL,
  `mail_header_read` char(1) default NULL,
  `mail_header_created` datetime NOT NULL,
  `mail_header_received` datetime default NULL,
  `mail_header_sent` datetime default NULL,
  `mail_header_content` varchar(64) default NULL,
  `mail_header_account` varchar(128) default NULL,
  `mail_header_attached` char(1) NOT NULL,
  `mail_header_folder` bigint(20) NOT NULL,
  `mail_header_cache` datetime default NULL,
  `mail_header_spam` char(1) default NULL,
  `mail_header_recipients` varchar(256) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FKE044A15A83FA436` (`mail_header_folder`),
  CONSTRAINT `FKE044A15A83FA436` FOREIGN KEY (`mail_header_folder`) REFERENCES `mail_folder` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=94827 DEFAULT CHARSET=utf8;

--
-- Table structure for table `mail_leavserver_info`
--

DROP TABLE IF EXISTS `mail_leavserver_info`;
CREATE TABLE `mail_leavserver_info` (
  `id` bigint(20) NOT NULL auto_increment,
  `uid` varchar(128) default NULL,
  `username` varchar(64) default NULL,
  `account` varchar(128) default NULL,
  `mailhost` varchar(128) default NULL,
  `receiverDate` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `mail_signature`
--

DROP TABLE IF EXISTS `mail_signature`;
CREATE TABLE `mail_signature` (
  `id` bigint(20) NOT NULL auto_increment,
  `mail_signature_name` varchar(64) NOT NULL,
  `mail_signature_owner` varchar(42) NOT NULL,
  `mail_signature_data` varchar(1024) NOT NULL,
  `mail_signature_default` char(1) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;

--
-- Table structure for table `mail_template_detail_info`
--

DROP TABLE IF EXISTS `mail_template_detail_info`;
CREATE TABLE `mail_template_detail_info` (
  `id` bigint(20) NOT NULL auto_increment,
  `mail_template_name` varchar(128) NOT NULL,
  `mail_template_type` varchar(255) NOT NULL,
  `mail_template_owner` varchar(64) default NULL,
  `mail_template_format` varchar(255) NOT NULL,
  `mail_template_status` varchar(255) NOT NULL,
  `mail_template_content` varchar(5120) default NULL,
  `mail_template_create` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `news_category_info`
--

DROP TABLE IF EXISTS `news_category_info`;
CREATE TABLE `news_category_info` (
  `id` bigint(20) NOT NULL auto_increment,
  `name` varchar(256) default NULL,
  `priority` int(11) NOT NULL,
  `permission` char(1) NOT NULL,
  `description` varchar(4096) default NULL,
  `status` varchar(2) default NULL,
  `topic` int(11) NOT NULL,
  `author` varchar(64) default NULL,
  `numOfMessage` bigint(20) NOT NULL,
  `lastCreated` datetime default NULL,
  `mentor` varchar(64) default NULL,
  `dmzAccess` char(1) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `news_forum_topic`
--

DROP TABLE IF EXISTS `news_forum_topic`;
CREATE TABLE `news_forum_topic` (
  `id` bigint(20) NOT NULL auto_increment,
  `priority` int(11) NOT NULL,
  `status` varchar(2) default NULL,
  `topic` varchar(256) default NULL,
  `topic_mark` int(11) default NULL,
  `author` varchar(64) default NULL,
  `createdDate` datetime default NULL,
  `lastPost` datetime default NULL,
  `replied` int(11) NOT NULL,
  `viewed` int(11) NOT NULL,
  `brief` varchar(256) default NULL,
  `topic_message_uuid` varchar(255) default NULL,
  `forum_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FK895F84E5CE1F61EF` (`forum_id`),
  CONSTRAINT `FK895F84E5CE1F61EF` FOREIGN KEY (`forum_id`) REFERENCES `news_category_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `news_topic_message`
--

DROP TABLE IF EXISTS `news_topic_message`;
CREATE TABLE `news_topic_message` (
  `id` bigint(20) NOT NULL auto_increment,
  `message_size` int(11) default NULL,
  `title` varchar(256) default NULL,
  `message_mark` int(11) default NULL,
  `author` varchar(64) default NULL,
  `posted` datetime default NULL,
  `message_uuid` varchar(48) default NULL,
  `forum_topic_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FKCFA7F04B7455E6BE` (`forum_topic_id`),
  CONSTRAINT `FKCFA7F04B7455E6BE` FOREIGN KEY (`forum_topic_id`) REFERENCES `news_forum_topic` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `recipient_sender`
--

DROP TABLE IF EXISTS `recipient_sender`;
CREATE TABLE `recipient_sender` (
  `id` bigint(20) NOT NULL auto_increment,
  `owner` varchar(64) default NULL,
  `email` varchar(255) default NULL,
  `recipient` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=999 DEFAULT CHARSET=utf8;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-12-18  4:47:57
