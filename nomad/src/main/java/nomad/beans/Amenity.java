package nomad.beans;

import java.io.Serializable;

public class Amenity implements Serializable
{
	private static final long serialVersionUID = -1084410177533522704L;
	private String id;
	private String name;

	public Amenity(String id, String name)
	{
		this.id = id;
		this.name = name;
	}

	public String getId()
	{
		return this.id;
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
