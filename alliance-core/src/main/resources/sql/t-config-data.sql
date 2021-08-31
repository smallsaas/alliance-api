
DELETE FROM `t_config_field` WHERE id='101';
DELETE FROM `t_config_field` WHERE id='102';
DELETE FROM `t_config_field` WHERE id='103';
DELETE FROM `t_config_field` WHERE id='104';
DELETE FROM `t_config_field` WHERE id='105';
DELETE FROM `t_config_field` WHERE id='106';
DELETE FROM `t_config_field` WHERE id='107';
DELETE FROM `t_config_field` WHERE id='201';
DELETE FROM `t_config_field` WHERE id='202';
DELETE FROM `t_config_field` WHERE id='203';
DELETE FROM `t_config_field` WHERE id='204';

INSERT INTO `t_config_field` VALUES ('101', 'common_alliance', '1', 'zh', '普通盟友入货金额', '2000', 'FLOAT', '￥', 0);
INSERT INTO `t_config_field` VALUES ('102', 'bonus_alliance', '1', 'zh', '分红盟友入货金额', '10000', 'FLOAT', '￥', 0);
INSERT INTO `t_config_field` VALUES ('103', 'temp_alliance_expiry_time', '1', 'zh', '临时盟友过期天数', '1', 'INTEGER', '单位 (天)', 0);
INSERT INTO `t_config_field` VALUES ('104', 'starting_time', '1', 'zh', '分红起算时间', '2019-08-13', 'STRING', '录入格式 [YYYY-MM-DD]', 0);
INSERT INTO `t_config_field` VALUES ('105', 'settlement_cycle', '1', 'zh', '分红周期', '15', 'INTEGER', '（月）', 0);
INSERT INTO `t_config_field` VALUES ('106', 'fixed_proportion', '1', 'zh', '固定分红', '50', 'FLOAT', '固定百分比(%)', 0);
INSERT INTO `t_config_field` VALUES ('107', 'ratio_proportion', '1', 'zh', '权重分红', '50', 'FLOAT', '权重(库存额)百分比(%)', 0);
INSERT INTO `t_config_field` VALUES ('201', 'hobby', '2', 'zh', '爱好', '', 'STRING', null, 0);
INSERT INTO `t_config_field` VALUES ('202', 'withdrawal_conditions', '1', 'zh', '每月至低可提现进货额', '2000', 'FLOAT', '￥', 0);
INSERT INTO `t_config_field` VALUES ('203', 'experience_time', '1', 'zh', '盟友体验周期', '0', 'INTEGER', '在体验期内，盟友可退库存余额。单位（月)', 0);
INSERT INTO `t_config_field` VALUES ('204', 'global_delay_register', '1', 'zh', '延时注册', 'true', 'STRING', '延时注册', 0);
