package nomad.utils;


// NOTE(Jovan): Utility class for paths
public class Path {

	// NOTE(Jovan): Web page redirections
	public static class Web
	{
	}
	
	// NOTE(Jovan): REST paths
	public static class Rest
	{
		public static final String ADMIN_ALL_USERS = "/rest/admin_all_users";
		
		public static final String HOST_ALL_APARTMENTS = "/rest/host_all_apartments";
		public static final String HOST_ADD_APARTMENT = "/rest/host_add_apartment";
		
		public static final String REG_GUEST = "/rest/reg_guest";
		public static final String LOGIN = "/rest/login";
		public static final String PERSONAL_DATA = "/rest/personal_data";
	}
	
}
