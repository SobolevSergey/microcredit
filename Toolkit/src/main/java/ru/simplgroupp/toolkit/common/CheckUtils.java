package ru.simplgroupp.toolkit.common;

public class CheckUtils {

	private static final int[] wsnils=new int[] {9,8,7,6,5,4,3,2,1}; 
	private static final int[] inn12_1=new int[]{7,2,4,10,3,5,9,4,6,8};
	private static final int[] inn12_2=new int[]{3,7,2,4,10,3,5,9,4,6,8};
	private static final int[] inn10=new int[]{2,4,10,3,5,9,4,6,8};
	
	public static Integer CalcSm(String arr,int[] val,int lng)
	{
		Integer sm=0;
		for (int i=0;i<lng;i++)
		{
			Integer u=Convertor.toInteger(arr.substring(i,i+1));
			sm+=u*val[i];
		}
		return sm;
	}
	
	/**
	 * проверяем снилс, возвращаем true если правильный 
	 */
	public static boolean CheckSnils(String arr)
	{
		boolean b=false;
		
		if  (arr.length()==11)
		{
		  String value=arr.substring(arr.length()-2, arr.length());
		  Integer sm=CalcSm(arr,wsnils,arr.length()-2);	
		  if ((sm==100)||(sm==101))
		  {
			  if (value.equals("00"))
				  b=true;
		  }
		
	      if(sm<100)
		  {
			if (value.equals(sm.toString()))
				b=true;
		  }
		
		  if (sm>101)
		  {
			Integer a=sm%101;
			if (a<10)
			{
				if (value.equals("0"+a.toString()))
					b=true;
			}
			else
			{
				if (value.equals(a.toString()))
					b=true;
			}
		  }
		}
		return b;
	}
	
	/**
	 * проверяем инн, возвращаем true если правильный 
	 */
	public static boolean CheckInn(String arr)
	{
		boolean b=false;
		if  ((arr.length()==10) || (arr.length()==12))
		{
		  String value2=arr.substring(arr.length()-1, arr.length());
		  if (arr.length()==12)
		  {
			  String value1=arr.substring(arr.length()-2, arr.length()-1);
			  Integer a=CalcSm(arr,inn12_1,arr.length()-2)%11;
			  if (a==10)
				  a=0;
			  if (value1.equals(a.toString()))
				b=true;
			  if (b)
			  {
				a=CalcSm(arr,inn12_2,arr.length()-1)%11;
				if (a==10)
					  a=0;
				if (value2.equals(a.toString()))
					b=true;
				else
					b=false;
			  }
		  }
		  else
		  {
		     Integer a=CalcSm(arr,inn10,arr.length()-1)%11;
		     if (a==10)
				  a=0;
		     if (value2.equals(a.toString()))
				b=true;
		   
		  }
		}
		return b;
	}

	/**
	 * Метод проверяет ИИН на корректность
	 *
	 * @param iin ИИН
	 * @return true если корректный
	 */
	public static Boolean checkIin(String iin) {
		
		if (iin == null)
			return false;
		
		iin = iin.trim();
		
		if (iin.length() != 12) return false;
		int s = 0;
		for (int i = 0; i < 11; i++) {
			s += (i + 1) * Convertor.toInteger(iin.charAt(i));
		}
		int k = s % 11;
		if (k == 10) {
			s = 0;
			for (int i = 0; i < 11; i++) {
				int t = (i + 3) % 11;
				if (t == 0) {
					t = 11;
				}
				s += t * Convertor.toInteger(iin.charAt(i));
			}
			k = s % 11;
			return k != 10 && (k == Convertor.toInteger(iin.substring(11, 12)));
		}
		return (k == Convertor.toInteger(iin.substring(11, 12)));
	}
}
