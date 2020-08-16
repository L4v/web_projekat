package nomad.services;

import static nomad.Application.guestDAO;
import static nomad.Application.gson;
import nomad.beans.UserGuest;
import spark.Route;
import spark.Request;
import spark.Response;

public class UserServices {

	public static Route personalData = (Request request, Response response) ->
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
	};
}
