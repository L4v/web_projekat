package nomad;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

import java.security.Key;

import com.google.gson.Gson;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import nomad.beans.UserBase;
import nomad.beans.UserGuest;
import nomad.dto.LoginDTO;
import nomad.dto.SessionDTO;
import nomad.services.UserLoginService;
import nomad.services.UserRegistrationService;
import nomad.utils.Path;

public class Application{
	
	static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

	public static void main(String args[])
	{
		Gson gson = new Gson();
		UserRegistrationService userRegistrationService = new UserRegistrationService();
		UserLoginService userLoginService = new UserLoginService();
		
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
			SessionDTO session = new SessionDTO(loggedInUser.getUsername(), loggedInUser.getUserType());
			String jws = Jwts.builder().setPayload(gson.toJson(session)).signWith(key).compact();
			
			return jws;
		});
	}
	
}
