-- 我的订单
select a.alliance_name, o.user_id, o.order_number, o.created_date, o.total_price, o.status from t_order o
LEFT JOIN t_alliance a on a.user_id =  o.user_id
where o.user_id=8
AND o.`status`='CLOSED_CONFIRMED'
AND o.created_date>STR_TO_DATE((SELECT value FROM t_config_field WHERE field='starting_time'),'%Y-%m-%d')
AND o.created_date<DATE_ADD(STR_TO_DATE((SELECT value FROM t_config_field WHERE field='starting_time'),'%Y-%m-%d') , interval (SELECT value FROM t_config_field WHERE field='settlement_cycle') MONTH);


-- 我的团队
select a1.user_id,a1.alliance_name from t_alliance a1,t_alliance a2 where a1.invitor_alliance_id=a2.id  and a1.alliance_ship=0 and a2.user_id=8
        AND o.`status`='CLOSED_CONFIRMED'
        AND o.created_date>STR_TO_DATE((SELECT value FROM t_config_field WHERE field='starting_time'),'%Y-%m-%d')
        AND o.created_date<DATE_ADD(STR_TO_DATE((SELECT value FROM t_config_field WHERE field='starting_time'),'%Y-%m-%d') , interval (SELECT value FROM t_config_field WHERE field='settlement_cycle') MONTH);

-- 个人订单
select a.alliance_name, o.user_id, o.order_number, o.created_date, o.total_price from t_order o
LEFT JOIN t_alliance a on a.user_id =  o.user_id
where o.user_id=8;

-- 团队订单列表（不包括自已)
SELECT o.id,o.order_number,o.total_price,o.user_id,kid.alliance_name FROM t_order o
        LEFT JOIN t_user u on u.id=o.user_id
        LEFT JOIN t_alliance a on a.user_id = 8
        LEFT JOIN t_alliance kid on kid.invitor_alliance_id=a.id and kid.user_id=o.user_id
        WHERE kid.user_id!=a.user_id
        AND o.`status`='CLOSED_CONFIRMED'
        AND o.created_date>STR_TO_DATE((SELECT value FROM t_config_field WHERE field='starting_time'),'%Y-%m-%d')
        AND o.created_date<DATE_ADD(STR_TO_DATE((SELECT value FROM t_config_field WHERE field='starting_time'),'%Y-%m-%d') , interval (SELECT value FROM t_config_field WHERE field='settlement_cycle') MONTH);

-- 个人及团队订单总额
SELECT
(
  SELECT sum(total_price) FROM t_order o WHERE user_id=8
  AND o.`status`='CLOSED_CONFIRMED'
  AND o.created_date>STR_TO_DATE((SELECT value FROM t_config_field WHERE field='starting_time'),'%Y-%m-%d')
  AND o.created_date<DATE_ADD(STR_TO_DATE((SELECT value FROM t_config_field WHERE field='starting_time'),'%Y-%m-%d') , interval (SELECT value FROM t_config_field WHERE field='settlement_cycle') MONTH)
) +
(
SELECT sum(o.total_price) FROM t_order o
        LEFT JOIN t_user u on u.id=o.user_id
        LEFT JOIN t_alliance a on a.user_id = 8
        LEFT JOIN t_alliance kid on kid.invitor_alliance_id=a.id and kid.user_id=o.user_id
        WHERE kid.user_id!=a.user_id
        AND o.`status`='CLOSED_CONFIRMED'
        AND o.created_date>STR_TO_DATE((SELECT value FROM t_config_field WHERE field='starting_time'),'%Y-%m-%d')
        AND o.created_date<DATE_ADD(STR_TO_DATE((SELECT value FROM t_config_field WHERE field='starting_time'),'%Y-%m-%d') , interval (SELECT value FROM t_config_field WHERE field='settlement_cycle') MONTH)
);


-- 团队订单总额
SELECT sum(total) FROM (SELECT u.real_name, o.order_number, o.created_date, o.total_price as total FROM t_order o
        LEFT JOIN t_user u on u.id=o.user_id
        LEFT JOIN t_alliance a on a.user_id=8
        LEFT JOIN t_alliance kid on kid.invitor_alliance_id=a.id
        WHERE ( o.user_id=a.user_id OR o.user_id=kid.user_id)
        AND o.`status`='CLOSED_CONFIRMED'
        AND o.created_date>STR_TO_DATE((SELECT value FROM t_config_field WHERE field='starting_time'),'%Y-%m-%d')
        AND o.created_date<DATE_ADD(STR_TO_DATE((SELECT value FROM t_config_field WHERE field='starting_time'),'%Y-%m-%d') , interval (SELECT value FROM t_config_field WHERE field='settlement_cycle') MONTH)
GROUP BY o.order_number ) t where 1=1;


-- 团队订单列表
SELECT u.real_name, a.id, kid.id, o.user_id, o.order_number, o.created_date, o.total_price as total FROM t_order o
        LEFT JOIN t_user u on u.id=o.user_id
        LEFT JOIN t_alliance a on a.user_id=8
        LEFT JOIN t_alliance kid on kid.invitor_alliance_id=a.id
        WHERE ( o.user_id=a.user_id OR o.user_id=kid.user_id)
        AND o.`status`='CLOSED_CONFIRMED'
        AND o.created_date>STR_TO_DATE((SELECT value FROM t_config_field WHERE field='starting_time'),'%Y-%m-%d')
        AND o.created_date<DATE_ADD(STR_TO_DATE((SELECT value FROM t_config_field WHERE field='starting_time'),'%Y-%m-%d') , interval (SELECT value FROM t_config_field WHERE field='settlement_cycle') MONTH)

-- 查询平均分红
select
   ROUND(sum((item.price-item.cost_price)*item.quantity*JSON_EXTRACT(psp.proportion,'$.value')/100.0)*
   (
     select
     value/100.0
     from t_config_field
     where field='fixed_proportion')/
    (
      select count(id)
      from t_alliance
      where
       alliance_type=1 and alliance_ship=0
     )
    ,2)
from t_order_item item
     inner JOIN t_order o on item.order_id=o.id and
            o.created_date>STR_TO_DATE(
           (select value from t_config_field
            where field='starting_time'),'%Y-%m-%d')
            and o.status='CLOSED_CONFIRMED'
     LEFT JOIN t_product_settlement_proportion psp on psp.product_id=item.product_id and psp.type='STOCKHOLDER'