package nomad.apartment;

import static nomad.Application.gson;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;

import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.lang.reflect.Type;

public class ApartmentDAO
{

	private String filename;

	public ApartmentDAO()
	{
		this.filename = "apartments.json";
		this.initFile();
	}

	public ApartmentDAO(String filename)
	{
		this.filename = filename;
		this.initFile();
	}

	private void initFile()
	{
		File f = new File(this.filename);
		if (!f.isFile())
		{
			this.saveAll(new ArrayList<Apartment>());
		}
	}
	
	private String getNextId(int curr)
	{
		//ArrayList<Apartment> apartments = (ArrayList<Apartment>) this.getAll();
		// TODO(Jovan): Add prefix?
		/*if(apartments.size() == 0)
		{
			return "0";
		}
		try
		{
			int maxId = Integer.parseInt(apartments.get(0).getId());
			for(Apartment a : apartments)
			{
				int currId = Integer.parseInt(a.getId());
				if(currId > maxId)
				{
					maxId = currId;
				}
			}
			return String.valueOf(maxId + 1);
		}
		catch(NumberFormatException e)
		{
			e.printStackTrace();
			return String.valueOf(-1);
		}*/
		return String.valueOf(curr++);
	}

	private void saveAll(ArrayList<Apartment> apartments)
	{
		try (FileWriter writer = new FileWriter(this.filename))
		{
			gson.toJson(apartments, writer);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public boolean remove(String id)
	{
		ArrayList<Apartment> apartments = (ArrayList<Apartment>) this.getAll();
		for(int i = 0; i < apartments.size(); ++i)
		{
			Apartment apartment = apartments.get(i);
			if(apartment.getId().equals(id) && !apartment.getDeleted())
			{
				apartment.setDeleted(true);
				apartments.set(i, apartment);
				this.saveAll(apartments);
				return true;
			}
		}
		return false;
	}

	public boolean add(Apartment apartment)
	{
		ArrayList<Apartment> apartments = (ArrayList<Apartment>) this.getAll();
		apartment.setId(apartment.getLocation().getLon() + apartment.getLocation().getLat());
		if (apartments.stream().filter(a -> a.getId().equals(apartment.getId()) && !a.getDeleted()).findAny().orElse(null) == null)
		{
			apartment.setDeleted(false);
			apartments.add(apartment);
			this.saveAll(apartments);
			return true;
		}
		return false;
		
	}

	public boolean update(Apartment apartment)
	{
		ArrayList<Apartment> apartments = (ArrayList<Apartment>) this.getAll();
		for (int i = 0; i < apartments.size(); ++i)
		{
			if (apartments.get(i).getId().contentEquals(apartment.getId()))
			{
				apartments.set(i, apartment);
				this.saveAll(apartments);
				return true;
			}
		}
		return false;
	}

	public Apartment get(String id)
	{
		ArrayList<Apartment> apartments = (ArrayList<Apartment>) this.getAll();
		return apartments.stream().filter(a -> a.getId().equals(id)).findAny().orElse(null);
	}

	public Collection<Apartment> getByIds(Collection<String> ids)
	{
		ArrayList<Apartment> apartments = (ArrayList<Apartment>) this.getAll();
		
		ArrayList<Apartment> byIds = new ArrayList<Apartment>();
		for(String id : ids)
		{
			for(Apartment a : apartments)
			{
				if(a.getId().equals(id))
				{
					byIds.add(a);
				}
			}
		}
		return byIds;
	}
	
	public Collection<Apartment> getHostsApartments(String username)
	{
		ArrayList<Apartment> apartments = (ArrayList<Apartment>) this.getAll();
		
		ArrayList<Apartment> hostsApartments = new ArrayList<Apartment>();
		for(Apartment apartment : apartments)
		{
			if(apartment.getHostId().equals(username))
			{
				apartments.add(apartment);
			}
		}
		return hostsApartments;
	}
	
	public Collection<Apartment> getActive()
	{
		ArrayList<Apartment> apartments = (ArrayList<Apartment>) this.getAll();
		ArrayList<Apartment> active = new ArrayList<Apartment>();
		for(Apartment a : apartments)
		{
			if(a.getStatus() == ApartmentStatus.ACTIVE)
			{
				active.add(a);
			}
		}
		return active;
	}

	public Collection<Apartment> getAll()
	{
		Collection<Apartment> apartments = null;
		Type collectionType = new TypeToken<Collection<Apartment>>()
		{
		}.getType();
		try (FileReader freader = new FileReader(this.filename); JsonReader jreader = new JsonReader(freader))
		{
			apartments = gson.fromJson(jreader, collectionType);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		if(apartments != null)
		{
			apartments.removeIf(a -> a.getDeleted());
		}
		return apartments == null ? new ArrayList<Apartment>() : apartments;
	}
	
	public void removeAll() {
		ArrayList<Apartment> apartments = (ArrayList<Apartment>) this.getAll();
		for(Apartment apartment : apartments)
		{
			apartment.setDeleted(true);
		}
		this.saveAll(apartments);
	}

}
