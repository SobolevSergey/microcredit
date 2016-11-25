select date_trunc('month', dates.d),date_trunc('month',longdate),count(id)  
from generate_series('2015-02-01'::date, '2015-10-28', '1 month') as dates(d)
left outer join prolong on (date_trunc('month',longdate)=date_trunc('month', dates.d)) and isactive=1
group by date_trunc('month', dates.d),date_trunc('month',longdate) order by date_trunc('month', dates.d) asc;

select date_trunc('month', dates.d),date_trunc('month',longdate),count(distinct credit_id)  
from generate_series('2015-02-01'::date, '2015-10-28', '1 month') as dates(d)
left outer join prolong on (date_trunc('month',longdate)=date_trunc('month', dates.d)) and isactive = 1
group by date_trunc('month', dates.d),date_trunc('month',longdate) order by date_trunc('month', dates.d) asc;

select date_trunc('month', dates.d),date_trunc('month',longdate),credit.id,avg(credit.creditsum) as creditsum
from generate_series('2015-02-01'::date, '2015-10-28', '1 month') as dates(d)
left outer join prolong on (date_trunc('month',longdate)=date_trunc('month', dates.d)) and isactive = 1 left outer join 
credit on prolong.credit_id = credit.id group by date_trunc('month', dates.d),date_trunc('month',longdate),credit.id;


select month,avg(creditsum) from (select date_trunc('month', dates.d) as month,date_trunc('month',longdate),credit.id,avg(credit.creditsum) as creditsum
from generate_series('2015-02-01'::date, '2015-10-28', '1 month') as dates(d)
left outer join prolong on (date_trunc('month',longdate)=date_trunc('month', dates.d)) and isactive = 1 left outer join 
credit on prolong.credit_id = credit.id group by date_trunc('month', dates.d),date_trunc('month',longdate),credit.id) as c group by month order by month asc;

select month,COALESCE(avg(DATE_PART('day', creditdataend - creditdatabeg)),0) from (select date_trunc('month', dates.d) as month,date_trunc('month',longdate),credit.id,creditdatabeg,creditdataend
from generate_series('2015-02-01'::date, '2015-10-28', '1 month') as dates(d)
left outer join prolong on (date_trunc('month',longdate)=date_trunc('month', dates.d)) and isactive = 1 left outer join 
credit on prolong.credit_id = credit.id group by date_trunc('month', dates.d),date_trunc('month',longdate),credit.id) as c group by month order by month asc;

select date_trunc('month', dates.d),date_trunc('month',longdate),COALESCE(avg(longamount),0)  
from generate_series('2015-02-01'::date, '2015-10-28', '1 month') as dates(d)
left outer join prolong on (date_trunc('month',longdate)=date_trunc('month', dates.d)) and isactive=1
group by date_trunc('month', dates.d),date_trunc('month',longdate) order by date_trunc('month', dates.d) asc;

