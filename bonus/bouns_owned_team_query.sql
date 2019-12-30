drop table if exists t_sum;
create table t_sum
(
   name varchar(30) not null,
   user_id int not null default 0,
   sum float default 0
)engine=innodb;

drop procedure if exists load_foo_test_data;

delimiter #

create procedure load_foo_test_data()
begin
  declare v_max int unsigned default (select count(user_id) from t_alliance where alliance_ship=0 and alliance_type=1);
  declare v_counter int unsigned default 0;
  DECLARE done INT DEFAULT 0;
  DECLARE userId INT;
  DECLARE cur CURSOR FOR select user_id from t_alliance where alliance_ship=0 and alliance_type=1;

  OPEN cur;
	read_loop: LOOP

			FETCH cur INTO userId;
			IF done THEN
					LEAVE read_loop;
			END IF;

			/* Do your work */
      insert into t_sum select alliance_name, user_id,
	ifnull(
      (SELECT sum(o.total_price) as sum
        -- select o.*,a.id,a.alliance_name
        FROM t_order o
		  LEFT JOIN t_user u on u.id=o.user_id
		  LEFT JOIN t_alliance a on a.user_id = o.user_id
		where o.`status`='CLOSED_CONFIRMED'
          and o.user_id=userId
		  AND o.created_date>STR_TO_DATE((SELECT value FROM t_config_field WHERE field='starting_time'),'%Y-%m-%d')
		  AND o.created_date < DATE_ADD(STR_TO_DATE((SELECT value FROM t_config_field WHERE field='starting_time'),'%Y-%m-%d') , interval (SELECT value FROM t_config_field WHERE field='settlement_cycle') MONTH)
        GROUP BY o.user_id
       -- order by user_id asc,o.id asc
       ), 0)
      from t_alliance where user_id=userId;

			/* Do your work */
	END LOOP;
	CLOSE cur;


  truncate table t_sum;
  start transaction;
  while v_counter < v_max do
    set v_counter=v_counter+1;
  end while;
  commit;

end #

delimiter ;

call load_foo_test_data();

select * from t_sum order by user_id;
-- 更新field
update st_statistics_record set st_statistics_record.record_value = (select SUM(sum) from t_sum);