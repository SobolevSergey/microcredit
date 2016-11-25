select date_trunc('month', dates.d),date_trunc('month',datecontest),count(id)  from generate_series('2015-02-01'::date, '2015-10-28', '1 month') as dates(d) left outer join creditrequest on (date_trunc('month',datecontest)=date_trunc('month', dates.d)) group by date_trunc('month', dates.d),date_trunc('month',datecontest) order by date_trunc('month', dates.d) asc;

select date_trunc('month', dates.d),date_trunc('month',datecontest),count(creditrequest.id) from generate_series('2015-02-01'::date, '2015-10-28', '1 month') as dates(d) left outer join creditrequest on (date_trunc('month',datecontest)=date_trunc('month', dates.d)) and creditrequest.status_id = 2 group by date_trunc('month', dates.d),date_trunc('month',datecontest) order by date_trunc('month', dates.d) asc;



select date_trunc('month', dates.d),date_trunc('month',datecontest) ,(count(distinct peoplepersonal.id)::float / (CASE count(distinct creditrequest.id) WHEN 0 THEN 1 ELSE count(distinct creditrequest.id) END)::float)*100 from generate_series('2015-02-01'::date, '2015-10-28', '1 month') as dates(d) left outer join creditrequest on (date_trunc('month',datecontest)=date_trunc('month', dates.d)) left outer join peoplepersonal on (date_trunc('month',databeg)=date_trunc('month', dates.d)) and (select count(*) from creditrequest c where c.peoplemain_id = peoplepersonal.peoplemain_id) > 0  group by date_trunc('month', dates.d),date_trunc('month',datecontest),date_trunc('month',databeg) order by date_trunc('month', dates.d) asc;

select date_trunc('month', dates.d),date_trunc('month',creditdatabeg),count(id)::float  
from generate_series('2015-02-01'::date, '2015-10-28', '1 month') as dates(d) left outer join credit 
on (date_trunc('month',creditdatabeg)=date_trunc('month', dates.d)) 
group by date_trunc('month', dates.d),date_trunc('month',creditdatabeg) order by date_trunc('month', dates.d) asc;

select date_trunc('month', dates.d),date_trunc('month',creditdatabeg),COALESCE(sum(creditsum),0) 
from generate_series('2015-02-01'::date, '2015-10-28', '1 month') as dates(d) left outer join credit 
on (date_trunc('month',creditdatabeg)=date_trunc('month', dates.d)) 
group by date_trunc('month', dates.d),date_trunc('month',creditdatabeg) order by date_trunc('month', dates.d) asc;


select date_trunc('month', dates.d),date_trunc('month',creditdatabeg),COALESCE(sum(creditsum),0)::float / (CASE count(id) WHEN 0 THEN 1 ELSE count(id) END)::float  
from generate_series('2015-02-01'::date, '2015-10-28', '1 month') as dates(d) left outer join credit 
on (date_trunc('month',creditdatabeg)=date_trunc('month', dates.d)) 
group by date_trunc('month', dates.d),date_trunc('month',creditdatabeg) order by date_trunc('month', dates.d) asc;

select date_trunc('month', dates.d),date_trunc('month',creditdatabeg),(COALESCE(sum(creditsum*creditpercent),0)::float / (CASE COALESCE(sum(creditsum),0) WHEN 0 THEN 1 ELSE COALESCE(sum(creditsum),0) END)::float)*100  
from generate_series('2015-02-01'::date, '2015-10-28', '1 month') as dates(d) left outer join credit 
on (date_trunc('month',creditdatabeg)=date_trunc('month', dates.d)) 
group by date_trunc('month', dates.d),date_trunc('month',creditdatabeg) order by date_trunc('month', dates.d) asc;

select date_trunc('month', dates.d),date_trunc('month',creditdatabeg),avg(DATE_PART('day', creditdataend - creditdatabeg)) 
from generate_series('2015-02-01'::date, '2015-10-28', '1 month') as dates(d) left outer join credit 
on (date_trunc('month',creditdatabeg)=date_trunc('month', dates.d)) 
group by date_trunc('month', dates.d),date_trunc('month',creditdatabeg) order by date_trunc('month', dates.d) asc;

select date_trunc('month', dates.d),date_trunc('month',datecontest),(count(CASE status_id WHEN 2 THEN 1 ELSE null END)::float /(CASE count(id) WHEN 0 THEN 1 ELSE count(id) END)::float)*100  
from generate_series('2015-02-01'::date, '2015-10-28', '1 month') as dates(d) left outer join creditrequest
on (date_trunc('month',datecontest)=date_trunc('month', dates.d)) 
group by date_trunc('month', dates.d),date_trunc('month',datecontest) order by date_trunc('month', dates.d) asc;

 



