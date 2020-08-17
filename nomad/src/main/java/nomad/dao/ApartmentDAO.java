package nomad.dao;

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

import nomad.beans.Apartment;

public class ApartmentDAO {

	private String filename;
	
	public ApartmentDAO() {
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
		if(!f.isFile())
		{
			this.saveAll(new ArrayList<Apartment>());
		}
	}
	
	private void saveAll(ArrayList<Apartment> apartments)
	{
		try(FileWriter writer = new FileWriter(this.filename))
		{
			gson.toJson(apartments, writer);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public boolean remove(String id)
	{
		ArrayList<Apartment> apartments = (ArrayList<Apartment>)this.getAll();
		boolean success = apartments.removeIf(a -> a.getId().equals(id));
		if(success == true)
		{
			this.saveAll(apartments);
		}
		return success;
	}
	
	public boolean add(Apartment apartment)
	{
		ArrayList<Apartment> apartments = (ArrayList<Apartment>)this.getAll();
		if(apartments.stream().filter(a -> a.getId().equals(apartment.getId())).findAny().orElse(null) == null)
		{
			apartments.add(apartment);
			this.saveAll(apartments);
			return true;
		}
		return false;
	}
	
	public boolean update(Apartment apartment)
	{
		ArrayList<Apartment> apartments = (ArrayList<Apartment>)this.getAll();
		for(int i = 0;
				i < apartments.size();
				++i)
		{
			if(apartments.get(i).getId().contentEquals(apartment.getId()))
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
		ArrayList<Apartment> apartments = (ArrayList<Apartment>)this.getAll();
		return apartments.stream().filter(a -> a.getId().equals(id)).findAny().orElse(null);
	}
	
	public Collection<Apartment> getAll()
	{
		Collection<Apartment> apartments = null;
		Type collectionType = new TypeToken<Collection<Apartment>>() {}.getType();
		try(FileReader freader = new FileReader(this.filename);
			JsonReader jreader = new JsonReader(freader))
		{
			apartments = gson.fromJson(jreader, collectionType);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		return apartments == null ? new ArrayList<Apartment>() : apartments;
	}

	
}
