package nomad.dao;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import nomad.beans.UserAdmin;

public class UserAdminDAO {

	private String filename;
	
	public UserAdminDAO() {
		this.filename = "admins.json";
		this.initFile();
	}
	
	public UserAdminDAO(String contextPath) {
		this.filename = contextPath;
		this.initFile();
	}
	
	private void initFile()
	{
		File f = new File(this.filename);
		if(!f.isFile())
		{
			this.saveAll(new ArrayList<UserAdmin>());
		}
	}
	
	private void saveAll(Collection<UserAdmin> admins)
	{
		Gson gson = new Gson();
		try(FileWriter writer = new FileWriter(this.filename))
		{
			gson.toJson(admins, writer);
		}
		catch(JsonIOException | IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public boolean add(UserAdmin newAdmin)
	{
		ArrayList<UserAdmin> admins = (ArrayList<UserAdmin>)this.getAll();
		UserAdmin admin = admins.stream().filter(a -> a.getUsername().equals(newAdmin.getUsername())).findAny().orElse(null);
		if(admin == null)
		{
			admins.add(newAdmin);
			this.saveAll(admins);
			return true;
		}
		return false;
	}
	
	
	public boolean update(UserAdmin newAdmin) {
		ArrayList<UserAdmin> admins = (ArrayList<UserAdmin>)this.getAll();
		for(int i = 0;
				i < admins.size();
				++i)
		{
			if(admins.get(i).getUsername().equals(newAdmin.getUsername()))
			{
				admins.set(i, newAdmin);
				this.saveAll(admins);
				return true;
			}
		}
		return false;
	}
	
	public boolean remove(String username)
	{
		ArrayList<UserAdmin> admins = (ArrayList<UserAdmin>)this.getAll();
		boolean success = admins.removeIf(a -> a.getUsername().equals(username));
		if(success == true)
		{
			this.saveAll(admins);
		}
		return success;
	}
	
	public UserAdmin get(String username) {
		ArrayList<UserAdmin> admins = (ArrayList<UserAdmin>)this.getAll();
		return admins.stream().filter(a -> a.getUsername().equals(username)).findAny().orElse(null);
	}
	
	public Collection<UserAdmin> getAll(){
		ArrayList<UserAdmin> admins = null;
		Type collectionType = new TypeToken<Collection<UserAdmin>>(){}.getType();
		try(FileReader freader = new FileReader(this.filename);
			JsonReader jreader = new JsonReader(freader))
		{
			Gson gson = new Gson();
			admins = gson.fromJson(jreader, collectionType);
		}
		catch(IOException e)
		{
			e.printStackTrace();
			
		}
		return admins == null ? new ArrayList<UserAdmin>() : admins;
	}
}
