package nomad;

import static spark.Spark.*;

import java.security.Key;
import java.util.ArrayList;

import com.google.gson.Gson;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import nomad.beans.Apartment;
import nomad.beans.UserAdmin;
import nomad.beans.UserBase;
import nomad.beans.UserGuest;
import nomad.beans.UserHost;
import nomad.dao.UserAdminDAO;
import nomad.dao.UserGuestDAO;
import nomad.dao.UserHostDAO;
import nomad.dto.LoginDTO;
import nomad.services.AdminServices;
import nomad.services.GuestServices;
import nomad.services.HostsServices;
import nomad.services.LoginServices;
import nomad.services.RegistrationServices;
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

	public static Filter addGzipHeader = (Request request, Response response) ->
	{
		response.header("Content-Encoding", "gzip");
	};
	
	
	
	public static void main(String args[])
	{
		gson = new Gson();
		
		adminDAO  = new UserAdminDAO("admins.json");
		guestDAO = new UserGuestDAO("guests.json");
		hostDAO = new UserHostDAO("hosts.json");
		
		HostsServices hostsServices = new HostsServices(hostDAO);
		
		AdminServices adminServices = new AdminServices(guestDAO, adminDAO, hostDAO);
		
		port(8080);
		staticFiles.location("/static");
		
		// TODO(Jovan): Ukloniti
		get(Path.Web.HELLO, (request, response) -> "Hello");
		post(Path.Rest.REG_GUEST, RegistrationServices.registerGuest);
		
		post(Path.Rest.LOGIN, LoginServices.login);
		
		get("rest/test", LoginServices.test);
		
		get("rest/getUser", GuestServices.getGuest);
		
		post(Path.Rest.PERSONAL_DATA, (request, response)->
		{
			response.type("application/json");
			String payload = request.body();
			UserGuest user = gson.fromJson(payload, UserGuest.class);
			
			guestDAO.update(user);
			System.out.println(user.getName());
			response.status(200);
			return response;
			
			/*response.type("application/json");
			String payload = request.body();
			UserBase user = gson.fromJson(payload, UserBase.class);
			if(user instanceof UserGuest)
			{
				guestDAO.update((UserGuest)user);
			}
			else if(user instanceof UserAdmin) 
			{
				adminDAO.update((UserAdmin)user);
			}
			else if(user instanceof UserHost)
			{
				hostDAO.update((UserHost)user);
			}
			System.out.println(user.getName());
			response.status(200);
			return response;*/
		});
		
		get(Path.Rest.ADMIN_ALL_USERS, (request, response) ->
		{
			String jws = parseJws(request);
			if(jws == null)
			{
				response.status(404);
				return response;
			}
			else
			{
				try
				{
					Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
					UserAdmin admin = adminDAO.get(claims.getBody().getSubject());
					if(admin == null)
					{
						response.status(404);
						return response;
					}
					response.status(200);
					ArrayList<UserBase> users = (ArrayList<UserBase>) adminServices.getAll();
					String usersJson = gson.toJson(users);
					return usersJson;
				}
				catch(Exception e)
				{
					e.printStackTrace();
					response.status(404);
					return response;
				}
			}
		});
		
		
		get(Path.Rest.HOST_ALL_APARTMENTS, (request, response) ->
		{
			response.type("application/json");
			String jws = parseJws(request);
			
			if(jws == null)
			{
				response.status(404);
				return response;
			}
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
			UserHost host = hostDAO.get(claims.getBody().getSubject());
			if(host == null)
			{
				response.status(404);
				return response;
			}
			ArrayList<Apartment> apartments = (ArrayList<Apartment>) hostsServices.getApartments(host.getUsername());
			return gson.toJson(apartments);
		});
	
		// NOTE(Jovan): Gzip compression
		after("*", addGzipHeader);

	}
	
}
