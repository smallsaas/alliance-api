

SELECT a.alliance_name as '盟友',IFNULL(pon_sum,0)  as '个人订单总额',
( pon_sum + other_sum ) as '团队订单总额',
(select record_value from st_statistics_record)
 as '所有盟友总额',
(select
 ROUND(sum((item.price-item.cost_price)*item.quantity*JSON_EXTRACT(psp.proportion,'$.value')/100.0),2)
from t_order_item item
     inner JOIN t_order o on item.order_id=o.id and
            o.created_date>STR_TO_DATE(
           (select value from t_config_field
            where field='starting_time'),'%Y-%m-%d')
            and o.status='CLOSED_CONFIRMED'
     LEFT JOIN t_product_settlement_proportion psp on psp.product_id=item.product_id and psp.type='STOCKHOLDER')
as '平台总利润',
IFNULL(
format(( pon_sum + other_sum )/(select record_value from st_statistics_record),4) ,
format(( pon_sum )/(select record_value from st_statistics_record),4) )
as '占比',




ROUND((select
 sum((item.price-item.cost_price)*item.quantity*JSON_EXTRACT(psp.proportion,'$.value')/100.0)
from t_order_item item
     inner JOIN t_order o on item.order_id=o.id and
            o.created_date>STR_TO_DATE(
           (select value from t_config_field
            where field='starting_time'),'%Y-%m-%d')
            and o.status='CLOSED_CONFIRMED'
     LEFT JOIN t_product_settlement_proportion psp on psp.product_id=item.product_id and psp.type='STOCKHOLDER')
* IFNULL(
format(( pon_sum + other_sum )/(select record_value from st_statistics_record),4) ,
format(( pon_sum )/(select record_value from st_statistics_record),4) ),2)
as '动态分红',


 (select
 ROUND(sum((item.price-item.cost_price)*item.quantity*JSON_EXTRACT(psp.proportion,'$.value')/100.0)*
   (
     select
     value/100.0
     from t_config_field
     where field='fixed_proportion')/
  (  select count(id)
     from t_alliance
     where
       alliance_type=1 and alliance_ship=0)
   ,2)
from t_order_item item
     inner JOIN t_order o on item.order_id=o.id and
            o.created_date>STR_TO_DATE(
           (select value from t_config_field
            where field='starting_time'),'%Y-%m-%d')
            and o.status='CLOSED_CONFIRMED'
     LEFT JOIN t_product_settlement_proportion psp on psp.product_id=item.product_id and psp.type='STOCKHOLDER')
as '平均分红',

(select `value` from t_config_field where field='starting_time')as '分红开始时间' ,
DATE_ADD((SELECT STR_TO_DATE(VALUE, '%Y-%m-%d')FROM	t_config_field WHERE field = 'starting_time'),
INTERVAL (SELECT VALUE FROM t_config_field	WHERE field = 'settlement_cycle') MONTH) as '分红结束时间'

from
t_alliance a
left JOIN
-- 查询个人
(
  SELECT sum(total_price) as pon_sum ,a.id as id,a.invitor_alliance_id as invitor_id
     FROM t_alliance a
     LEFT JOIN t_user u on u.id= a.user_id
     left JOIN t_order o on o.user_id=u.id
  where
  o.`status`='CLOSED_CONFIRMED'
  AND o.created_date>STR_TO_DATE((SELECT value FROM t_config_field WHERE field='starting_time'),'%Y-%m-%d')
  AND o.created_date<DATE_ADD(STR_TO_DATE((SELECT value FROM t_config_field WHERE field='starting_time'),'%Y-%m-%d') ,
 interval (SELECT value FROM t_config_field WHERE field='settlement_cycle') MONTH)
GROUP BY a.id
) pon on  a.id=pon.id
left JOIN
-- 查询除个人外团队
(
SELECT sum(total) as other_sum,id FROM
(SELECT a.id, IFNULL(sum(o.total_price), 0) as total
        FROM t_alliance a
        LEFT JOIN t_alliance kid on kid.invitor_alliance_id=a.id
        LEFT JOIN  t_user u on kid.user_id=u.id
        LEFT JOIN t_order o on u.id=o.user_id
        where (o.user_id=kid.user_id or o.user_id=a.user_id)
        AND o.`status`='CLOSED_CONFIRMED'
        AND o.created_date>STR_TO_DATE((SELECT value FROM t_config_field WHERE field='starting_time'),'%Y-%m-%d')
        AND o.created_date<DATE_ADD(STR_TO_DATE((SELECT value FROM t_config_field WHERE field='starting_time'),'%Y-%m-%d')
        , interval (SELECT value FROM t_config_field WHERE field='settlement_cycle') MONTH)
        GROUP BY a.id
 ) t
where 1=1
GROUP BY t.id
)other on  pon.id=other.id

;






