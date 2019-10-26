DROP TABLE IF EXISTS `t_alliance`;
CREATE TABLE `t_alliance` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户id',
  `invitor_alliance_id` int(11) DEFAULT NULL COMMENT '邀请人id',
  `alliance_ship` int(11) NOT NULL DEFAULT '0' COMMENT '是否为盟友 0:否，1:临时，2:正式',
  `stockholder_ship` int(11) NOT NULL DEFAULT '0' COMMENT '是否为股东 0:否，1:是',
  `creation_time` datetime DEFAULT NULL COMMENT 'creation_time',
  `alliance_ship_time` datetime DEFAULT NULL COMMENT '成为盟友的时间',
  `stockholder_ship_time` datetime DEFAULT NULL COMMENT '成为股东的时间',
  `temp_alliance_expiry_time` datetime DEFAULT NULL COMMENT '临时盟友过期时间',
  `alliance_status` int(11) NOT NULL DEFAULT '0' COMMENT '盟友状态，0:禁止，1:正常',
  `alliance_inventory_amount` decimal(10,4) NOT NULL DEFAULT '0.0000' COMMENT '库存金额',
  `alliance_point` decimal(10,4) NOT NULL DEFAULT '0.0000' COMMENT '积分',
  `alliance_name` varchar(10) NOT NULL COMMENT '盟友姓名',
  `alliance_sex` int(1) DEFAULT NULL COMMENT '性别',
  `alliance_occupation` varchar(10) DEFAULT NULL COMMENT '职业',
  `alliance_industry` varchar(10) DEFAULT NULL COMMENT '行业',
  `alliance_address` varchar(50) DEFAULT NULL COMMENT '居住地',
  `alliance_business` varchar(10) DEFAULT NULL COMMENT '业务方向',
  `alliance_hobby` varchar(50) DEFAULT NULL COMMENT '兴趣爱好',
  `alliance_phone` varchar(11) DEFAULT NULL COMMENT '电话',
  `alliance_dob` datetime DEFAULT NULL COMMENT '生日',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
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