package nomad.apartment;

public class ApartmentImageDTO
{
	private String apartmentId;
	private String image;
	
	public ApartmentImageDTO(String apartmentId, String image)
	{
		this.apartmentId = apartmentId;
		this.image = image;
	}
	
	public String getApartmentId()
	{
		return this.apartmentId;
	}
	
	public void setApartmentId(String apartmentId)
	{
		this.apartmentId = apartmentId;
	}
	
	public String getImage()
	{
		return this.image;
	}
	
	public void setImage(String image)
	{
		this.image = image;
	}
}
