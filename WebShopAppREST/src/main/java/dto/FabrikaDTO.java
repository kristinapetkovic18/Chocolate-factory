package dto;

import java.util.ArrayList;
import java.util.List;

import beans.Cokolada;
import beans.Location;
import beans.WorkHour;

public class FabrikaDTO {

	private String id;
	private String ime;
	private String lokacija;
	private double prosecnaOcena;
	public FabrikaDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	private double min;
	private double max;
	private String cokoladaIme;
	private String street;
	private String city;
	private String postalNumber;
	private String putanja;
	private Location location;
	private ArrayList<Cokolada> cokolade;
	private String slika;
	private boolean status;
	private WorkHour radnoVreme;
	private String startTime;
	private String endTime;
	
	
	
	public FabrikaDTO(String ime, String lokacija, String street, String city, String postalNumber, String putanja,
			String startTime, String endTime) {
		super();
		this.ime = ime;
		this.lokacija = lokacija;
		this.street = street;
		this.city = city;
		this.postalNumber = postalNumber;
		this.putanja = putanja;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	
	public String getPutanja() {
		return putanja;
	}

	public void setPutanja(String putanja) {
		this.putanja = putanja;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
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

	public String getCokoladaIme() {
		return cokoladaIme;
	}

	public void setCokoladaIme(String cokoladaIme) {
		this.cokoladaIme = cokoladaIme;
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	
	public FabrikaDTO(String id, String ime, String lokacija, double prosecnaOcena, String slika, boolean status,
			WorkHour radnoVreme, String start, String end, Location location, ArrayList<Cokolada> cokolade) {
		super();
		this.id = id;
		this.ime = ime;
		this.lokacija = lokacija;
		this.prosecnaOcena = prosecnaOcena;
		this.slika = slika;
		this.status = status;
		this.radnoVreme = radnoVreme;
		this.startTime = start;
		this.endTime = end;
		this.location = location;
		this.cokolade = cokolade;
	}

	public String getStart() {
		return startTime;
	}

	public void setStart(String start) {
		this.startTime = start;
	}

	public String getEnd() {
		return endTime;
	}

	public void setEnd(String end) {
		this.endTime = end;
	}

	public FabrikaDTO(String id, String ime, String lokacija, double prosecnaOcena, String slika, boolean status,
			WorkHour radnoVreme, Location location, ArrayList<Cokolada> cokolade) {
		super();
		this.id = id;
		this.ime = ime;
		this.lokacija = lokacija;
		this.prosecnaOcena = prosecnaOcena;
		this.slika = slika;
		this.status = status;
		this.radnoVreme = radnoVreme;
		this.location = location;
		this.cokolade = cokolade;
	}

	public FabrikaDTO(String id, String ime, double prosecnaOcena, String slika, boolean status, WorkHour radnoVreme) {
		super();
		this.id = id;
		this.ime = ime;
		this.lokacija = lokacija;
		this.prosecnaOcena = prosecnaOcena;
		this.slika = slika;
		this.status = status;
		this.radnoVreme = radnoVreme;
		this.location = location;
		this.cokolade = cokolade;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public WorkHour getRadnoVreme() {
		return radnoVreme;
	}

	public void setRadnoVreme(WorkHour radnoVreme) {
		this.radnoVreme = radnoVreme;
	}

	public FabrikaDTO(String id, String ime, String lokacija, double prosecnaOcena, String slika, boolean status,
			WorkHour radnoVreme, ArrayList<Cokolada> cokolade) {
		super();
		this.id = id;
		this.ime = ime;
		this.lokacija = lokacija;
		this.prosecnaOcena = prosecnaOcena;
		this.slika = slika;
		this.status = status;
		this.radnoVreme = radnoVreme;
		this.cokolade = cokolade;
	}


	public FabrikaDTO(String id, String ime, String lokacija, double prosecnaOcena, String slika,
			ArrayList<Cokolada> cokolade) {
		super();
		this.id = id;
		this.ime = ime;
		this.lokacija = lokacija;
		this.prosecnaOcena = prosecnaOcena;
		this.slika = slika;
		this.cokolade = cokolade;
	}

	public ArrayList<Cokolada> getCokolade() {
		return cokolade;
	}

	public void setCokolade(ArrayList<Cokolada> cokolade) {
		this.cokolade = cokolade;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getLokacija() {
		return lokacija;
	}

	public void setLokacija(String lokacija) {
		this.lokacija = lokacija;
	}

	public double getProsecnaOcena() {
		return prosecnaOcena;
	}

	public void setProsecnaOcena(double prosecnaOcena) {
		this.prosecnaOcena = prosecnaOcena;
	}

	public String getSlika() {
		return slika;
	}

	public void setSlika(String slika) {
		this.slika = slika;
	}

}
