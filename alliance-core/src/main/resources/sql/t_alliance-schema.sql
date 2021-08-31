DROP TABLE IF EXISTS `t_alliance`;
CREATE TABLE `t_alliance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `invitor_alliance_id` int(11) DEFAULT NULL COMMENT '邀请人id',
  `alliance_ship` int(11) DEFAULT NULL,
  `stockholder_ship` int(11) DEFAULT NULL,
  `creation_time` datetime DEFAULT NULL COMMENT 'creation_time',
  `alliance_ship_time` datetime DEFAULT NULL COMMENT '成为盟友的时间',
  `stockholder_ship_time` datetime DEFAULT NULL COMMENT '成为股东的时间',
  `temp_alliance_expiry_time` datetime DEFAULT NULL COMMENT '临时盟友过期时间',
  `alliance_status` int(11) DEFAULT NULL,
  `alliance_inventory_amount` decimal(10,4) DEFAULT NULL,
  `alliance_point` decimal(10,4) DEFAULT NULL,
  `alliance_rank` int(11) DEFAULT NULL,
  `alliance_name` varchar(30) DEFAULT NULL,
  `alliance_sex` int(1) DEFAULT NULL COMMENT '性别',
  `alliance_occupation` varchar(50) DEFAULT NULL COMMENT '职业',
  `alliance_industry` varchar(30) DEFAULT NULL COMMENT '行业',
  `alliance_address` varchar(100) DEFAULT NULL COMMENT '居住地',
  `alliance_business` varchar(30) DEFAULT NULL COMMENT '业务方向',
  `alliance_hobby` varchar(50) DEFAULT NULL COMMENT '兴趣爱好',
  `alliance_phone` varchar(30) DEFAULT NULL COMMENT '电话',
  `alliance_dob` datetime DEFAULT NULL COMMENT '生日',
  `starting_cycle` datetime DEFAULT NULL COMMENT '起算周期',
  `balance` decimal(10,4) DEFAULT NULL COMMENT '去年结余',
  `alliance_type` int(1) DEFAULT NULL COMMENT '1分红盟友，2平台盟友',
  `age` int(10) DEFAULT NULL,
  `avator` varchar(255) DEFAULT NULL,
  `role` varchar(30) DEFAULT NULL COMMENT '角色',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_offline_withdrawal`;
CREATE TABLE `t_offline_withdrawal` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `balance` decimal(10,2) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `status` int(11) DEFAULT '0',
  `user_id` int(11) DEFAULT NULL,
  `note` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO `t_config_field` VALUES ('204', 'global_delay_register', '1', 'zh', '延时注册', 'true', 'STRING', '延时注册');