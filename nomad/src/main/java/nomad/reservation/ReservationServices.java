package nomad.reservation;

import static nomad.utils.Responses.notFound;
import static nomad.utils.Responses.ok;
import static nomad.utils.Responses.forbidden;
import static nomad.Application.adminDAO;
import static nomad.Application.apartmentDAO;
import static nomad.Application.gson;
import static nomad.Application.guestDAO;
import static nomad.Application.hostDAO;
import static nomad.Application.key;
import static nomad.Application.parseJws;
import static nomad.Application.reservationDAO;
import static nomad.utils.Responses.badRequest;
import static nomad.utils.Responses.serverError;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

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
		for (Apartment apartment : apartmentDAO.getByIds(host.getApartments()))
		{
			hostsReservations.addAll(reservationDAO.getForApartment(apartment.getId()));
		}

		return hostsReservations;
	}

	private static boolean verifyReservation(Reservation reservation)
	{

		// NOTE(Jovan): Check if apartment exists
		Apartment apartment = apartmentDAO.get(reservation.getApartmentId());
		if (apartment == null)
		{
			return false;
		}

		/* NOTE(Jovan): This validation happens in add method of DAO
		// NOTE(Jovan): Check if reservation already exists
		if (reservationDAO.getForApartment(apartment.getId()).stream().filter(r -> r.getId().equals(reservation.getId())).findAny()
				.orElse(null) != null)
		{
			return false;
		}
		*/

		// NOTE(Jovan): Check if reservation interval is valid
		boolean validTimeInterval = reservationDAO.getAll().stream()
				.filter(r -> r.getStartDate().after(reservation.getStartDate())
						&& r.getEndDate().before(reservation.getEndDate()))
				.findAny().orElse(null) == null;

		return validTimeInterval;
	}
	
	private static double calculateReservationPrice(Reservation reservation)
	{
		// TODO(Jovan): Add dates
		Calendar calendar = Calendar.getInstance();
		double price = 0;
		Date startDate = new Date(reservation.getStartDate().getTime());
		Apartment apartment = apartmentDAO.get(reservation.getApartmentId());
		if(apartment == null)
		{
			return -1;
		}
		for(int i = 0; i < reservation.getNoDays(); ++i)
		{
			calendar.setTime(startDate);
			if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
					|| calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
			{
				// NOTE(Jovan): If it's a weekday, 10% off
				price += apartment.getPrice() * 0.9;
				continue;
			}
			
			// TODO(Jovan): Holiday and holiday + weekday
			
			price += apartment.getPrice();
			calendar.add(Calendar.DATE, 1);
			startDate = calendar.getTime();
		}
		
		return price;
	}

	public static Route hostViewReservations = (Request request, Response response) ->
	{
		String jws = parseJws(request);
		if (jws == null)
		{
			return forbidden("Invalid login", response);
		}
		try
		{
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
			UserHost host = hostDAO.get(claims.getBody().getSubject());
			if (host == null)
			{
				return forbidden("Not a host", response);
			}

			ArrayList<Reservation> reservations = (ArrayList<Reservation>) getHostReservations(host);

			String json = gson.toJson(reservations);
			response.type("application/json");
			return ok(json, response);
		} catch (Exception e)
		{
			return serverError("Server error: " + e.getMessage(), response);
		}
	};

	public static Route guestViewReservations = (Request request, Response response) ->
	{
		response.type("application/json");
		String jws = parseJws(request);
		if (jws == null)
		{
			return forbidden("Invalid login", response);
		}
		try
		{
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
			UserGuest guest = guestDAO.get(claims.getBody().getSubject());
			if (guest == null)
			{
				return forbidden("Not a guest", response);
			}
			ArrayList<Reservation> reservations = (ArrayList<Reservation>)reservationDAO.getByIds(guest.getReservations());
			String json = gson.toJson(reservations);
			return ok(json, response);
		} catch (Exception e)
		{
			return serverError("Server error: " + e.getMessage(), response);
		}
	};

	public static Route adminViewReservations = (Request request, Response response) ->
	{
		response.type("application/json");
		String jws = parseJws(request);
		if (jws == null)
		{
			return forbidden("Invalid login", response);
		}
		try
		{
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
			UserAdmin admin = adminDAO.get(claims.getBody().getSubject());
			if (admin == null)
			{
				return forbidden("Invalid admin", response);
			}

			ArrayList<Reservation> reservations = (ArrayList<Reservation>) reservationDAO.getAll();
			String json = gson.toJson(reservations);
			return ok(json, response);
		} catch (Exception e)
		{
			return serverError("Server error: " + e.getMessage(), response);
		}
	};

	public static Route createReservation = (Request request, Response response) ->
	{
		String jws = parseJws(request);
		if (jws == null)
		{
			return forbidden("User not guest", response);
		}

		try
		{
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
			UserGuest guest = guestDAO.get(claims.getBody().getSubject());
			if(guest == null)
			{
				return forbidden("User not guest", response);
			}
			
			String json = request.body();
			Reservation reservation = gson.fromJson(json, Reservation.class);
			if (verifyReservation(reservation) == false)
			{
				return badRequest("Invalid reservation", response);
			}
			reservation.setGuestId(guest.getUsername());
			reservation.setTotalPrice(calculateReservationPrice(reservation));
			reservation.setStatus(ReservationStatus.CREATED);
			if(reservationDAO.add(reservation) == true)
			{
				String resJson = gson.toJson(reservation);
				// NOTE(Jovan): Sending for preview
				// TODO(Jovan): Maybe send only id?
				response.type("application/json");
				return ok(resJson, response);
			}
			
			return badRequest("Failed adding reservation", response);
		}
		catch(Exception e)
		{
			return serverError("Server error " + e.getMessage(), response);
		}
	};
	
	public static Route guestCancelReservation = (Request request, Response response) ->
	{
		String jws = parseJws(request);
		if (jws == null)
		{
			return forbidden("Invalid login", response);
		}

		try
		{
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
			UserGuest guest = guestDAO.get(claims.getBody().getSubject());
			if (guest == null)
			{
				return forbidden("Not guest", response);
			}

			String json = request.body();
			Reservation reservation = gson.fromJson(json, Reservation.class);
			if(reservation.getStatus() == ReservationStatus.ACCEPTED || reservation.getStatus() == ReservationStatus.CREATED)
			{
				reservation.setStatus(ReservationStatus.CANCELLED);
				if(reservationDAO.update(reservation))
				{
					guest.removeReservation(reservation.getId());
					guestDAO.update(guest);
					ArrayList<Reservation> reservations = (ArrayList<Reservation>) reservationDAO.getByIds(guest.getReservations());
					String jsonRes = gson.toJson(reservations);
					return ok(jsonRes, response);
				}
				return badRequest("Failed cancelling reservation", response);
			}
			return badRequest("Failed cancelling reservation - reservation status is not ACCEPTED or CREATED", response);
			
		} catch (Exception e)
		{
			return serverError("Server error: " + e.getMessage(), response);
		}
	};
	
	public static Route checkIfHasReservation = (Request request, Response response) ->
	{
		response.type("application/json");
		String jws = parseJws(request);
		if (jws == null)
		{
			return notFound("Invalid login", response);
		}

		try
		{
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
			UserGuest guest = guestDAO.get(claims.getBody().getSubject());
			if (guest == null)
			{
				return notFound("Not guest", response);
			}

			String json = request.body();
			Apartment apartment = gson.fromJson(json, Apartment.class);
			
			ArrayList<Reservation> reservations = (ArrayList<Reservation>) reservationDAO.getForApartment(apartment.getId());
			if(isAllowed(reservations, guest.getUsername()))
			{
				return ok("Allowed", response);
			}
			return forbidden("Not allowed", response);
		} catch (Exception e)
		{
			return serverError("Server error: " + e.getMessage(), response);
		}
	};
	
	private static boolean isAllowed(ArrayList<Reservation> reservations, String username) {
		for(Reservation reservation : reservations)
		{
			if(reservation.getGuestId().equals(username))
			{
				return true;
			}
		}
		return false;
	}
	
}
