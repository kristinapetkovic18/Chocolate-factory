package beans;

public class KupovinaItem {

	private String id;
	private String kupovinaId;
	private String cokoladaId;
	private String cokoladaIme;

	private int kolicina;
	private String fabrikaId;
	public String getFabrikaIme() {
		return fabrikaIme;
	}

	public void setFabrikaIme(String fabrikaIme) {
		this.fabrikaIme = fabrikaIme;
	}

	public KupovinaItem(String id, String kupovinaId, String cokoladaId, String cokoladaIme, int kolicina,
			String fabrikaId, String fabrikaIme) {
		super();
		this.id = id;
		this.kupovinaId = kupovinaId;
		this.cokoladaId = cokoladaId;
		this.cokoladaIme = cokoladaIme;
		this.kolicina = kolicina;
		this.fabrikaId = fabrikaId;
		this.fabrikaIme = fabrikaIme;
	}

	private String fabrikaIme;
	public KupovinaItem(String id, String kupovinaId, String cokoladaId, String cokoladaIme, int kolicina,
			String fabrikaId) {
		super();
		this.id = id;
		this.kupovinaId = kupovinaId;
		this.cokoladaId = cokoladaId;
		this.cokoladaIme = cokoladaIme;
		this.kolicina = kolicina;
		this.fabrikaId = fabrikaId;
	}

	public String getCokoladaIme() {
		return cokoladaIme;
	}

	public void setCokoladaIme(String cokoladaIme) {
		this.cokoladaIme = cokoladaIme;
	}

	public String getId() {
		return id;
	}

	public KupovinaItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	public KupovinaItem(String id, String kupovinaId, String cokoladaId, int kolicina, String fabrikaId) {
		super();
		this.id = id;
		this.kupovinaId = kupovinaId;
		this.cokoladaId = cokoladaId;
		this.kolicina = kolicina;
		this.fabrikaId = fabrikaId;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKupovinaId() {
		return kupovinaId;
	}

	public void setKupovinaId(String kupovinaId) {
		this.kupovinaId = kupovinaId;
	}

	public String getCokoladaId() {
		return cokoladaId;
	}

	public void setCokoladaId(String cokoladaId) {
		this.cokoladaId = cokoladaId;
	}

	public int getKolicina() {
		return kolicina;
	}

	public void setKolicina(int kolicina) {
		this.kolicina = kolicina;
	}

	public String getFabrikaId() {
		return fabrikaId;
	}

	public void setFabrikaId(String fabrikaId) {
		this.fabrikaId = fabrikaId;
	}

}
