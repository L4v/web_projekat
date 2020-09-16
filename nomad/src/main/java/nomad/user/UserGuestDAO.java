package nomad.user;

import static nomad.Application.gson;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class UserGuestDAO
{

	private String filename;

	public UserGuestDAO()
	{
		this.filename = "guests.json";
		this.initFile();
	}

	public UserGuestDAO(String contextPath)
	{
		this.filename = contextPath;
		this.initFile();
	}

	// NOTE(Jovan): Kreiramo prazan JSON u slucaju
	// da ne postoji
	private void initFile()
	{
		File f = new File(this.filename);
		if (!f.isFile())
		{
			this.saveAll(new ArrayList<UserGuest>());
		}
	}

	private void saveAll(Collection<UserGuest> guests)
	{
		try (FileWriter writer = new FileWriter(this.filename))
		{
			gson.toJson(guests, writer);
		} catch (JsonIOException | IOException e)
		{
			e.printStackTrace();
		}
	}

	public boolean remove(String username)
	{
		ArrayList<UserGuest> guests = (ArrayList<UserGuest>) this.getAll();
		for(int i = 0; i < guests.size(); ++i)
		{
			UserGuest guest = guests.get(i);
			if(guest.getUsername().equals(username) && !guest.getDeleted())
			{
				guest.setDeleted(true);
				guests.set(i, guest);
				this.saveAll(guests);
				return true;
			}
		}
		return false;
	}

	public boolean update(UserGuest userGuest)
	{
		ArrayList<UserGuest> guests = (ArrayList<UserGuest>) this.getAll();

		for (int i = 0; i < guests.size(); ++i)
		{
			if (guests.get(i).getUsername().equals(userGuest.getUsername()))
			{
				guests.set(i, userGuest);
				this.saveAll(guests);
				return true;
			}
		}

		return false;

	}

	public boolean add(UserGuest guest)
	{
		ArrayList<UserGuest> guests = (ArrayList<UserGuest>) this.getAll();
		if (guests.stream().filter(g -> g.getUsername().equals(guest.getUsername()) && !g.getDeleted()).findAny().orElse(null) == null)
		{
			guest.setDeleted(false);
			guests.add(guest);
			this.saveAll(guests);
			return true;
		}

		return false;
	}

	public UserGuest get(String username)
	{
		ArrayList<UserGuest> guests = (ArrayList<UserGuest>) this.getAll();
		return guests.stream().filter(g -> g.getUsername().equals(username)).findFirst().orElse(null);
	}
	
	public Collection<UserGuest> getByIds(Collection<String> ids)
	{
		ArrayList<UserGuest> guests = (ArrayList<UserGuest>) this.getAll();
		ArrayList<UserGuest> byIds = new ArrayList<UserGuest>();
		for(String id : ids)
		{
			for(UserGuest guest: guests)
			{
				if(guest.getUsername().equals(id))
				{
					byIds.add(guest);
				}
			}
		}
		return byIds;
	}

	public Collection<UserGuest> getAll()
	{
		Collection<UserGuest> guests = null;
		Type collectionType = new TypeToken<Collection<UserGuest>>()
		{
		}.getType();
		try (FileReader freader = new FileReader(this.filename); JsonReader jreader = new JsonReader(freader))
		{
			guests = gson.fromJson(jreader, collectionType);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		if(guests != null)
		{
			guests.removeIf(a -> a.getDeleted());
		}
		return guests == null ? new ArrayList<UserGuest>() : guests;
	}
}
