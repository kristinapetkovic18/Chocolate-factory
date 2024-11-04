package dto;

import enums.Status;

public class KomentarDTO {

	private String kupacId;

	public String getKupacId() {
		return kupacId;
	}
	public void setKupacId(String kupacId) {
		this.kupacId = kupacId;
	}
	public KomentarDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getFabrikaId() {
		return fabrikaId;
	}
	public void setFabrikaId(String fabrikaId) {
		this.fabrikaId = fabrikaId;
	}
	public String getTekst() {
		return tekst;
	}
	public void setTekst(String tekst) {
		this.tekst = tekst;
	}
	public int getOcena() {
		return ocena;
	}
	public void setOcena(int ocena) {
		this.ocena = ocena;
	}
	public KomentarDTO(String kupacId, String fabrikaId, String tekst, int ocena) {
	super();
	this.kupacId = kupacId;
	this.fabrikaId = fabrikaId;
	this.tekst = tekst;
	this.ocena = ocena;
}
	private String fabrikaId;
	private String tekst;
	private int ocena;
}
