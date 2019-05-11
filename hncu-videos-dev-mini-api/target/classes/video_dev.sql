/*
SQLyog Enterprise v12.08 (64 bit)
MySQL - 5.5.27 : Database - video_dev
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`video_dev` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `video_dev`;

/*Table structure for table `bgm` */

DROP TABLE IF EXISTS `bgm`;

CREATE TABLE `bgm` (
  `id` varchar(64) NOT NULL,
  `author` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `path` varchar(255) NOT NULL COMMENT '播放地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `bgm` */

insert  into `bgm`(`id`,`author`,`name`,`path`) values ('a7f943b436344a7cb6b7ab79a2e81a8c','花粥','出山','\\bgm\\aa.mp3'),('ab29e42fe2684a5f9f92108fa6b2a056','但你走了','当你走了','\\bgm\\当你走了.mp3'),('ac4234a1de0c4994b259e83f9ee9049e','花粥','出山','\\bgm\\aa.mp3'),('d10121f3708f4b52b9aa72817f01812a','111','222','\\bgm\\dongxi.mp3'),('d10121f3708f4b52b9aa72817f01812e','林俊佑','东西','\\bgm\\东西.mp3'),('e7ade2b53f724b0f9f83d6b2740e029e','学友','烦恼歌','\\bgm\\烦恼歌.mp3');

/*Table structure for table `comments` */

DROP TABLE IF EXISTS `comments`;

CREATE TABLE `comments` (
  `id` varchar(20) NOT NULL,
  `father_comment_id` varchar(20) DEFAULT NULL,
  `to_user_id` varchar(20) DEFAULT NULL,
  `video_id` varchar(20) NOT NULL COMMENT '视频id',
  `from_user_id` varchar(20) NOT NULL COMMENT '留言者，评论的用户id',
  `comment` text NOT NULL COMMENT '评论内容',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程评论表';

/*Data for the table `comments` */

/*Table structure for table `search_records` */

DROP TABLE IF EXISTS `search_records`;

CREATE TABLE `search_records` (
  `id` varchar(64) NOT NULL,
  `content` varchar(255) NOT NULL COMMENT '搜索的内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='视频搜索的记录表';

/*Data for the table `search_records` */

/*Table structure for table `users` */

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `id` varchar(64) NOT NULL,
  `username` varchar(20) NOT NULL COMMENT '用户名',
  `password` varchar(64) NOT NULL COMMENT '密码',
  `face_image` varchar(255) DEFAULT NULL COMMENT '我的头像，如果没有默认给一张',
  `nickname` varchar(20) NOT NULL COMMENT '昵称',
  `fans_counts` int(11) DEFAULT '0' COMMENT '我的粉丝数量',
  `follow_counts` int(11) DEFAULT '0' COMMENT '我关注的人总数',
  `receive_like_counts` int(11) DEFAULT '0' COMMENT '我接受到的赞美/收藏 的数量',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `users` */

insert  into `users`(`id`,`username`,`password`,`face_image`,`nickname`,`fans_counts`,`follow_counts`,`receive_like_counts`) values ('f11666c226f2478dbfed82f604a2982f','abc123','4QrcOUm6Wau+VuBX8g+IPg==','/f11666c226f2478dbfed82f604a2982f/face/wx98a96b869e7ffc11.o6zAJs7KM8v9unCa3OKkMawHKJzM.x3x8I6u9KHn5c20ae4b76655a3b845ee1d87a9080dab.jpg','abc123',0,0,0);

/*Table structure for table `users_fans` */

DROP TABLE IF EXISTS `users_fans`;

CREATE TABLE `users_fans` (
  `id` varchar(64) NOT NULL,
  `user_id` varchar(64) NOT NULL COMMENT '用户',
  `fan_id` varchar(64) NOT NULL COMMENT '粉丝',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`,`fan_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户粉丝关联关系表';

/*Data for the table `users_fans` */

/*Table structure for table `users_like_videos` */

DROP TABLE IF EXISTS `users_like_videos`;

CREATE TABLE `users_like_videos` (
  `id` varchar(64) NOT NULL,
  `user_id` varchar(64) NOT NULL COMMENT '用户',
  `video_id` varchar(64) NOT NULL COMMENT '视频',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_video_rel` (`user_id`,`video_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户喜欢的/赞过的视频';

/*Data for the table `users_like_videos` */

/*Table structure for table `users_report` */

DROP TABLE IF EXISTS `users_report`;

CREATE TABLE `users_report` (
  `id` varchar(64) NOT NULL,
  `deal_user_id` varchar(64) NOT NULL COMMENT '被举报用户id',
  `deal_video_id` varchar(64) NOT NULL,
  `title` varchar(128) NOT NULL COMMENT '类型标题，让用户选择，详情见 枚举',
  `content` varchar(255) DEFAULT NULL COMMENT '内容',
  `userid` varchar(64) NOT NULL COMMENT '举报人的id',
  `create_date` datetime NOT NULL COMMENT '举报时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='举报用户表';

/*Data for the table `users_report` */

/*Table structure for table `videos` */

DROP TABLE IF EXISTS `videos`;

CREATE TABLE `videos` (
  `id` varchar(64) NOT NULL,
  `user_id` varchar(64) NOT NULL COMMENT '发布者id',
  `audio_id` varchar(64) DEFAULT NULL COMMENT '用户使用音频的信息',
  `video_desc` varchar(128) DEFAULT NULL COMMENT '视频描述',
  `video_path` varchar(255) NOT NULL COMMENT '视频存放的路径',
  `video_seconds` float(6,2) DEFAULT NULL COMMENT '视频秒数',
  `video_width` int(6) DEFAULT NULL COMMENT '视频宽度',
  `video_height` int(6) DEFAULT NULL COMMENT '视频高度',
  `cover_path` varchar(255) DEFAULT NULL COMMENT '视频封面图',
  `like_counts` bigint(20) NOT NULL DEFAULT '0' COMMENT '喜欢/赞美的数量',
  `status` int(1) NOT NULL COMMENT '视频状态：\r\n1、发布成功\r\n2、禁止播放，管理员操作',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='视频信息表';

/*Data for the table `videos` */

insert  into `videos`(`id`,`user_id`,`audio_id`,`video_desc`,`video_path`,`video_seconds`,`video_width`,`video_height`,`cover_path`,`like_counts`,`status`,`create_time`) values ('0348f2ca86d242a0812cb1dd9f4486a6','f11666c226f2478dbfed82f604a2982f','','小祖宗','/f11666c226f2478dbfed82f604a2982f/video/wx98a96b869e7ffc11.o6zAJs7KM8v9unCa3OKkMawHKJzM.IfVat0DF4sIU7cbec915033b88ce6c31fa3e503dc9f0.mp4',22.43,720,1280,'/f11666c226f2478dbfed82f604a2982f/video/wx98a96b869e7ffc11.jpg',0,1,'2019-04-14 14:18:58');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
