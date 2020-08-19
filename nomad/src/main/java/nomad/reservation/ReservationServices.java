package nomad.reservation;

import static nomad.Application.gson;
import static nomad.Application.hostDAO;
import static nomad.Application.key;
import static nomad.Application.parseJws;

import java.util.ArrayList;
import java.util.Collection;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import nomad.apartment.Apartment;
import nomad.user.UserHost;
import spark.Request;
import spark.Response;
import spark.Route;

public class ReservationServices
{

	private static Collection<Reservation> getHostReservations(UserHost host)
	{
		ArrayList<Reservation> hostsReservations = new ArrayList<Reservation>();
		for (Apartment apartment : host.getApartments())
		{
			hostsReservations.addAll(apartment.getReservations());
		}

		return hostsReservations;
	}

	public static Route hostViewReservations = (Request request, Response response) ->
	{
		response.type("application/json");
		String jws = parseJws(request);
		if (jws == null)
		{
			response.status(404);
			response.body("Invalid login!");
			return response;
		}
		try
		{
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
			UserHost host = hostDAO.get(claims.getBody().getSubject());
			if (host == null)
			{
				response.status(404);
				response.body("Not host!");
				return response;
			}

			ArrayList<Reservation> reservations = (ArrayList<Reservation>) getHostReservations(host);

			response.status(200);
			response.body(gson.toJson(reservations));
			return response;
		} catch (Exception e)
		{
			e.printStackTrace();
			response.status(500);
			return response;
		}
	};
}
