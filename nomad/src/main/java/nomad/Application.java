package nomad;

import static spark.Spark.*;

import java.security.Key;

import com.google.gson.Gson;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import nomad.dao.UserAdminDAO;
import nomad.dao.UserGuestDAO;
import nomad.dao.UserHostDAO;
import nomad.services.AdminServices;
import nomad.services.GuestServices;
import nomad.services.HostServices;
import nomad.services.LoginServices;
import nomad.services.RegistrationServices;
import nomad.services.UserServices;
import nomad.utils.Filters;
import nomad.utils.Path;
import spark.Filter;
import spark.Request;
import spark.Response;

public class Application{
	
	public static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

	public static Gson gson;
	
	// TODO(Jovan): Prepraviti na logicko brisanje??
	public static UserAdminDAO adminDAO;
	public static UserGuestDAO guestDAO;
	public static UserHostDAO hostDAO;

	// TODO(Jovan): Separate into utility classes?
	
	public static String parseJws(Request request)
	{
		String auth = request.headers("Authorization");
		if(auth == null || auth.isEmpty() || !auth.contains("Bearer"))
		{
			return null;
		}
		
		return auth.length() <= 7 ? null : auth.substring(auth.indexOf("Bearer") + 7);
	}
	
	
	
	public static void main(String args[])
	{
		gson = new Gson();
		
		adminDAO  = new UserAdminDAO("admins.json");
		guestDAO = new UserGuestDAO("guests.json");
		hostDAO = new UserHostDAO("hosts.json");
		
		port(8080);
		staticFiles.location("/static");
		
		post(Path.Rest.REG_GUEST, RegistrationServices.registerGuest);
		
		post(Path.Rest.LOGIN, LoginServices.login);
		
		get("rest/test", LoginServices.test);
		
		// TODO(Jovan -> Kris): Pogledaj da li je samo za guest
		// ili da stavimo u uopsteni UserServices
		get("rest/getUser", GuestServices.getGuest);
		
		// TODO(Jovan -> Kris): Moved to UserServices
		post(Path.Rest.PERSONAL_DATA, UserServices.personalData);
		
		get(Path.Rest.ADMIN_ALL_USERS, AdminServices.getAllUsers);
		
		
		get(Path.Rest.HOST_ALL_APARTMENTS, HostServices.allApartments);
	
		// NOTE(Jovan): Gzip compression
		after("*", Filters.addGzipHeader);

	}
	
}
