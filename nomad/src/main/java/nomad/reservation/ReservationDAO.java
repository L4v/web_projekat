package nomad.reservation;

import static nomad.Application.gson;
import static nomad.Application.guestDAO;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import nomad.user.UserGuest;

public class ReservationDAO
{

	private String filename;

	public ReservationDAO()
	{
		this.filename = "reservations.json";
		this.initFile();
	}

	public ReservationDAO(String filename)
	{
		this.filename = filename;
		this.initFile();
	}

	private void initFile()
	{
		File f = new File(this.filename);
		if (!f.isFile())
		{
			this.saveAll(new ArrayList<Reservation>());
		}
	}

	private void saveAll(Collection<Reservation> reservations)
	{
		try (FileWriter writer = new FileWriter(this.filename))
		{
			gson.toJson(reservations, writer);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public boolean remove(String id)
	{
		ArrayList<Reservation> reservations = (ArrayList<Reservation>) this.getAll();
		boolean success = reservations.removeIf(r -> r.getId().equals(id));
		if (success == true)
		{
			this.saveAll(reservations);
		}
		return success;
	}

	public boolean add(Reservation reservation)
	{
		ArrayList<Reservation> reservations = (ArrayList<Reservation>) this.getAll();
		String id = reservation.getApartmentId() + reservation.getGuestId() + reservation.getStartDate().getTime() + reservation.getStartDate().getTime();
		reservation.setId(id);
		
		for(Reservation r : reservations)
		{
			if(r.getId().equals(reservation.getId()))
			{
				return false;
			}
		}
		UserGuest guest = guestDAO.get(reservation.getGuestId());
		if(guest == null)
		{
			return false;
		}
		guest.addReservation(reservation.getId());
		guestDAO.update(guest);
		reservations.add(reservation);
		this.saveAll(reservations);
		return true;
	}

	public boolean update(Reservation reservation)
	{
		ArrayList<Reservation> reservations = (ArrayList<Reservation>) this.getAll();
		for (int i = 0; i < reservations.size(); ++i)
		{
			if (reservations.get(i).getId().equals(reservation.getId()))
			{
				reservations.set(i, reservation);
				this.saveAll(reservations);
				return true;
			}
		}
		return false;
	}

	public Reservation get(String id)
	{
		ArrayList<Reservation> reservations = (ArrayList<Reservation>) this.getAll();
		return reservations.stream().filter(r -> r.getId().equals(id)).findAny().orElse(null);
	}
	
	public Collection<Reservation> getForApartment(String apartmentId)
	{
		ArrayList<Reservation> reservations = (ArrayList<Reservation>) this.getAll();
		
		ArrayList<Reservation> forApartment = new ArrayList<Reservation>();
		for(Reservation r : reservations)
		{
			if(r.getApartmentId().equals(apartmentId))
			{
				forApartment.add(r);
			}
		}
		return forApartment;
	}

	public Collection<Reservation> getForGuest(String username)
	{
		ArrayList<Reservation> reservations = (ArrayList<Reservation>) this.getAll();
		
		ArrayList<Reservation> forGuest = new ArrayList<Reservation>();
		for(Reservation r : reservations)
		{
			if(r.getGuestId().equals(username))
			{
				reservations.add(r);
			}
		}
		return forGuest;
	}

	public Collection<Reservation> getByIds(Collection<String> ids)
	{
		ArrayList<Reservation> reservations = (ArrayList<Reservation>) this.getAll();
		
		ArrayList<Reservation> byIds = new ArrayList<Reservation>();
		for(String id : ids)
		{
			for(Reservation a : reservations)
			{
				if(a.getId().equals(id))
				{
					byIds.add(a);
				}
			}
		}
		return byIds;
	}

	public Collection<Reservation> getAll()
	{
		Collection<Reservation> reservations = null;
		Type collectionType = new TypeToken<Collection<Reservation>>()
		{
		}.getType();
		try (FileReader freader = new FileReader(this.filename); JsonReader jreader = new JsonReader(freader))
		{
			reservations = gson.fromJson(jreader, collectionType);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return reservations == null ? new ArrayList<Reservation>() : reservations;
	}
}
