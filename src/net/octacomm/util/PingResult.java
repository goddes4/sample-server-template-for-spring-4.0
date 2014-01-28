package net.octacomm.util;

/**
 * Ping 결과를 관리하는 클래스
 * 
 * @author hslim
 *
 */
public class PingResult 
{
	/** packet loss */
	private int loss;
	
	/** minium round trip times in milli-seconds */
	private int min = -1;
	
	/** average round trip times in milli-seconds */
	private int avg = -1;
	
	/** maximum round trip times in milli-seconds */
	private int max = -1;
	
	public int getLoss() 
	{
		return loss;
	}

	public void setLoss(int loss) 
	{
		this.loss = loss;
	}

	public int getMin() 
	{
		return min;
	}

	public void setMin(int min) 
	{
		this.min = min;
	}

	public int getAvg() 
	{
		return avg;
	}

	public void setAvg(int avg) 
	{
		this.avg = avg;
	}

	public int getMax() 
	{
		return max;
	}

	public void setMax(int max) 
	{
		this.max = max;
	}

	@Override
	public String toString() 
	{
		return "PingResult [loss=" + loss + ", min=" + min + ", avg=" + avg
				+ ", max=" + max + "]";
	}
}
