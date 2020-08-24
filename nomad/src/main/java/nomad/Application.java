package nomad;

import static spark.Spark.*;

import java.security.Key;

import com.google.gson.Gson;

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

		return auth.length() <= 7 ? null : auth.substring(auth.indexOf("Bearer") + 7);
	}
	
	public static Response invalidResponse(String msg, Response response)
	{
		response.body(msg);
		response.status(404);
		return response;
	}

	public static void main(String args[])
	{
		gson = new Gson();

		adminDAO = new UserAdminDAO("admins.json");
		guestDAO = new UserGuestDAO("guests.json");
		hostDAO = new UserHostDAO("hosts.json");
		apartmentDAO = new ApartmentDAO("apartments.json");
		reservationDAO = new ReservationDAO("reservations.json");
		commentDAO = new CommentDAO("comments.json");
		amenityDAO = new AmenityDAO("amenity.json");

		port(8080);
		staticFiles.location("/static");

		post(Path.Rest.REG_GUEST, RegistrationServices.registerGuest);
		post(Path.Rest.LOGIN, LoginServices.login);
		post(Path.Rest.PERSONAL_DATA, UserServices.personalData);
		post(Path.Rest.HOST_ADD_APARTMENT, ApartmentServices.hostAddApartment);
		post(Path.Rest.CREATE_RESERVATION, ReservationServices.createReservation);
		post(Path.Rest.GUEST_ADD_COMMENT, CommentServices.addComment);
		post(Path.Rest.UPDATE_AMENITY, AmenityServices.updateAmenity);
		
		get("rest/test", LoginServices.test);
		get("rest/getUser", UserServices.getUser);
		get(Path.Rest.ADMIN_ALL_USERS, AdminServices.getAllUsers);
		get(Path.Rest.ADMIN_ALL_APARTMENTS, AdminServices.allApartments);
		get(Path.Rest.ADMIN_ALL_RESERVATIONS, ReservationServices.adminViewReservations);
		get(Path.Rest.ADMIN_ALL_COMMENTS, CommentServices.adminViewComments);
		get(Path.Rest.HOST_ALL_APARTMENTS, HostServices.allApartments);
		get(Path.Rest.HOST_ALL_RESERVATIONS, ReservationServices.hostViewReservations);
		get(Path.Rest.HOST_ALL_GUESTS, HostServices.getMyGuests);
		get(Path.Rest.HOST_ALL_COMMENTS, CommentServices.hostViewComments);
		get(Path.Rest.GUEST_ALL_APARTMENTS, GuestServices.allApartments);
		get(Path.Rest.GUEST_ALL_RESERVATIONS, ReservationServices.guestViewReservations);
		
		// NOTE(Jovan): Gzip compression
		after("*", Filters.addGzipHeader);

	}

}
