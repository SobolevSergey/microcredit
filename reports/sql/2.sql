select date_trunc('month', dates.d),date_trunc('month',creditdataend),count(id)  
from generate_series('2015-02-01'::date, '2015-10-28', '1 month') as dates(d)
left outer join credit on (date_trunc('month',creditdataend)=date_trunc('month', dates.d)) 
group by date_trunc('month', dates.d),date_trunc('month',creditdataend) order by date_trunc('month', dates.d) asc;

select date_trunc('month', dates.d),date_trunc('month',creditdataend),COALESCE(sum(creditsumback),0)  
from generate_series('2015-02-01'::date, '2015-10-28', '1 month') as dates(d)
left outer join credit on (date_trunc('month',creditdataend)=date_trunc('month', dates.d)) 
group by date_trunc('month', dates.d),date_trunc('month',creditdataend) order by date_trunc('month', dates.d) asc;

select date_trunc('month', dates.d),date_trunc('month',processDate),count(distinct credit_id)  
from generate_series('2015-02-01'::date, '2015-10-28', '1 month') as dates(d)
left outer join payment on (date_trunc('month',processDate)=date_trunc('month', dates.d)) 
group by date_trunc('month', dates.d),date_trunc('month',processDate) order by date_trunc('month', dates.d) asc;

select date_trunc('month', dates.d),date_trunc('month',processDate),COALESCE(sum(amount),0) 
from generate_series('2015-02-01'::date, '2015-10-28', '1 month') as dates(d)
left outer join payment on (date_trunc('month',processDate)=date_trunc('month', dates.d)) 
group by date_trunc('month', dates.d),date_trunc('month',processDate) order by date_trunc('month', dates.d) asc;

