select date_trunc('month', dates.d),date_trunc('month',databeg),count(distinct credit.id)  
from generate_series('2015-02-01'::date, '2015-10-28', '1 month') as dates(d)
left outer join collector on (date_trunc('month',databeg)=date_trunc('month', dates.d)) left outer join credit on credit.id = collector.credit_id 
group by date_trunc('month', dates.d),date_trunc('month',databeg) order by date_trunc('month', dates.d) asc;

select month,COALESCE(sum(c.creditsumback),0) from  (select date_trunc('month', dates.d) as month,date_trunc('month',collector.databeg),credit.creditsumdebt as creditsumdebt,credit.creditsum,credit.creditsumback as creditsumback  
from generate_series('2014-02-01'::date, '2015-12-28', '1 month') as dates(d)
left outer join collector on (date_trunc('month',databeg)=date_trunc('month', dates.d)) left outer join credit on credit.id = collector.credit_id 
group by date_trunc('month', dates.d),date_trunc('month',databeg),credit.id order by date_trunc('month', dates.d) asc) as c group by month order by month asc;

select month,COALESCE(sum(CASE WHEN c.creditsumdebt is null THEN c.creditsum WHEN c.creditsumdebt <= 0 THEN c.creditsum  else c.creditsumdebt end),0) from  (select date_trunc('month', dates.d) as month,date_trunc('month',collector.databeg),credit.creditsumdebt as creditsumdebt,credit.creditsum  
from generate_series('2014-02-01'::date, '2015-12-28', '1 month') as dates(d)
left outer join collector on (date_trunc('month',databeg)=date_trunc('month', dates.d)) left outer join credit on credit.id = collector.credit_id 
group by date_trunc('month', dates.d),date_trunc('month',databeg),credit.id order by date_trunc('month', dates.d) asc) as c group by month order by month asc;


select month,COALESCE(sum(c.creditsumback),0) - COALESCE(sum(CASE WHEN c.creditsumdebt is null THEN c.creditsum WHEN c.creditsumdebt <= 0 THEN c.creditsum  else c.creditsumdebt end),0) from  (select date_trunc('month', dates.d) as month,date_trunc('month',collector.databeg),credit.creditsumdebt as creditsumdebt,credit.creditsum,credit.creditsumback as creditsumback  
from generate_series('2014-02-01'::date, '2015-12-28', '1 month') as dates(d)
left outer join collector on (date_trunc('month',databeg)=date_trunc('month', dates.d)) left outer join credit on credit.id = collector.credit_id 
group by date_trunc('month', dates.d),date_trunc('month',databeg),credit.id order by date_trunc('month', dates.d) asc) as c group by month order by month asc;

select date_trunc('month', dates.d),date_trunc('month',datedebt),COALESCE(sum(debt.amount),0)  
from generate_series('2015-01-01'::date, '2015-12-31', '1 month') as dates(d)
left outer join debt on (date_trunc('month',datedebt)=date_trunc('month', dates.d)) 
group by date_trunc('month', dates.d),date_trunc('month',datedebt) order by date_trunc('month', dates.d) asc;

select month,COALESCE(sum(CASE WHEN c.creditsumdebt is null THEN c.creditsum WHEN c.creditsumdebt <= 0 THEN c.creditsum  else c.creditsumdebt end),0) from (select date_trunc('month', dates.d) as month,date_trunc('month',debt.datedebt),credit.id,credit.creditsumback as creditsumback,credit.creditsum as creditsum,credit.creditsumdebt as creditsumdebt 
from generate_series('2015-01-01'::date, '2015-12-31', '1 month') as dates(d)
left outer join debt on (date_trunc('month',datedebt)=date_trunc('month', dates.d)) left outer join credit on credit.id = (select acceptedcredit_id from creditrequest where creditrequest.id = debt.creditrequest_id) 
group by date_trunc('month', dates.d),date_trunc('month',datedebt),credit.id order by date_trunc('month', dates.d) asc) as c group by month order by month asc;

select month,COALESCE(sum(c.creditsumback),0) - COALESCE(sum(CASE WHEN c.creditsumdebt is null THEN c.creditsum WHEN c.creditsumdebt <= 0 THEN c.creditsum  else c.creditsumdebt end),0) from (select date_trunc('month', dates.d) as month,date_trunc('month',debt.datedebt),credit.id,credit.creditsumback as creditsumback,credit.creditsum as creditsum,credit.creditsumdebt as creditsumdebt 
from generate_series('2015-01-01'::date, '2015-12-31', '1 month') as dates(d)
left outer join debt on (date_trunc('month',datedebt)=date_trunc('month', dates.d)) left outer join credit on credit.id = (select acceptedcredit_id from creditrequest where creditrequest.id = debt.creditrequest_id) 
group by date_trunc('month', dates.d),date_trunc('month',datedebt),credit.id order by date_trunc('month', dates.d) asc) as c group by month order by month asc;

