package nomad.reservation;

import static nomad.Application.gson;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

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
		if (reservations.stream().filter(r -> r.getId().equals(reservation.getId())).findAny().orElse(null) == null)
		{
			reservations.add(reservation);
			this.saveAll(reservations);
			return true;
		}
		return false;
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
