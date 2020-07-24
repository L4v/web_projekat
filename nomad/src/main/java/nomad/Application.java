package nomad;

import static spark.Spark.*;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.staticFiles;

import com.google.gson.Gson;

import nomad.beans.UserGuest;
import nomad.services.UserRegistrationService;
import nomad.utils.Path;

public class Application{

	public static void main(String args[])
	{
		Gson gson = new Gson();
		UserRegistrationService userRegistrationService = new UserRegistrationService();
		
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
			//TODO;
			return null;
		});
	}
	
}
