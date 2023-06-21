/*
SQLyog Ultimate v13.1.1 (64 bit)
MySQL - 8.0.27 : Database - common-fileserver
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`common-fileserver` /*!40100 DEFAULT CHARACTER SET utf8 */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `common-fileserver`;

/*Table structure for table `j_oss` */

DROP TABLE IF EXISTS `j_oss`;

CREATE TABLE `j_oss` (
                         `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
                         `branch_name` varchar(50) NOT NULL COMMENT '机构名称',
                         `code` varchar(50) NOT NULL COMMENT '代号',
                         `out_server_url` varchar(200) DEFAULT '' COMMENT 'sdk上传外网地址',
                         `server_url` varchar(200) NOT NULL COMMENT 'sdk上传内网地址',
                         `pub_url` varchar(200) DEFAULT '' COMMENT '对外提供的HTTP地址',
                         `access_key_id` varchar(200) NOT NULL COMMENT 'accessKeyId',
                         `access_key_secret` varchar(200) NOT NULL COMMENT 'accessKeySecret',
                         `status` int NOT NULL DEFAULT '1' COMMENT '状态',
                         `use_upload_type` tinyint DEFAULT '1' COMMENT '1：使用内部url上传 2：使用外部url上传',
                         `memo` varchar(50) DEFAULT '',
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

/*Data for the table `j_oss` */

insert  into `j_oss`(`id`,`branch_name`,`code`,`out_server_url`,`server_url`,`pub_url`,`access_key_id`,`access_key_secret`,`status`,`use_upload_type`,`memo`) values
    (3,'简易贷P2P','jyd-p2p-file','http://oss-cn-shenzhen.aliyuncs.com','http://oss-cn-shenzhen-internal.aliyuncs.com','http://jyd-p2p-file.oss-cn-shenzhen.aliyuncs.com','LTAIBW9fnKG2YUUv','sK89Yk6c4XACZ80sTyWcdpXFYeIRK0',1,1,'oss-cn-shenzhen-internal.aliyuncs.com');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
