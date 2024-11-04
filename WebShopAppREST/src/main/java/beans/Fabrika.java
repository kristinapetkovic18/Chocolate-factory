package beans;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Base64;

public class Fabrika{

	public Fabrika() {
		super();
		// TODO Auto-generated constructor stub
	}
	private String id;
	private boolean deleted;
	private String ime;
	private boolean status;
	private String lokacija;
	private double prosecnaOcena;
	private WorkHour radnoVreme;
	private String slika;
	
	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
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
	public WorkHour getRadnoVreme() {
		return radnoVreme;
	}
	public void setRadnoVreme(WorkHour radnoVreme) {
		this.radnoVreme = radnoVreme;
	}
	public String getSlika() {
		return slika;
	}
	public void setSlika(String slika) {
		this.slika = slika;
	}

	public boolean matchesIme(String ime) {
	    return this.ime.toLowerCase().contains(ime.toLowerCase());
	}

	

	public boolean matchesMin(double min) {
		
		return this.prosecnaOcena >= min;
	}
	

	public boolean matchesMax(double max) {
		
		return this.prosecnaOcena <= max;
	}
	
	public Fabrika(String id, boolean deleted, String ime, boolean status, String lokacija, double prosecnaOcena, WorkHour radnoVreme,
			String slika) {
		this.deleted = deleted;
		this.id = id;
		this.ime = ime;
		this.status = status;
		this.lokacija = lokacija;
		this.prosecnaOcena = prosecnaOcena;
		this.radnoVreme = radnoVreme;
		this.slika = slika;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	
}
