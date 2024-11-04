package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import beans.Address;
import beans.Cokolada;
import beans.Fabrika;
import beans.Location;
import enums.VrstaCokolade;

public class LokacijaDAO {

	private HashMap<String, Location> lokacije = new HashMap<String, Location>();
	private String ctxPath = "";
	private String filePath = "C:/Users/user_name/Desktop/web/lokacije.txt";

	public LokacijaDAO() {
	}

	public LokacijaDAO(String contextPath) {
		this.ctxPath = contextPath;
		loadProducts(contextPath);

	}

	public Collection<Location> findAll() {
		return lokacije.values();
	}

	public Location getById(String id) {
		return lokacije.containsKey(id) ? lokacije.get(id) : null;
	}
	
	  public List<String> searchFabrike(String lokacija) {
	        List<String> listOfIds = new ArrayList<>();

	        lokacije.values().stream()
	            .filter(fabrika -> lokacija == null || fabrika.matchesLokacija(lokacija.toLowerCase()))
	            .forEach(fabrika -> listOfIds.add(fabrika.getFabrikaId())); 
	        return listOfIds;
	}


	public Location save(Location product) {
		Integer maxId = -1;
		for (String id : lokacije.keySet()) {
			int idNum = Integer.parseInt(id);
			if (idNum > maxId) {
				maxId = idNum;
			}
		}
		maxId++;

		saveNew(product, this.ctxPath);
		lokacije.put(String.valueOf(product.getFabrikaId()), product);

		return product;
	}

	public void saveNew(Location newChocolate, String contextPath) {
		BufferedWriter out = null;
		try {

			File file = new File(this.filePath);
			System.out.println("Saving to: " + file.getCanonicalPath());

			out = new BufferedWriter(new FileWriter(file, true));
			out.write(newChocolate.getFabrikaId() + ";" + newChocolate.getLongitude() + ";" + newChocolate.getLatitude()
					+ ";" + newChocolate.getAddress().getStreet() + ";" + newChocolate.getAddress().getCity() + ";"
					+ newChocolate.getAddress().getPostalNumber());
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

			String filePath = "C:/Users/user_name/Desktop/web/lokacije.txt";
			File file = new File(this.filePath);
			System.out.println(file.getCanonicalPath());

			in = new BufferedReader(new FileReader(file));

			String line, fabrikaId = "", street = "", city = "0", postalNumber = "0";

			double longitude = 0, latitude = 0;
			Address adresa = new Address("", "", "");
			StringTokenizer st;

			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;

				st = new StringTokenizer(line, ";");
				while (st.hasMoreTokens()) {
					fabrikaId = st.nextToken().trim();

					longitude = Double.parseDouble(st.nextToken().trim());
					latitude = Double.parseDouble(st.nextToken().trim());

					street = st.nextToken().trim();
					city = st.nextToken().trim();
					postalNumber = st.nextToken().trim();
					adresa = new Address(street, city, postalNumber);

					lokacije.put(fabrikaId, new Location(fabrikaId, adresa, longitude, latitude));
				}

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

}
