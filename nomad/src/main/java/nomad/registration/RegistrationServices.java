package nomad.registration;

import static nomad.Application.guestDAO;
import static nomad.Application.hostDAO;

import nomad.user.UserGuest;
import nomad.user.UserHost;

import static nomad.Application.gson;
import static nomad.utils.Responses.ok;
import static nomad.utils.Responses.badRequest;

import spark.Request;
import spark.Response;
import spark.Route;

public class RegistrationServices
{

	private static boolean registerGuest(UserGuest guest)
	{
		return guestDAO.add(guest);
	}
	
	private static boolean registerHost(UserHost host)
	{
		return hostDAO.add(host);
	}

	public static Route registerHost = (Request request, Response response) ->
	{
		// TODO(Jovan): Admin validation
		String payload = request.body();
		UserHost host = gson.fromJson(payload, UserHost.class);
		return registerHost(host) ? ok("Host registered", response) : badRequest("Host could not be registered", response);
	};
	
	public static Route registerGuest = (Request request, Response response) ->
	{
		response.type("application/json");
		String payload = request.body();
		UserGuest guest = gson.fromJson(payload, UserGuest.class);
		response.status(registerGuest(guest) ? 200 : 404);
		return response;
	};

}
