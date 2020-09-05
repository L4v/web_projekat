package nomad.reservation;

import static nomad.Application.gson;
import static nomad.Application.guestDAO;
import static nomad.Application.hostDAO;
import static nomad.Application.adminDAO;
import static nomad.Application.key;
import static nomad.Application.parseJws;
import static nomad.Application.invalidResponse;
import static nomad.Application.reservationDAO;
import static nomad.Application.apartmentDAO;

import java.util.ArrayList;
import java.util.Collection;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import nomad.apartment.Apartment;
import nomad.user.UserAdmin;
import nomad.user.UserGuest;
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

	public static boolean verifyReservation(Reservation reservation)
	{

		// NOTE(Jovan): Check if apartment exists
		Apartment apartment = apartmentDAO.get(reservation.getApartment().getId());
		if (apartment == null)
		{
			return false;
		}

		// NOTE(Jovan): Check if reservation already exists
		if (apartment.getReservations().stream().filter(r -> r.getId().equals(reservation.getId())).findAny()
				.orElse(null) != null)
		{
			return false;
		}

		// NOTE(Jovan): Check if reservation interval is valid
		boolean validTimeInterval = reservationDAO.getAll().stream()
				.filter(r -> r.getStartDate().after(reservation.getStartDate())
						&& r.getEndDate().before(reservation.getEndDate()))
				.findAny().orElse(null) == null;

		return validTimeInterval;
	}

	public static Route hostViewReservations = (Request request, Response response) ->
	{
		response.type("application/json");
		String jws = parseJws(request);
		if (jws == null)
		{
			return invalidResponse("Invalid login", response);
		}
		try
		{
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
			UserHost host = hostDAO.get(claims.getBody().getSubject());
			if (host == null)
			{
				return invalidResponse("Not a host", response);
			}

			ArrayList<Reservation> reservations = (ArrayList<Reservation>) getHostReservations(host);

			response.status(200);
			response.body(gson.toJson(reservations));
			return response;
		} catch (Exception e)
		{
			return invalidResponse("Server error: " + e.getMessage(), response);
		}
	};

	public static Route guestViewReservations = (Request request, Response response) ->
	{
		response.type("application/json");
		String jws = parseJws(request);
		if (jws == null)
		{
			return invalidResponse("Invalid login", response);
		}
		try
		{
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
			UserGuest guest = guestDAO.get(claims.getBody().getSubject());
			if (guest == null)
			{
				return invalidResponse("Not a guest", response);
			}
			ArrayList<Reservation> reservations = guest.getReservations();
			
			response.status(200);
			response.body(gson.toJson(reservations));
			return response;
		} catch (Exception e)
		{
			return invalidResponse("Server error: " + e.getMessage(), response);
		}
	};

	public static Route adminViewReservations = (Request request, Response response) ->
	{
		response.type("application/json");
		String jws = parseJws(request);
		if (jws == null)
		{
			return invalidResponse("Invalid login", response);
		}
		try
		{
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
			UserAdmin admin = adminDAO.get(claims.getBody().getSubject());
			if (admin == null)
			{
				response.status(404);
				return response;
			}

			ArrayList<Reservation> reservations = (ArrayList<Reservation>) reservationDAO.getAll();

			response.status(200);
			response.body(gson.toJson(reservations));
			return response;
		} catch (Exception e)
		{
			return invalidResponse("Server error: " + e.getMessage(), response);
		}
	};

	public static Route createReservation = (Request request, Response response) ->
	{
		// TODO(Jovan): Pull parsing and jws check into function?
		String jws = parseJws(request);
		if (jws == null)
		{
			return invalidResponse("Invalid login", response);
		}

		String json = request.body();
		Reservation reservation = gson.fromJson(json, Reservation.class);
		if (verifyReservation(reservation) == false)
		{
			return invalidResponse("Invalid reservation", response);
		}

		Apartment apartment = reservation.getApartment();
		apartment.addReservation(reservation);
		apartmentDAO.update(apartment);
		
		reservationDAO.add(reservation);
		
		response.status(200);
		response.body("Reservation added");
		return response;
	};
	
	//TODO: da li je dovoljno ovo ili mora i kod apartmana i kod gosta da update-ujemo??
	public static Route guestCancelReservation = (Request request, Response response) ->
	{
		response.type("application/json");
		String jws = parseJws(request);
		if (jws == null)
		{
			return invalidResponse("Invalid login", response);
		}

		try
		{
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
			UserGuest guest = guestDAO.get(claims.getBody().getSubject());
			if (guest == null)
			{
				return invalidResponse("Not guest", response);
			}

			String json = request.body();
			Reservation reservation = gson.fromJson(json, Reservation.class);
			if(reservation.getStatus() == ReservationStatus.ACCEPTED || reservation.getStatus() == ReservationStatus.CREATED)
			{
				reservation.setStatus(ReservationStatus.CANCELLED);
				if(reservationDAO.update(reservation) && apartmentDAO.update(reservation.getApartment()) && guestDAO.update(reservation.getGuest()))
				{
					response.status(200);
					response.body("Reservation cancelled");
					return response;
				}
				return invalidResponse("Failed cancelling reservation", response);
			}
			return invalidResponse("Failed cancelling reservation - reservation status is not ACCEPTED or CREATED", response);
			
		} catch (Exception e)
		{
			return invalidResponse("Server error: " + e.getMessage(), response);
		}
	};
	
}
