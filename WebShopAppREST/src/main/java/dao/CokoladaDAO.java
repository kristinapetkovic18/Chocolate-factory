package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.StringTokenizer;

import beans.Cokolada;
import beans.User;
import enums.Role;
import enums.VrstaCokolade;

public class CokoladaDAO {

	private HashMap<String, Cokolada> cokolade = new HashMap<String, Cokolada>();
	private String ctxPath = "";
	private String filePath = "C:/Users/user_name/Desktop/web/cokolade.txt";

	public CokoladaDAO() {
	}

	public CokoladaDAO(String contextPath) {
		this.ctxPath = contextPath;
		loadProducts(contextPath);

	}

	public Collection<Cokolada> findAll() {
		return cokolade.values();
	}

	public Cokolada getById(String id) {
		return cokolade.containsKey(id) ? cokolade.get(id) : null;
	}

	public ArrayList<Cokolada> getByFabrikaId(String fabrikaId) {
		ArrayList<Cokolada> pronadjeneCokolade = new ArrayList<>();
		for (Cokolada c : this.cokolade.values()) {
			if (c.getFabrikaId().equals(fabrikaId)) {
				pronadjeneCokolade.add(c);
			}
		}

		return pronadjeneCokolade;
	}
	
	
	public ArrayList<String> getFabrikaIdByCokoladaName(String ime) {

		ArrayList<String> pronadjeniId = new ArrayList<>();
		if (ime!= null && ime != "") {
			

			for (Cokolada c : this.cokolade.values()) {
				if (c.getIme().toLowerCase().contains(ime.toLowerCase()) ||
						c.getOpis().toLowerCase().contains(ime.toLowerCase())) {
					pronadjeniId.add(c.getFabrikaId());
				}
			}
			return pronadjeniId;
		}

		return null;
	}


	public Cokolada save(Cokolada product) {
		Integer maxId = -1;
		for (String id : cokolade.keySet()) {
			int idNum = Integer.parseInt(id);
			if (idNum > maxId) {
				maxId = idNum;
			}
		}
		maxId++;

		product.setId(String.valueOf(maxId));
		product.setKolicina(0);
		product.setNaStanju(false);
		saveNewChocolate(product, this.ctxPath);
		cokolade.put(String.valueOf(product.getId()), product);

		return product;
	}

	public void saveNewChocolate(Cokolada newChocolate, String contextPath) {
		BufferedWriter out = null;
		try {

			File file = new File(this.filePath);
			System.out.println("Saving to: " + file.getCanonicalPath());

			out = new BufferedWriter(new FileWriter(file, true));
			// out.newLine();
			out.write(newChocolate.getId() + ";" + newChocolate.isDeleted() + ";" + newChocolate.getIme() + ";"
					+ newChocolate.getOpis() + ";" + newChocolate.isNaStanju() + ";" + newChocolate.getVrsta() + ";"
					+ newChocolate.getFabrikaId() + ";" + newChocolate.getGramaza() + ";" + newChocolate.getCena() + ";"
					+ newChocolate.getKolicina() + ";" + newChocolate.getPutanja());
			out.newLine();
		} catch (IOException e) {
			System.err.println("Error writing to file: " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void loadProducts(String contextPath) {

		BufferedReader in = null;
		try {

			File file = new File(this.filePath);

			in = new BufferedReader(new FileReader(file));

			String line, ime = "", opis = "", id = "0", fabrikaId = "0";
			boolean naStanju = true, deleted = false;
			int kolicina = 0, vrstaa = 0;
			double gramaza = 0, cena = 0;
			String putanja = "";
			StringTokenizer st;

			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;

				st = new StringTokenizer(line, ";");
				while (st.hasMoreTokens()) {
					id = st.nextToken().trim();
					deleted = Boolean.parseBoolean(st.nextToken().trim());
					ime = st.nextToken().trim();
					opis = st.nextToken().trim();
					naStanju = Boolean.parseBoolean(st.nextToken().trim());

					vrstaa = Integer.parseInt(st.nextToken().trim());
					fabrikaId = st.nextToken().trim();

					gramaza = Double.parseDouble(st.nextToken().trim());
					cena = Double.parseDouble(st.nextToken().trim());
					kolicina = Integer.parseInt(st.nextToken().trim());
					putanja = st.nextToken().trim();
				}

				cokolade.put(id, new Cokolada(id, deleted, ime, opis, naStanju, vrstaa, fabrikaId, gramaza, cena,
						kolicina, putanja));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
		}

	}

	public Cokolada update(String id, Cokolada updatedCokolada) {
		if (cokolade.containsKey(id)) {
			updatedCokolada.setId(id);
			cokolade.put(id, updatedCokolada);
			rewriteFile();
			return updatedCokolada;
		}
		return null;
	}

	public void delete(String id) {

		// logicki
		Cokolada cokoladaZaBrisanje = getById(id);
		cokoladaZaBrisanje.setDeleted(true);
		update(id, cokoladaZaBrisanje);
		rewriteFile();
		// cokolade.remove(id);
	}

	private void rewriteFile() {
		BufferedWriter out = null;
		try {
			String filePath = "C:/Users/user_name/Desktop/web/cokolade.txt";
			File file = new File(this.filePath);
			out = new BufferedWriter(new FileWriter(file, false)); // Overwrite the file

			for (Cokolada cokolada : cokolade.values()) {
				out.write(cokolada.getId() + ";" + cokolada.isDeleted() + ";" + cokolada.getIme() + ";"
						+ cokolada.getOpis() + ";" + cokolada.isNaStanju() + ";" + cokolada.getVrsta() + ";"
						+ cokolada.getFabrikaId() + ";" + cokolada.getGramaza() + ";" + cokolada.getCena() + ";"
						+ cokolada.getKolicina() + ";" + cokolada.getPutanja());
				out.newLine();
			}
		} catch (IOException e) {
			System.err.println("Error writing to file: " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}


    public ArrayList<String> getFabrikaIdsByChocolateName(String name) {
        ArrayList<String> fabrikaIds = new ArrayList<>();
        for (Cokolada cokolada : cokolade.values()) {
            if (cokolada.getIme().equalsIgnoreCase(name)) {
                fabrikaIds.add(cokolada.getFabrikaId());
            }
        }
        return fabrikaIds;
    }
}
