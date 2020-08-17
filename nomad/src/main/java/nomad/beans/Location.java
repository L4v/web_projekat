package nomad.beans;

import java.io.Serializable;

public class Location implements Serializable
{
	private static final long serialVersionUID = -3339741894270639805L;
	private String lat;
	private String lon;
	private Address address;

	public Location(String lat, String lon, Address address)
	{
		this.lat = lat;
		this.lon = lon;
		this.address = address;
	}

	public String getLat()
	{
		return this.lat;
	}

	public void setLat(String lat)
	{
		this.lat = lat;
	}

	public String getLon()
	{
		return this.lon;
	}

	public void setLon(String lon)
	{
		this.lon = lon;
	}

	public Address getAddress()
	{
		return this.address;
	}

	public void setAddress(Address address)
	{
		this.address = address;
	}
}
