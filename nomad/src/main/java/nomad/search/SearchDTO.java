package nomad.search;

public class SearchDTO
{
	private long fromDate;
	private long toDate;
	private String area;
	private double fromPrice;
	private double toPrice;
	private int fromRoom;
	private int toRoom;
	private int noGuests;
	
	public SearchDTO(long fromDate, long toDate, String area, double fromPrice, double toPrice, int fromRoom, int toRoom, int noGuests)
	{
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.area = area;
		this.fromPrice = fromPrice;
		this.toPrice = toPrice;
		this.fromRoom = fromRoom;
		this.toRoom = toRoom;
		this.noGuests = noGuests;
	}

	public long getFromDate()
	{
		return fromDate;
	}

	public void setFromDate(long fromDate)
	{
		this.fromDate = fromDate;
	}

	public long getToDate()
	{
		return toDate;
	}

	public void setToDate(long toDate)
	{
		this.toDate = toDate;
	}

	public String getArea()
	{
		return area;
	}

	public void setArea(String area)
	{
		this.area = area;
	}

	public double getFromPrice()
	{
		return fromPrice;
	}

	public void setFromPrice(double fromPrice)
	{
		this.fromPrice = fromPrice;
	}

	public double getToPrice()
	{
		return toPrice;
	}

	public void setToPrice(double toPrice)
	{
		this.toPrice = toPrice;
	}

	public int getFromRoom()
	{
		return fromRoom;
	}

	public void setFromRoom(int fromRoom)
	{
		this.fromRoom = fromRoom;
	}

	public int getToRoom()
	{
		return toRoom;
	}

	public void setToRoom(int toRoom)
	{
		this.toRoom = toRoom;
	}

	public int getNoGuests()
	{
		return noGuests;
	}

	public void setNoGuests(int noGuests)
	{
		this.noGuests = noGuests;
	}
	
}
