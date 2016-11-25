select date_trunc('month', dates.d),date_trunc('month',creditdataend),count(id)  
from generate_series('2015-02-01'::date, '2015-10-28', '1 month') as dates(d)
left outer join credit on (date_trunc('month',creditdataend)=date_trunc('month', dates.d)) 
group by date_trunc('month', dates.d),date_trunc('month',creditdataend) order by date_trunc('month', dates.d) asc;

select date_trunc('month', dates.d),date_trunc('month',creditdataendfact),count(id)  
from generate_series('2015-02-01'::date, '2015-10-28', '1 month') as dates(d)
left outer join credit on (date_trunc('month',creditdataendfact)=date_trunc('month', dates.d)) and isover is true
group by date_trunc('month', dates.d),date_trunc('month',creditdataendfact) order by date_trunc('month', dates.d) asc;