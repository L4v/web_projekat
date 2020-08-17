package nomad.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import nomad.beans.Amenity;

public class AmenityDAO
{

	private static Map<String, Amenity> amenities = new HashMap<>();

	public AmenityDAO()
	{

	}

	public AmenityDAO(String contextPath)
	{
		loadAmenities(contextPath);
	}

	private void loadAmenities(String contextPath)
	{
		// TODO
	}

	public Amenity find(String id)
	{
		return amenities.containsKey(id) ? amenities.get(id) : null;
	}

	public Collection<Amenity> findAll()
	{
		return amenities.values();
	}
}
