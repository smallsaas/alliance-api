

INSERT INTO sys_perm_group(`id`, `org_id`, `pid`, `identifier`, `name`)
VALUES ('100000000000000005', '100000000000000001', '100000000000000001', 'Alliance.management', '盟友模块');
INSERT INTO sys_perm(`id`, `group_id`, `identifier`, `name`,`tag`) VALUES('100000000000005001',  '100000000000000005', 'Alliance.edit',  '编辑盟友', '0');
INSERT INTO sys_perm(`id`, `group_id`, `identifier`, `name`,`tag`) VALUES('100000000000005002' , '100000000000000005', 'Alliance.delete','删除盟友', '0');
INSERT INTO sys_perm(`id`, `group_id`, `identifier`, `name`,`tag`) VALUES('100000000000005003' , '100000000000000005', 'Alliance.view',  '查看盟友', '0');
INSERT INTO sys_perm(`id`, `group_id`, `identifier`, `name`,`tag`) VALUES('100000000000005004' , '100000000000000005', 'Alliance.new',   '新增盟友', '0');

INSERT INTO sys_perm(`id`, `group_id`, `identifier`, `name`,`tag`) VALUES('100000000000005005' , '100000000000000005', 'Alliance.edit.state',  '修改盟友支付状态', '0');
INSERT INTO sys_perm(`id`, `group_id`, `identifier`, `name`,`tag`) VALUES('100000000000005006' , '100000000000000005', 'Alliance.edit.state.up',  '升级盟友', '0');

/**/
/*INSERT INTO sys_perm(`id`, `group_id`, `identifier`, `name`,`tag`) VALUES('100000000000005007' , '100000000000000005', 'AllianceBonus.edit',    '编辑盟友奖金', '0');*/
/*INSERT INTO sys_perm(`id`, `group_id`, `identifier`, `name`,`tag`) VALUES('100000000000005008' , '100000000000000005', 'AllianceBonus.delete',  '删除盟友奖金', '0');*/
/*INSERT INTO sys_perm(`id`, `group_id`, `identifier`, `name`,`tag`) VALUES('100000000000005009' , '100000000000000005', 'AllianceBonus.view',    '查看盟友奖金', '0');*/
/*INSERT INTO sys_perm(`id`, `group_id`, `identifier`, `name`,`tag`) VALUES('100000000000005010' , '100000000000000005', 'AllianceBonus.new',     '新增盟友奖金', '0');*/
/**/
/* INSERT INTO sys_perm_group(`id`, `org_id`, `pid`, `identifier`, `name`)*/
/* VALUES('100000000000000006','100000000000000001','100000000000000001', 'wallet_management','钱包管理');*/
/* INSERT INTO sys_perm(`id`, `group_id`, `identifier`, `name`,`tag`) VALUES('100000000000006001', '100000000000000006', 'Wallet.edit',  '编辑钱包', '0');*/
/* INSERT INTO sys_perm(`id`, `group_id`, `identifier`, `name`,`tag`) VALUES('100000000000006002', '100000000000000006', 'Wallet.delete','删除钱包', '0');*/
/* INSERT INTO sys_perm(`id`, `group_id`, `identifier`, `name`,`tag`) VALUES('100000000000006003', '100000000000000006', 'Wallet.view',  '查看钱包 ', '0');*/
/* INSERT INTO sys_perm(`id`, `group_id`, `identifier`, `name`,`tag`) VALUES('100000000000006004', '100000000000000006', 'Wallet.new',   '新增钱包 ', '0');*/
/* INSERT INTO sys_perm(`id`, `group_id`, `identifier`, `name`,`tag`) VALUES('100000000000006005', '100000000000000006', 'WalletCharge.edit',  '编辑钱包零钱', '0');*/
/* INSERT INTO sys_perm(`id`, `group_id`, `identifier`, `name`,`tag`) VALUES('100000000000006006', '100000000000000006', 'WalletCharge.delete','删除钱包零钱', '0');*/
/* INSERT INTO sys_perm(`id`, `group_id`, `identifier`, `name`,`tag`) VALUES('100000000000006007', '100000000000000006', 'WalletCharge.view',  '查看钱包零钱', '0');*/
/* INSERT INTO sys_perm(`id`, `group_id`, `identifier`, `name`,`tag`) VALUES('100000000000006008', '100000000000000006', 'WalletCharge.new',   '新增钱包零钱', '0');*/
/* INSERT INTO sys_perm(`id`, `group_id`, `identifier`, `name`,`tag`) VALUES('100000000000006009', '100000000000000006', 'WalletHistory.view', '查看钱包使用记录', '0');*/
