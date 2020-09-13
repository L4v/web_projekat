package nomad.utils;

import java.util.Date;

public class DateRange
{
	private Date start;
	private Date end;
	
	public DateRange(Date start, Date end)
	{
		this.start = start;
		this.end = end;
	}
	
	public Date getStart()
	{
		return this.start;
	}
	
	public void setStart(Date start)
	{
		this.start = start;
	}
	
	public Date getEnd()
	{
		return this.end;
	}
	
	public void setEnd(Date end)
	{
		this.end = end;
	}
}
