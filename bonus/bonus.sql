--盟友个人订单(当前月)
SELECT * from t_order o 
   LEFT JOIN t_user u on u.id=o.user_id
   LEFT JOIN t_alliance a ON a.user_id=u.id 
WHERE o.`status`='CLOSED_CONFIRMED' AND year(o.created_date) = year(curdate()) AND month(o.created_date) = month(curdate())
      AND a.user_id=#{user_id}

--盟友团队
select a1.user_id,a1.alliance_name from t_alliance a1,t_alliance a2 where a1.invitor_alliance_id=a2.id  and a1.alliance_ship=0 and a2.user_id=8

--盟友个人订单（按年月)
SELECT * from t_order o 
   LEFT JOIN t_user u on u.id=o.user_id
   LEFT JOIN t_alliance a ON a.user_id=u.id 
WHERE o.`status`='CLOSED_CONFIRMED' 
    AND year(o.created_date) = #{year}   
    AND month(o.created_date) = #{month}
    AND a.user_id=#{user_id}


--- 盟友个人订单（周期内） PASS
SELECT o.id, o.order_number,o.total_price FROM t_order o 
LEFT JOIN t_user u on u.id=o.user_id
LEFT JOIN t_alliance a ON a.user_id=u.id 
WHERE o.status!='CLOSED_CONFIRMED'
            AND o.created_date>STR_TO_DATE((SELECT value FROM t_config_field WHERE field='starting_time'),'%Y-%m-%d') 
            AND o.created_date<DATE_ADD(STR_TO_DATE((SELECT value FROM t_config_field WHERE field='starting_time'),'%Y-%m-%d') , interval (SELECT value FROM t_config_field WHERE field='settlement_cycle') MONTH) 
            AND a.user_id=6;

--盟友团队订单项 (周期内) PASS
SELECT o.id,o.order_number,o.total_price,o.user_id,kid.alliance_name FROM t_order_item item LEFT JOIN t_order o on item.order_id=o.id 
        LEFT JOIN t_user u on u.id=o.user_id
        LEFT JOIN t_alliance a on a.user_id = #{user_id}
        LEFT JOIN t_alliance kid on kid.invitor_alliance_id=a.id and kid.user_id=o.user_id
        WHERE kid.user_id!=a.user_id
        -- 过滤订单
        AND o.`status`='CLOSED_CONFIRMED' 
        AND o.created_date>STR_TO_DATE((SELECT value FROM t_config_field WHERE field='starting_time'),'%Y-%m-%d') 
        AND o.created_date<DATE_ADD(STR_TO_DATE((SELECT value FROM t_config_field WHERE field='starting_time'),'%Y-%m-%d') , interval (SELECT value FROM t_config_field WHERE field='settlement_cycle') MONTH);


--盟友团队订单 (周期内) PASS
SELECT o.id,o.order_number,o.total_price,o.user_id,kid.alliance_name FROM t_order o
        LEFT JOIN t_user u on u.id=o.user_id
        LEFT JOIN t_alliance a on a.user_id = #{user_id}
        LEFT JOIN t_alliance kid on kid.invitor_alliance_id=a.id and kid.user_id=o.user_id
        WHERE kid.user_id!=a.user_id
        -- 过滤订单
        AND o.`status`='CLOSED_CONFIRMED' 
        AND o.created_date>STR_TO_DATE((SELECT value FROM t_config_field WHERE field='starting_time'),'%Y-%m-%d') 
        AND o.created_date<DATE_ADD(STR_TO_DATE((SELECT value FROM t_config_field WHERE field='starting_time'),'%Y-%m-%d') , interval (SELECT value FROM t_config_field WHERE field='settlement_cycle') MONTH);


--盟友个人及团人订单 (周期内) PASS
SELECT o.id,o.order_number,o.total_price,o.user_id,a.user_id,a.alliance_name,kid.user_id as kid_user_id,kid.alliance_name FROM t_order o
        LEFT JOIN t_user u on u.id=o.user_id
        LEFT JOIN t_alliance a on a.id=#{user_id}
        LEFT JOIN t_alliance kid on kid.invitor_alliance_id=a.id
        -- 过滤订单
        WHERE ( o.user_id=a.user_id OR o.user_id=kid.user_id)
        AND o.`status`='CLOSED_CONFIRMED' 
        AND o.created_date>STR_TO_DATE((SELECT value FROM t_config_field WHERE field='starting_time'),'%Y-%m-%d') 
        AND o.created_date<DATE_ADD(STR_TO_DATE((SELECT value FROM t_config_field WHERE field='starting_time'),'%Y-%m-%d') , interval (SELECT value FROM t_config_field WHERE field='settlement_cycle') MONTH);

SELECT o.id,o.order_number,o.total_price,o.user_id,a.user_id,a.id,a.alliance_name,kid.user_id as kid_user_id,kid.alliance_name FROM t_order o
        LEFT JOIN t_user u on u.id=o.user_id
        LEFT JOIN t_alliance a on a.id=337
        LEFT JOIN t_alliance kid on kid.invitor_alliance_id=a.id
        -- 过滤订单
        WHERE ( o.user_id=a.user_id OR o.user_id=kid.user_id)
        AND o.`status`!='CLOSED_CONFIRMED' 
        AND o.created_date>STR_TO_DATE((SELECT value FROM t_config_field WHERE field='starting_time'),'%Y-%m-%d') 
        AND o.created_date<DATE_ADD(STR_TO_DATE((SELECT value FROM t_config_field WHERE field='starting_time'),'%Y-%m-%d') , interval (SELECT value FROM t_config_field WHERE field='settlement_cycle') MONTH);


--盟友个人及团人订单总额 (周期内) PASS 
SELECT sum(o.total_price) FROM t_order o
        LEFT JOIN t_user u on u.id=o.user_id
        LEFT JOIN t_alliance a on a.user_id=#{user_id}
        LEFT JOIN t_alliance kid on kid.invitor_alliance_id=a.id
        -- 过滤订单
        WHERE ( o.user_id=a.user_id OR o.user_id=kid.user_id)
        AND o.`status`='CLOSED_CONFIRMED' 
        AND o.created_date>STR_TO_DATE((SELECT value FROM t_config_field WHERE field='starting_time'),'%Y-%m-%d') 
        AND o.created_date<DATE_ADD(STR_TO_DATE((SELECT value FROM t_config_field WHERE field='starting_time'),'%Y-%m-%d') , interval (SELECT value FROM t_config_field WHERE field='settlement_cycle') MONTH);



--查询个人提成总额(周期内)
SELECT ROUND(sum(((item.price-item.cost_price)*item.quantity*JSON_EXTRACT(psp.proportion,'$.value')/100)),2), o.total_price
        FROM t_order_item item
        LEFT JOIN t_order o on item.order_id=o.id 
        LEFT JOIN t_user u on u.id=o.user_id
        LEFT JOIN t_alliance a on a.user_id = 6
        LEFT JOIN t_product_settlement_proportion psp ON psp.product_id=item.product_id AND psp.type='ALLIANCE'
        LEFT JOIN t_alliance kid on kid.invitor_alliance_id=a.id and kid.user_id=u.id 
        WHERE o.`status`='CLOSED_CONFIRMED' 
        AND kid.user_id!=a.user_id
        AND o.created_date>STR_TO_DATE((SELECT value FROM t_config_field WHERE field='starting_time'),'%Y-%m-%d') 
        AND o.created_date<DATE_ADD(STR_TO_DATE((SELECT value FROM t_config_field WHERE field='starting_time'),'%Y-%m-%d') , interval (SELECT value FROM t_config_field WHERE field='settlement_cycle') MONTH) 



--SELECT  ROUND(
--    SUM( (item.price-item.cost_price)*item.quantity*JSON_EXTRACT(psp.proportion,'$.value')/100.0) 
--       * (SELECT value/100.0 FROM t_config_field WHERE field='ratio_proportion') 
--       * (
--            (   SELECT ifnull(sum(a1.alliance_inventory_amount), 0)
--                FROM t_alliance a1, t_alliance kid
--                WHERE a1.invitor_alliance_id = kid.id AND a1.alliance_ship = 0 AND kid.user_id = 8
--            )
--            /
--            ( SELECT sum(alliance_inventory_amount) FROM t_alliance WHERE alliance_ship=0)
--    )
--,2)
