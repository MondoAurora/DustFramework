select min(str_to_date('%y', data_value) from rec_data where data_key = 'start';

select data_value from rec_data where data_key = 'start' limit 2;

select min(str_to_date(data_value, '%Y.%m.%d %H:%i:%s.%f')) as minTime, max(str_to_date(data_value, '%Y.%m.%d %H:%i:%s.%f')) as maxTime 
  from rec_data d, rec_head h
  where data_key = 'start' and d.rec_id = h.rec_id and h.rec_type = 'Cube';



select distinct h.rec_id, h.rec_type, h.rec_type_ver 
  from rec_head h, rec_data d0, rec_data d1 
  where h.rec_type='Cube' and ( (d0.data_key = 'start') and ('2010.01.12 02:57:00.00' < d0.data_value)  ) 
    AND ( (d1.data_key = 'end') and ('2010.01.12 03:17:00.00' > d1.data_value)  ) 
    and h.rec_id=d0.rec_id and h.rec_id=d1.rec_id;