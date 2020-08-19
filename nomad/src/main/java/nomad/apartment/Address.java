package nomad.apartment;

import java.io.Serializable;

public class Address implements Serializable
{
	private static final long serialVersionUID = 5560065015617074755L;
	private String street;
	private String streetNumber;
	private String area;
	private String areaCode;

	public Address(String street, String streetNumber, String area, String areaCode)
	{
		this.street = street;
		this.streetNumber = streetNumber;
		this.area = area;
		this.areaCode = areaCode;
	}

	public String getStreet()
	{
		return street;
	}

	public void setStreet(String street)
	{
		this.street = street;
	}

	public String getStreetNumber()
	{
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber)
	{
		this.streetNumber = streetNumber;
	}

	public String getArea()
	{
		return area;
	}

	public void setArea(String area)
	{
		this.area = area;
	}

	public String getAreaCode()
	{
		return areaCode;
	}

	public void setAreaCode(String areaCode)
	{
		this.areaCode = areaCode;
	}

}
