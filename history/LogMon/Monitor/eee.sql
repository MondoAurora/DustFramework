CREATE TABLE `rec_data` (             
            `rec_id` int(11) NOT NULL,          
            `data_key` varchar(20) NOT NULL,    
            `data_value` text,                  
            PRIMARY KEY  (`rec_id`,`data_key`)  
          ) ENGINE=InnoDB DEFAULT CHARSET=utf8  

CREATE TABLE `rec_head` (                                                    
            `rec_id` int(11) NOT NULL auto_increment COMMENT 'The unique record key',  
            `rec_type` varchar(20) NOT NULL COMMENT 'The data type',                   
            `rec_type_ver` int(11) default '1' COMMENT 'data type version',            
            `rec_master_id` int(11) default NULL COMMENT 'master key if any',          
            PRIMARY KEY  (`rec_id`),                                                   
            KEY `rec_type` (`rec_type`),                                               
            KEY `rec_master_id` (`rec_master_id`)                                      
          ) ENGINE=InnoDB DEFAULT CHARSET=utf8                                         




select distinct h.rec_id, h.rec_type, h.rec_type_ver from rec_head h, rec_data d1, rec_data d2 
where h.rec_type='Cube' and h.rec_id=d1.rec_id and h.rec_id=d2.rec_id and ( (d1.data_key = 'start') and ('2010.01.12 02:57:00.00' < d1.data_value)  ) AND ( (d2.data_key = 'end') and ('2010.01.12 03:17:00.00' > d2.data_value)  ) ;



select * from rec_data where rec_id in (5,10,15,22,37,55,66,80,94,101,110,120,130,147,157,165,178,196);



select distinct h.rec_id, h.rec_type, h.rec_type_ver from rec_head h , rec_data d0 , rec_data d1 
where h.rec_type='Cube' and ( d0.(data_key = 'start') and ('2010.01.12 02:57:00.00' < d0.data_value)  ) AND ( d1.(data_key = 'end') and ('2010.01.12 03:17:00.00' > d1.data_value)  ) and h.rec_id=d0.rec_id and h.rec_id=d1.rec_id ;



select h.rec_id, h.rec_type, h.rec_type_ver from rec_head h where h.rec_type='imtaLogCollOne' and h.rec_master_id=5;



select min(str_to_date(data_value, '%Y.%m.%d %H:%i:%s.%f')) as minTime, max(str_to_date(data_value, '%Y.%m.%d %H:%i:%s.%f')) as maxTime from rec_data where data_key = 'start';
