package nomad.amenity;

import static nomad.Application.gson;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class AmenityDAO
{

	private String filename;

	public AmenityDAO()
	{
		this.filename = "amenities.json";
		this.initFile();
	}
	
	public AmenityDAO(String filename)
	{
		this.filename = filename;
		this.initFile();
	}

	private void initFile()
	{
		File f = new File(this.filename);
		if (!f.isFile())
		{
			this.saveAll(new ArrayList<Amenity>());
		}
	}
	
	private void saveAll(ArrayList<Amenity> amenities)
	{
		try (FileWriter writer = new FileWriter(this.filename))
		{
			gson.toJson(amenities, writer);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public boolean remove(String id)
	{
		ArrayList<Amenity> amenities = (ArrayList<Amenity>) this.getAll();
		for(int i = 0; i < amenities.size(); ++i)
		{
			Amenity amenity = amenities.get(i);
			if(amenity.getId().equals(id) && !amenity.getDeleted())
			{
				amenity.setDeleted(true);
				amenities.set(i, amenity);
				this.saveAll(amenities);
				return true;
			}
		}
		return false;
	}

	public boolean add(Amenity amenity)
	{
		ArrayList<Amenity> amenities = (ArrayList<Amenity>) this.getAll();
		amenity.setId(Math.random() + amenity.getName() + Math.random());
		if (amenities.stream().filter(a -> a.getId().equals(amenity.getId()) && !a.getDeleted()).findAny().orElse(null) == null)
		{
			amenity.setDeleted(false);
			amenities.add(amenity);
			this.saveAll(amenities);
			return true;
		}
		return false;
	}

	public boolean update(Amenity amenity)
	{
		ArrayList<Amenity> amenities = (ArrayList<Amenity>) this.getAll();
		for (int i = 0; i < amenities.size(); ++i)
		{
			if (amenities.get(i).getId().contentEquals(amenity.getId()))
			{
				amenities.set(i, amenity);
				this.saveAll(amenities);
				return true;
			}
		}
		return false;
	}

	public Amenity get(String id)
	{
		ArrayList<Amenity> amenities = (ArrayList<Amenity>) this.getAll();
		return amenities.stream().filter(a -> a.getId().equals(id)).findAny().orElse(null);
	}

	public Collection<Amenity> getAll()
	{
		Collection<Amenity> amenities = null;
		Type collectionType = new TypeToken<Collection<Amenity>>()
		{
		}.getType();
		try (FileReader freader = new FileReader(this.filename); JsonReader jreader = new JsonReader(freader))
		{
			amenities = gson.fromJson(jreader, collectionType);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		if(amenities != null)
		{
			amenities.removeIf(a -> a.getDeleted());
		}
		return amenities == null ? new ArrayList<Amenity>() : amenities;
	}
}
