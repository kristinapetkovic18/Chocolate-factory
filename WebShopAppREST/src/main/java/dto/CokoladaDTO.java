package dto;

public class CokoladaDTO {
//kolicina, boolean na , , cenu, , tip, gramažu,  i sliku.

	private String ime;
	private String opis;
	private boolean naStanju;
	private int vrsta;
	private int fabrikaId;

	public CokoladaDTO(String ime, String opis, boolean naStanju, int vrsta, int fabrikaId, double gramaza, double cena,
			int kolicina, String putanja) {
		super();
		this.ime = ime;
		this.opis = opis;
		this.naStanju = naStanju;
		this.vrsta = vrsta;
		this.fabrikaId = fabrikaId;
		this.gramaza = gramaza;
		this.cena = cena;
		this.kolicina = kolicina;
		this.putanja = putanja;
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

	public int getVrsta() {
		return vrsta;
	}

	public void setVrsta(int vrsta) {
		this.vrsta = vrsta;
	}

	public int getFabrikaId() {
		return fabrikaId;
	}

	public void setFabrikaId(int fabrikaId) {
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

	public String getPutanja() {
		return putanja;
	}

	public void setPutanja(String putanja) {
		this.putanja = putanja;
	}

	private double gramaza;
	private double cena;
	private int kolicina;
	private String putanja;

}
