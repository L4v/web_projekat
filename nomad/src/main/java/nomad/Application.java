package nomad;

import static nomad.Application.gson;
import static spark.Spark.after;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.options;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

import java.lang.reflect.Type;
import java.security.Key;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import nomad.utils.DateTypeAdapter;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import nomad.amenity.AmenityDAO;
import nomad.amenity.AmenityServices;
import nomad.apartment.ApartmentDAO;
import nomad.apartment.ApartmentServices;
import nomad.comment.CommentDAO;
import nomad.comment.CommentServices;
import nomad.login.LoginServices;
import nomad.registration.RegistrationServices;
import nomad.reservation.ReservationDAO;
import nomad.reservation.ReservationServices;
import nomad.user.AdminServices;
import nomad.user.GuestServices;
import nomad.user.HostServices;
import nomad.user.UserAdminDAO;
import nomad.user.UserGuestDAO;
import nomad.user.UserHostDAO;
import nomad.user.UserServices;
import nomad.utils.Filters;
import nomad.utils.Path;
import spark.Request;
import spark.Response;
import spark.Route;

public class Application
{

	public static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

	public static Gson gson;

	// TODO(Jovan): Prepraviti na logicko brisanje??
	public static UserAdminDAO adminDAO;
	public static UserGuestDAO guestDAO;
	public static UserHostDAO hostDAO;
	public static ApartmentDAO apartmentDAO;
	public static ReservationDAO reservationDAO;
	public static CommentDAO commentDAO;
	public static AmenityDAO amenityDAO;

	// TODO(Jovan): Separate into utility classes?

	public static String parseJws(Request request)
	{
		String auth = request.headers("Authorization");
		if (auth == null || auth.isEmpty() || !auth.contains("Bearer"))
		{
			return null;
		}
		
		String jws = auth.substring(auth.indexOf("Bearer") + 7);
		if(jws.equalsIgnoreCase("undefined") || auth.length() <= 7)
		{
			return null;
		}

		return jws;
	}
	
	
	public static Route serveStaticResource = (Request request, Response response) ->
	{
		response.redirect("/static/index.html");
		return response;
	};
	
	/* NOTE(Jovan):
	 * From sparkjava.com:
	 * >> Enables CORS on requests.
	 * >> This method is an initialization method and should be called once.
	 * 
	 */
	private static void enableCORS(final String origin, final String methods, final String headers) {

	    options("/*", (request, response) -> {

	        String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
	        if (accessControlRequestHeaders != null) {
	            response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
	        }

	        String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
	        if (accessControlRequestMethod != null) {
	            response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
	        }

	        return "OK";
	    });

	    before((request, response) -> {
	        response.header("Access-Control-Allow-Origin", origin);
	        response.header("Access-Control-Request-Method", methods);
	        response.header("Access-Control-Allow-Headers", headers);
	        // Note: this may or may not be necessary in your particular application
	        response.type("application/json");
	    });
	}

	public static void main(String args[])
	{
		/*GsonBuilder builder = new GsonBuilder();//.setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();//registerTypeAdapter(Date.class, new DateTypeAdapter()).create();
		builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() 
		{
			public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
			{
				return new Date(json.getAsJsonPrimitive().getAsLong());
			}
		});
		gson = builder.create();*/
		gson = new Gson();
		
		adminDAO = new UserAdminDAO("admins.json");
		guestDAO = new UserGuestDAO("guests.json");
		hostDAO = new UserHostDAO("hosts.json");
		apartmentDAO = new ApartmentDAO("apartments.json");
		reservationDAO = new ReservationDAO("reservations.json");
		commentDAO = new CommentDAO("comments.json");
		amenityDAO = new AmenityDAO("amenities.json");

		port(8080);
		staticFiles.location("/static");
		
		// NOTE(Jovan): Enabling CORS
		enableCORS("*", "*", "*");
		
		// NOTE(Jovan): For SPA
		get("/", serveStaticResource);



		post(Path.Rest.REG_GUEST, RegistrationServices.registerGuest);
		post(Path.Rest.REG_HOST, RegistrationServices.registerHost);
		post(Path.Rest.LOGIN, LoginServices.login);
		post(Path.Rest.PERSONAL_DATA, UserServices.personalData);
		post(Path.Rest.HOST_ADD_APARTMENT, ApartmentServices.hostAddApartment);
		post(Path.Rest.CREATE_RESERVATION, ReservationServices.createReservation);
		post(Path.Rest.GUEST_CANCEL_RESERVATION, ReservationServices.guestCancelReservation);
		post(Path.Rest.GUEST_ADD_COMMENT, CommentServices.addComment);
		post(Path.Rest.ADD_AMENITY, AmenityServices.addAmenity);
		post(Path.Rest.REMOVE_AMENITY, AmenityServices.removeAmenity);
		post(Path.Rest.UPDATE_AMENITY, AmenityServices.updateAmenity);
		post(Path.Rest.ADMIN_REMOVE_APARTMENTS, ApartmentServices.adminDeleteApartments);
		post(Path.Rest.HOST_ENABLE_APARTMENT, HostServices.enableApartment);
		
		get("rest/test", LoginServices.verifyLogin);
		get(Path.Rest.GET_USER, UserServices.getUser);
		get(Path.Rest.GUEST_GET_USERNAME, GuestServices.getUsername);
		get(Path.Rest.CHECK_IF_ADMIN, AdminServices.checkIfAdmin);
		get(Path.Rest.CHECK_IF_HOST, HostServices.checkIfHost);
		get(Path.Rest.CHECK_IF_GUEST, GuestServices.checkIfGuest);
		get(Path.Rest.GET_AMENITIES, AmenityServices.getAmenities);
		get(Path.Rest.ADMIN_ALL_USERS, AdminServices.getAllUsers);
		get(Path.Rest.ADMIN_ALL_APARTMENTS, AdminServices.allApartments);
		get(Path.Rest.ADMIN_ALL_AMENITIES, AdminServices.allAmenities);
		get(Path.Rest.ADMIN_ALL_RESERVATIONS, ReservationServices.adminViewReservations);
		get(Path.Rest.ADMIN_ALL_COMMENTS, CommentServices.adminViewComments);
		get(Path.Rest.HOST_ALL_APARTMENTS, HostServices.allApartments);
		get(Path.Rest.HOST_ALL_RESERVATIONS, ReservationServices.hostViewReservations);
		get(Path.Rest.HOST_ALL_GUESTS, HostServices.getMyGuests);
		get(Path.Rest.HOST_ALL_COMMENTS, CommentServices.hostViewComments);
		get(Path.Rest.GUEST_RESERVED_APARTMENTS, GuestServices.reservedApartments);
		get(Path.Rest.GUEST_ALL_APARTMENTS, GuestServices.allApartments);
		get(Path.Rest.GUEST_ALL_RESERVATIONS, ReservationServices.guestViewReservations);
		get(Path.Rest.CHECK_IF_HAS_RESERVATION, ReservationServices.checkIfHasReservation);
		
		// TODO(Jovan): Catch all for vue router, not necessary?
		/*before((request, response) ->
		{
			try(InputStream stream = Application.class.getResourceAsStream("/static/index.html")){
				halt(200, IOUtils.toString(stream));
			} catch(IOException e) {
				e.printStackTrace();
			}
		});*/
		get("*", (request, response) ->
		{
			response.redirect("/");
			return response;
		});
		// NOTE(Jovan): Gzip compression
		after("*", Filters.addGzipHeader);

	}

}
