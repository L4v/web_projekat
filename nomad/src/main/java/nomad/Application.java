package nomad;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

import java.security.Key;
import java.util.ArrayList;

import com.google.gson.Gson;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import nomad.beans.UserAdmin;
import nomad.beans.UserBase;
import nomad.beans.UserGuest;
import nomad.dao.UserAdminDAO;
import nomad.dao.UserGuestDAO;
import nomad.dao.UserHostDAO;
import nomad.dto.LoginDTO;
import nomad.services.AdminServices;
import nomad.services.UserLoginService;
import nomad.services.UserRegistrationService;
import nomad.utils.Path;

public class Application{
	
	static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

	public static void main(String args[])
	{
		Gson gson = new Gson();
		
		// TODO(Jovan): Prepraviti na logicko brisanje??
		UserAdminDAO adminDAO = new UserAdminDAO("admins.json");
		UserGuestDAO guestDAO = new UserGuestDAO("guests.json");
		UserHostDAO hostDAO = new UserHostDAO("hosts.json");
		
		UserRegistrationService userRegistrationService = new UserRegistrationService(guestDAO);
		UserLoginService userLoginService = new UserLoginService(adminDAO, guestDAO, hostDAO);
		
		AdminServices adminServices = new AdminServices(guestDAO, adminDAO, hostDAO);
		
		port(8080);
		staticFiles.location("/static");
		
		// TODO(Jovan): Ukloniti
		get(Path.Web.HELLO, (request, response) -> "Hello");
		post(Path.Rest.REG_GUEST, (request, response)->
		{
			response.type("application/json");
			String payload = request.body();
			UserGuest guest = gson.fromJson(payload, UserGuest.class);
			response.status(userRegistrationService.registerGuest(guest) ? 200 : 404);
			return response;
		});
		
		post(Path.Rest.LOGIN, (request, response)->
		{
			response.type("application/json");
			String payload = request.body();
			LoginDTO user = gson.fromJson(payload, LoginDTO.class);
			UserBase loggedInUser = userLoginService.validate(user);
			if(loggedInUser == null)
			{
				response.status(404);
				return null;
			}
			response.status(200);
			String jws = Jwts.builder().setSubject(loggedInUser.getUsername()).signWith(key).compact();
			
			return jws;
		});
		
		get("rest/test", (request, response) ->
		{
			String auth = request.headers("Authorization");
			if(auth.isEmpty()|| !auth.contains("Bearer"))
			{
				response.status(404);
			}
			else
			{
				// TODO(Jovan): Signature exception ????
				String jws = auth.substring(auth.indexOf("Bearer") + 7);
				try
				{
					Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
					response.body(claims.getBody().getSubject());
					response.status(200);
					return response;
				}
				catch(Exception e)
				{
					e.printStackTrace();
					response.status(404);
					return response;
				}
			}
			return response;
		});
		
		get("rest/getUser", (request, response)->
		{
			response.type("application/json");
			String auth = request.headers("Authorization");
			if(auth.isEmpty()|| !auth.contains("Bearer"))
			{
				response.status(404);
			}
			else
			{
				String jws = auth.substring(auth.indexOf("Bearer") + 7);
				try
				{
					Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
					UserGuest guest = guestDAO.get(claims.getBody().getSubject());
					response.status(200);
					String gs = gson.toJson(guest);
					return gson.toJson(guest);
				}
				catch(Exception e)
				{
					e.printStackTrace();
					response.status(404);
					return response;
				}
			}
			return response;
		});
		
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
			// TODO(Jovan): Pull out auth check into function?
			response.type("application/json");
			String auth = request.headers("Authorization");
			if(auth.isEmpty()|| !auth.contains("Bearer"))
			{
				response.status(404);
			}
			else
			{
				String jws = auth.substring(auth.indexOf("Bearer") + 7);
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
			return response;
		});
		
		
	}
	
}
