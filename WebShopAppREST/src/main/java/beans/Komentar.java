package beans;

import dto.KomentarDTO;
import enums.Status;

public class Komentar {

	private String id;
	private Status status;
	private String kupacId;
	private String kupacUsername;

	private String fabrikaId;
	private String tekst;
	private int ocena;
	
	public Komentar() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Komentar(String id, Status status, String kupacId, String kupacUsername, String fabrikaId, String tekst,
			int ocena) {
		super();
		this.id = id;
		this.status = status;
		this.kupacId = kupacId;
		this.kupacUsername = kupacUsername;
		this.fabrikaId = fabrikaId;
		this.tekst = tekst;
		this.ocena = ocena;
	}
	public Komentar(KomentarDTO dto) {
		
		this.status = Status.Obrada;
		this.kupacId = dto.getKupacId();
		this.kupacUsername = "";
		this.fabrikaId = dto.getFabrikaId();
		this.tekst = dto.getTekst();
		this.ocena = dto.getOcena();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public String getKupacId() {
		return kupacId;
	}
	public void setKupacId(String kupacId) {
		this.kupacId = kupacId;
	}
	public String getKupacUsername() {
		return kupacUsername;
	}
	public void setKupacUsername(String kupacUsername) {
		this.kupacUsername = kupacUsername;
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
}
