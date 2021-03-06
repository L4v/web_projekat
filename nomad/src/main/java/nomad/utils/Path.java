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
		public static final String HOST_ALL_RESERVATIONS = "/rest/host_all_reservations";
		public static final String HOST_ALL_COMMENTS = "/rest/host_all_comments";
		public static final String HOST_ADD_APARTMENT = "/rest/host_add_apartment";
		public static final String HOST_ENABLE_APARTMENT = "/rest/host_enable_apartment";
		public static final String HOST_REJECT_RESERVATION = "/rest/host_reject_reservation";
		public static final String HOST_ACCEPT_RESERVATION = "/rest/host_accept_reservation";
		public static final String HOST_FINISH_RESERVATION = "/rest/host_finish_reservation";
		public static final String HOST_UPLOAD_IMAGE = "/rest/host_upload_image";
		public static final String HOST_UPDATE_APARTMENT = "/rest/host_update_apartment";
		public static final String CHECK_IF_HOST = "/rest/check_if_host";
		
		public static final String GUEST_ALL_APARTMENTS = "/rest/guest_all_apartments";
		public static final String GUEST_RESERVED_APARTMENTS = "/rest/guest_reserved_apartments";
		public static final String GUEST_ALL_RESERVATIONS = "/rest/guest_view_reservations";
		public static final String GUEST_ADD_COMMENT = "/rest/guest_add_comment";
		public static final String GUEST_GET_USERNAME = "/rest/guest_get_username";
		public static final String GUEST_CANCEL_RESERVATION = "/rest/guest_cancel_reservation";
		public static final String CHECK_IF_GUEST = "/rest/check_if_guest";
		public static final String GET_GUESTS = "/rest/get_guests";
		public static final String GET_APARTMENTS = "/rest/get_apartments";
		
		public static final String REG_GUEST = "/rest/reg_guest";
		public static final String REG_HOST = "/rest/reg_host";
		public static final String LOGIN = "/rest/login";
		public static final String PERSONAL_DATA = "/rest/personal_data";
		public static final String GET_USER = "/rest/get_user";
		
		public static final String CREATE_RESERVATION = "/rest/create_reservation";
		public static final String ADD_AMENITY = "/rest/add_amenity";
		public static final String REMOVE_AMENITY = "/rest/remove_amenity";
		public static final String UPDATE_AMENITY = "/rest/update_amenity";
		public static final String GET_AMENITIES = "/rest/get_amenities";
		public static final String CHECK_IF_HAS_RESERVATION = "/rest/check_if_has_reservation";
		public static final String GET_ALL_APARTMENTS = "/rest/get_all_apartments";
		public static final String SAVE_SEARCH_RESULTS = "/rest/save_search_results";
		public static final String GET_SEARCH_RESULTS = "/rest/get_search_results";
		public static final String SEARCH_APARTMENT = "/rest/search_apartment";
	}

}
