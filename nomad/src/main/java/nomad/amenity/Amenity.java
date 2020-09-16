package nomad.amenity;

import java.io.Serializable;

public class Amenity implements Serializable
{
	private static final long serialVersionUID = -1084410177533522704L;
	private String id;
	private String name;
	private boolean deleted;

	public Amenity(String id, String name)
	{
		this.id = id;
		this.name = name;
		deleted = false;
	}
	
	public boolean getDeleted()
	{
		return this.deleted;
	}
	
	public void setDeleted(boolean deleted)
	{
		this.deleted = deleted;
	}

	public String getId()
	{
		return this.id;
	}
	
	public void setId(String id)
	{
		this.id = id;
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
