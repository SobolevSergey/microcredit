package ru.simplgroupp.ejb;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import junit.framework.Assert;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import ru.simplgroupp.util.CalcUtils;
import ru.simplgroupp.util.MoneyString;

public class TestCalcUtils {
	
	//@Test
	public void testIntRangeScale() throws Exception {
		Object[] scale = CalcUtils.intRangeScale(-5, 3, 4, 8, 9, 12);
		Assert.assertEquals(3, scale.length);
		Assert.assertNotNull(scale[0]);
		Assert.assertNotNull(scale[1]);
		Assert.assertNotNull(scale[2]);
	}

	//@Test
	public void testGetScalePos() throws Exception {
		Object[] scale = CalcUtils.intRangeScale(-5, 3, 4, 8, 9, 12);
		
		int npos = CalcUtils.getScalePos(scale, new Integer(4));
		Assert.assertEquals(1, npos);

		npos = CalcUtils.getScalePos(scale, new Integer(-10));
		Assert.assertEquals(-1, npos);

		npos = CalcUtils.getScalePos(scale, new Integer(0));
		Assert.assertEquals(0, npos);
		
		npos = CalcUtils.getScalePos(scale, new Integer(18));
		Assert.assertEquals(-1, npos);
		
	}
	
	//@Test
	public void testDaysToDatesScale() throws Exception {
		Object[] scaleDays = CalcUtils.intRangeScale(0, 3, 4, 8, 9, 12);
		Date dateStart = new Date();
		Object[] scaleDates = CalcUtils.daysToDatesScale(dateStart, scaleDays);
		Assert.assertNotNull(scaleDates);
	}
	
	 // @Test
    public void testCalc() throws Exception{
    	System.out.println("half up "+new BigDecimal(new Double(1.6)).setScale(0, RoundingMode.HALF_UP).doubleValue());
    	System.out.println("ceiling "+new BigDecimal(new Double(1.6)).setScale(0, RoundingMode.CEILING).doubleValue());
    	System.out.println("down "+new BigDecimal(new Double(1.6)).setScale(0, RoundingMode.DOWN).doubleValue());
    	System.out.println("floor"+new BigDecimal(new Double(1.6)).setScale(0, RoundingMode.FLOOR).doubleValue());
    	System.out.println("up "+new BigDecimal(new Double(1.6)).setScale(0, RoundingMode.UP).doubleValue());
    }
    
  //@Test
  	public void testMoneyString(){
  		Double d=CalcUtils.calcYearStake(0.025, new Date());
  		System.out.println(MoneyString.num2str(false,d));
  		d=new Double(2876);
  		System.out.println(MoneyString.num2str(false,d));
  		d=new Double(123.45);
  		System.out.println(MoneyString.num2str(true,d));
  		d=new Double(564);
  		System.out.println(MoneyString.num2str(true,d));
  	}
  	
  //	@Test
  	public void testCalcSumBack(){
  		double d=CalcUtils.calcSumBack(8000d, 9160d, 0.02d,DateUtils.addDays(new Date(), 2),new Date());
  		System.out.println("SumBack "+d);
  	}
  	
  	@Test
  	public void testCalcPercentForSubtract(){
  		double d=CalcUtils.calcSumPercentForSubtract(new Date(), DateUtils.addDays(new Date(), 2), 10000d, 0.02d);
  		System.out.println("SumBack "+d);
  	}
}
