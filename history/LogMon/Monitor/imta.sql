select a.data_key, data_value from rec_data a where rec_id=5;

select data_key, data_value from rec_data where rec_id=501;

select count(*) from rec_data;
select count(*) from rec_head;


select data_key, data_value from rec_data dstat where dstat.data_key='status' and dstat.data_value='D';


select dstat.data_value as stat, dcount.data_value as cnt
from rec_data dstat, rec_data dcount 
where dstat.data_key='status' and dstat.data_value='D' 
  and dcount.data_value>200 and dcount.data_key='count' and dstat.rec_id=dcount.rec_id;


select dhead.rec_master_id as mastid, mstart.data_value as segBeg, mend.data_value as segEnd, duser.data_value as authUser, dstat.data_value as stat, dcount.data_value as cnt
from rec_data dstat, rec_data dcount, rec_data duser, rec_head dhead, rec_data mstart, rec_data mend
where dstat.data_key='status' and dstat.data_value='D' 
  and dcount.data_value>100 and dcount.data_key='count'
  and duser.data_key='authUser' and duser.data_value<>'null'
  and dstat.rec_id=dcount.rec_id and dstat.rec_id=duser.rec_id and dstat.rec_id=dhead.rec_id
  and mstart.rec_id=dhead.rec_master_id and mend.rec_id=dhead.rec_master_id
  and mstart.data_key='start' and mend.data_key='end' 