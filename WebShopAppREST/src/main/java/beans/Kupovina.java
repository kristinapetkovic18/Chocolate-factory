package beans;

import java.time.LocalDate;
import java.util.List;

import enums.Status;

public class Kupovina {
	private String id; //10 karaktera
	private String kupacId;
	private String fabrikaId;
	public String getFabrikaId() {
		return fabrikaId;
	}
	public void setFabrikaId(String fabrikaId) {
		this.fabrikaId = fabrikaId;
	}
	public Kupovina(String id, String kupacId, String imeIprezimeKupca, double cena,
			LocalDate vremeKupovine, Status status, String porukaMenadzera, String fabrikaId) {
		super();
		this.id = id;
		this.kupacId = kupacId;
		this.fabrikaId = fabrikaId;
		this.imeIprezimeKupca = imeIprezimeKupca;
		this.cena = cena;
		this.vremeKupovine = vremeKupovine;
		this.status = status;
		this.porukaMenadzera = porukaMenadzera;
	}
	private String imeIprezimeKupca;
	//posle kupovine obavljene smanjiti kolicinu cokolada u fabrici prvo proveriti je  l ih ima uopste
	private double cena;
	private LocalDate vremeKupovine; //treba i datum i vreme
	private Status status;
	private String porukaMenadzera;

	
	public Kupovina(String id, String kupacId, String imeIprezimeKupca, double cena, LocalDate vremeKupovine,
			Status status, String porukaMenadzera) {
		super();
		this.id = id;
		this.kupacId = kupacId;
		this.imeIprezimeKupca = imeIprezimeKupca;
		this.cena = cena;
		this.vremeKupovine = vremeKupovine;
		this.status = status;
		this.porukaMenadzera = porukaMenadzera;
	}
	public String getPorukaMenadzera() {
		return porukaMenadzera;
	}
	public void setPorukaMenadzera(String porukaMenadzera) {
		this.porukaMenadzera = porukaMenadzera;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getKupacId() {
		return kupacId;
	}
	public void setKupacId(String kupacId) {
		this.kupacId = kupacId;
	}
	public String getImeIprezimeKupca() {
		return imeIprezimeKupca;
	}
	public void setImeIprezimeKupca(String imeIprezimeKupca) {
		this.imeIprezimeKupca = imeIprezimeKupca;
	}
	public double getCena() {
		return cena;
	}
	public void setCena(double cena) {
		this.cena = cena;
	}
	public LocalDate getVremeKupovine() {
		return vremeKupovine;
	}
	public void setVremeKupovine(LocalDate vremeKupovine) {
		this.vremeKupovine = vremeKupovine;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public Kupovina(String id, String kupacId, String imeIprezimeKupca, double cena, LocalDate vremeKupovine,
			Status status) {
		super();
		this.id = id;
		this.kupacId = kupacId;
		this.imeIprezimeKupca = imeIprezimeKupca;
		this.cena = cena;
		this.vremeKupovine = vremeKupovine;
		this.status = status;
	}
	public Kupovina() {
		super();
		// TODO Auto-generated constructor stub
	}

	
}
