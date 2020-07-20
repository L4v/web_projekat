package nomad.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import nomad.beans.UserHost;


public class UserHostDAO {

	private Map<String, UserHost> userHosts = new HashMap<>();
	
	public UserHostDAO() {
		
	}
	
	public UserHostDAO(String contextPath) {
		loadUserHosts(contextPath);
	}
	
	private void loadUserHosts(String contextPath) {
		//TODO
	}
	
	public UserHost find(String username, String password) {
		if (!userHosts.containsKey(username)) {
			return null;
		}
		UserHost userHost = userHosts.get(username);
		if (!userHost.getPassword().equals(password)) {
			return null;
		}
		return userHost;
	}
	
	public Collection<UserHost> findAll(){
		return userHosts.values();
	}
}
