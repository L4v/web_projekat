package nomad.services;

import static nomad.Application.guestDAO;
import static nomad.Application.gson;
import nomad.beans.UserGuest;
import spark.Request;
import spark.Response;
import spark.Route;

public class RegistrationServices
{

	private static boolean registerGuest(UserGuest guest)
	{
		return guestDAO.add(guest);
	}

	public static Route registerGuest = (Request request, Response response) ->
	{
		response.type("application/json");
		String payload = request.body();
		UserGuest guest = gson.fromJson(payload, UserGuest.class);
		response.status(registerGuest(guest) ? 200 : 404);
		return response;
	};

}
