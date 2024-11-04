package beans;

public class Cokolada {
	
	
	@Override
	public String toString() {
		return "Cokolada [id=" + id + ", deleted=" + deleted + ", ime=" + ime + ", opis=" + opis + ", naStanju="
				+ naStanju + ", vrsta=" + vrsta + ", fabrikaId=" + fabrikaId + ", gramaza=" + gramaza + ", cena=" + cena
				+ ", kolicina=" + kolicina + ", putanja=" + putanja + "]";
	}
	private String id;
	private boolean deleted;
	private String ime;
	private String opis;
	private boolean naStanju;
	private int vrsta;
	private String fabrikaId;
	private double gramaza;
	private double cena;
	private int kolicina;
	private String putanja;
	
	public String getPutanja() {
		return putanja;
	}
	public void setPutanja(String putanja) {
		this.putanja = putanja;
	}

	
	public Cokolada(String id, boolean deleted, String ime, String opis, boolean naStanju, int vrstsa, String fabrikaId, double gramaza,
			double cena, int kolicina,  String putanja) {
		this.id = id;
		this.deleted = deleted;
		this.ime = ime;
		this.opis = opis;
		this.naStanju = naStanju;
		this.vrsta = vrstsa;
		this.fabrikaId = fabrikaId;
		this.gramaza = gramaza;
		this.cena = cena;
		this.kolicina = kolicina;
		this.putanja = putanja;
	}
	
	
	public Cokolada() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	public String getOpis() {
		return opis;
	}
	public void setOpis(String opis) {
		this.opis = opis;
	}
	public boolean isNaStanju() {
		return naStanju;
	}
	public void setNaStanju(boolean naStanju) {
		this.naStanju = naStanju;
	}

	public String getFabrikaId() {
		return fabrikaId;
	}
	public void setFabrikaId(String fabrikaId) {
		this.fabrikaId = fabrikaId;
	}
	public double getGramaza() {
		return gramaza;
	}
	public void setGramaza(double gramaza) {
		this.gramaza = gramaza;
	}
	public double getCena() {
		return cena;
	}
	public void setCena(double cena) {
		this.cena = cena;
	}
	public int getKolicina() {
		return kolicina;
	}
	public void setKolicina(int kolicina) {
		this.kolicina = kolicina;
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
	public int getVrsta() {
		return vrsta;
	}
	public void setVrsta(int vrsta) {
		this.vrsta = vrsta;
	}
}
