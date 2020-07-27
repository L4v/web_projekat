package nomad;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

import java.security.Key;

import com.google.gson.Gson;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import nomad.beans.UserBase;
import nomad.beans.UserGuest;
import nomad.dao.UserAdminDAO;
import nomad.dao.UserGuestDAO;
import nomad.dao.UserHostDAO;
import nomad.dto.LoginDTO;
import nomad.services.UserLoginService;
import nomad.services.UserRegistrationService;
import nomad.utils.Path;

public class Application{
	
	static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

	public static void main(String args[])
	{
		Gson gson = new Gson();
		
		// TODO(Jovan): Refaktorisati dao-e
		UserAdminDAO adminDAO = new UserAdminDAO();
		UserGuestDAO guestDAO = new UserGuestDAO("users.json");
		UserHostDAO hostDAO = new UserHostDAO();
		
		UserRegistrationService userRegistrationService = new UserRegistrationService(guestDAO);
		UserLoginService userLoginService = new UserLoginService(adminDAO, guestDAO, hostDAO);
		
		port(8080);
		staticFiles.location("/static");
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
		
		
	}
	
}
