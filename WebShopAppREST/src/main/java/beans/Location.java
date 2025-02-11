package beans;

public class Location{

	private String fabrikaId;
	public Location(String fabrikaId, Address address, double longitude, double latitude) {
		super();
		this.fabrikaId = fabrikaId;
		this.address = address;
		this.longitude = longitude;
		this.latitude = latitude;
	}

	private Address address;
	private double longitude;
	private double latitude;
	
	public Location() {
		super();
	}

	public boolean matchesLokacija(String lokacija) {
		boolean matches = false;
		if(this.address.getStreet().toLowerCase().contains(lokacija.toLowerCase())) {
			matches = true;
		}

		if(this.address.getCity().toLowerCase().contains(lokacija.toLowerCase())) {
			matches = true;
		}
		
		if(this.address.getPostalNumber().toLowerCase().contains(lokacija.toLowerCase())) {
			matches = true;
		}
	    return matches ;
	}
	
	public Location(double longitude, double latitude, Address address) {
		super();
		this.longitude = longitude;
		this.latitude = latitude;
		this.address = address;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getFabrikaId() {
		return fabrikaId;
	}

	public void setFabrikaId(String fabrikaId) {
		this.fabrikaId = fabrikaId;
	}
	

	
}

