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
		boolean success = guests.removeIf(g -> g.getUsername().equals(username));
		if (success == true)
		{
			this.saveAll(guests);
		}
		return success;
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
		if (guests.stream().filter(g -> g.getUsername().equals(guest.getUsername())).findAny().orElse(null) == null)
		{
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
		return guests == null ? new ArrayList<UserGuest>() : guests;
	}
}