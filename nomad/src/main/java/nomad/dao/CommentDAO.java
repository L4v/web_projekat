package nomad.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import nomad.beans.Comment;

public class CommentDAO {

	private static Map<String, Comment> comments = new HashMap<>();
	
	public CommentDAO() {
		
	}
	
	public CommentDAO(String contextPath) {
		loadComments(contextPath);
	}
	
	private void loadComments(String contextPath) {
		//TODO
	}
	
	public Comment find(String id) {
		return comments.containsKey(id) ? comments.get(id) : null;
	}
	
	public Collection<Comment> findAll(){
		return comments.values();
	}
}
