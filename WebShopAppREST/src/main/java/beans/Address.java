package beans;

public class Address  {

	private String street;
	private String city;
	private String postalNumber;
	
	public Address() {}
	
	public Address(String street, String city, String postalNumber) {
		super();
		this.street = street;
		this.city = city;
		this.postalNumber = postalNumber;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}


	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostalNumber() {
		return postalNumber;
	}

	public void setPostalNumber(String postalNumber) {
		this.postalNumber = postalNumber;
	}
	
	public boolean cityMatches(String cityParameter) {
		return city.toLowerCase().contains(cityParameter.toLowerCase());
	}
}

