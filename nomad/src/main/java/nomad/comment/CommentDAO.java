package nomad.comment;

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


public class CommentDAO
{

	private String filename;

	public CommentDAO()
	{
		this.filename = "comments.json";
		this.initFile();
	}

	public CommentDAO(String filename)
	{
		this.filename = filename;
		this.initFile();
	}

	private void initFile()
	{
		File f = new File(this.filename);
		if (!f.isFile())
		{
			this.saveAll(new ArrayList<Comment>());
		}
	}

	private void saveAll(ArrayList<Comment> comments)
	{
		try(FileWriter writer = new FileWriter(this.filename))
		{
			gson.toJson(comments, writer);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public boolean add(Comment comment)
	{
		ArrayList<Comment> comments = (ArrayList<Comment>) this.getAll();
		comment.setId(comment.getApartmentId() + comment.getGuestId() + comment.getRating() + Math.random());
		if (comments.stream().filter(a -> a.getId().equals(comment.getId()) && !a.getDeleted()).findAny().orElse(null) == null)
		{
			comment.setDeleted(false);
			comments.add(comment);
			this.saveAll(comments);
			return true;
		}
		return false;
	}
	
	public boolean remove(String id)
	{
		ArrayList<Comment> comments = (ArrayList<Comment>) this.getAll();
		for(int i = 0; i < comments.size(); ++i)
		{
			Comment comment = comments.get(i);
			if(comment.getId().equals(id) && !comment.getDeleted())
			{
				comment.setDeleted(true);
				comments.set(i, comment);
				this.saveAll(comments);
				return true;
			}
		}
		return false;
	}

	public boolean update(Comment comment)
	{
		ArrayList<Comment> comments = (ArrayList<Comment>)this.getAll();
		for(int i = 0; i < comments.size(); ++i)
		{
			if(comments.get(i).getId().contentEquals(comment.getId()))
			{
				comments.set(i, comment);
				this.saveAll(comments);
				return true;
			}
		}
		return false;
	}
	
	public Comment get(String id)
	{
		ArrayList<Comment> comments = (ArrayList<Comment>) this.getAll();
		return comments.stream().filter(c -> c.getId().equals(id)).findAny().orElse(null);
	}

	public Collection<Comment> getAll()
	{
		Collection<Comment> comments = null;
		Type collectionType = new TypeToken<Collection<Comment>>() {}.getType();
		try(FileReader freader = new FileReader(this.filename);
			JsonReader jreader = new JsonReader(freader))
		{
			comments = gson.fromJson(jreader, collectionType);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		if(comments != null)
		{
			comments.removeIf(a -> a.getDeleted());
		}
		return comments == null ? new ArrayList<Comment>() : comments;
	}
}
