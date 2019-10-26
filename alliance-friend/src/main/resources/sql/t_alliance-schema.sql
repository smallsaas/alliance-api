DROP TABLE IF EXISTS `t_moments_friend`;
CREATE TABLE `t_moments_friend` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `alliance_id` bigint(20) DEFAULT NULL COMMENT '盟友id',
  `contact_phone` varchar(26) NOT NULL COMMENT '电话号码',
  `name` varchar(26) NOT NULL COMMENT '名字',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `alliance_user_id` bigint(20) DEFAULT NULL COMMENT 'webapp user_id',
  `pcd_province` varchar(26) DEFAULT NULL COMMENT '省份',
  `pcd_city` varchar(26) DEFAULT NULL COMMENT '城市',
  `pcd_distinct` varchar(26) DEFAULT NULL COMMENT 'distinct',
  `post_code` varchar(26) DEFAULT NULL COMMENT '邮编',
  `address` varchar(80) DEFAULT NULL COMMENT '地址',
  `avator` varchar(255) DEFAULT NULL COMMENT '头像',
  `nick` varchar(26) DEFAULT NULL COMMENT '昵称',
  `remark` varchar(30) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;