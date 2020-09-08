package nomad.utils;

// NOTE(Jovan): Utility class for paths
public class Path
{

	// NOTE(Jovan): Web page redirections
	public static class Web
	{
	}

	// NOTE(Jovan): REST paths
	public static class Rest
	{
		public static final String ADMIN_ALL_USERS = "/rest/admin_all_users";
		public static final String ADMIN_ALL_APARTMENTS = "/rest/admin_all_apartments";
		public static final String ADMIN_ALL_RESERVATIONS = "/rest/admin_view_reservations";
		public static final String ADMIN_ALL_COMMENTS = "/rest/admin_view_comments";
		public static final String ADMIN_ALL_AMENITIES = "/rest/admin_all_amenities";
		public static final String ADMIN_REMOVE_APARTMENTS = "/rest/admin_remove_apartments";
		public static final String CHECK_IF_ADMIN = "/rest/check_if_admin";
		
		public static final String HOST_ALL_APARTMENTS = "/rest/host_all_apartments";
		public static final String HOST_ALL_GUESTS = "/rest/host_all_guests";
		public static final String HOST_ALL_RESERVATIONS = "/rest/host_view_reservations";
		public static final String HOST_ALL_COMMENTS = "/rest/host_view_comments";
		public static final String HOST_ADD_APARTMENT = "/rest/host_add_apartment";
		public static final String HOST_SEARCH_GUESTS = "/rest/host_search_guests";
		
		public static final String GUEST_ALL_APARTMENTS = "/rest/guest_all_guests";
		public static final String GUEST_ALL_RESERVATIONS = "/rest/guest_view_reservations";
		public static final String GUEST_ADD_COMMENT = "/rest/guest_add_comment";
		public static final String GUEST_GET_USERNAME = "/rest/guest_get_username";
		public static final String GUEST_CANCEL_RESERVATION = "/rest/guest_cancel_reservation";
		
		public static final String REG_GUEST = "/rest/reg_guest";
		public static final String REG_HOST = "/rest/reg_host";
		public static final String LOGIN = "/rest/login";
		public static final String PERSONAL_DATA = "/rest/personal_data";
		public static final String GET_USER = "/rest/get_user";
		
		public static final String CREATE_RESERVATION = "/rest/create_reservation";
		public static final String ADD_AMENITY = "/rest/add_amenity";
		public static final String REMOVE_AMENITY = "/rest/remove_amenity";
		public static final String UPDATE_AMENITY = "/rest/update_amenity";
	}

}
