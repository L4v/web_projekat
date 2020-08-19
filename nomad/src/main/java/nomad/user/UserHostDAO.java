package nomad.user;

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

public class UserHostDAO
{

	private String filename;

	public UserHostDAO()
	{
		this.filename = "hosts.json";
		this.initFile();
	}

	public UserHostDAO(String contextPath)
	{
		this.filename = contextPath;
		this.initFile();
	}

	private void initFile()
	{
		File f = new File(this.filename);
		if (!f.isFile())
		{
			this.saveAll(new ArrayList<UserHost>());
		}
	}

	private void saveAll(Collection<UserHost> hosts)
	{
		try (FileWriter writer = new FileWriter(this.filename))
		{
			gson.toJson(hosts, writer);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public boolean remove(String username)
	{
		ArrayList<UserHost> hosts = (ArrayList<UserHost>) this.getAll();
		boolean success = hosts.removeIf(h -> h.getUsername().equals(username));
		if (success == true)
		{
			this.saveAll(hosts);
		}
		return success;
	}

	public boolean add(UserHost host)
	{
		ArrayList<UserHost> hosts = (ArrayList<UserHost>) this.getAll();
		if (hosts.stream().filter(h -> h.getUsername().equals(host.getUsername())).findAny().orElse(null) == null)
		{
			hosts.add(host);
			this.saveAll(hosts);
			return true;
		}
		return false;
	}

	public boolean update(UserHost userHost)
	{
		ArrayList<UserHost> hosts = (ArrayList<UserHost>) this.getAll();
		for (int i = 0; i < hosts.size(); ++i)
		{
			if (hosts.get(i).getUsername().equals(userHost.getUsername()))
			{
				hosts.set(i, userHost);
				this.saveAll(hosts);
				return true;
			}
		}
		return false;
	}

	public UserHost get(String username)
	{
		ArrayList<UserHost> hosts = (ArrayList<UserHost>) this.getAll();
		return hosts.stream().filter(h -> h.getUsername().equals(username)).findAny().orElse(null);
	}

	public Collection<UserHost> getAll()
	{
		Collection<UserHost> hosts = null;
		Type collectionType = new TypeToken<Collection<UserHost>>()
		{
		}.getType();
		try (FileReader freader = new FileReader(this.filename); JsonReader jreader = new JsonReader(freader))
		{
			hosts = gson.fromJson(jreader, collectionType);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return hosts == null ? new ArrayList<UserHost>() : hosts;
	}
}
