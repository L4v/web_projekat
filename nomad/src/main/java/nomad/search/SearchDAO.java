package nomad.search;

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

public class SearchDAO
{
	private String filename;
	
	public SearchDAO()
	{
		this.filename = "search_results.json";
		this.initFile();
	}
	
	public SearchDAO(String filename)
	{
		this.filename = filename;
		this.initFile();
	}
	
	private void initFile()
	{
		File f = new File(this.filename);
		if(!f.isFile())
		{
			this.save(new ArrayList<String>());
		}
	}
	
	public void save(ArrayList<String> results)
	{
		try(FileWriter writer = new FileWriter(this.filename))
		{
			gson.toJson(results, writer);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public Collection<String> get()
	{
		Collection<String> results = null;
		Type collectionType = new TypeToken<Collection<String>>() {}.getType();
		try(FileReader freader = new FileReader(this.filename);
			JsonReader jreader = new JsonReader(freader))
		{
			results = gson.fromJson(jreader, collectionType);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		return results == null ? new ArrayList<String>() : results;
	}
	
}
