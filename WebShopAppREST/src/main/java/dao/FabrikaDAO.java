package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import beans.Cokolada;
import beans.Fabrika;
import beans.WorkHour;
import dto.FabrikaDTO;

public class FabrikaDAO {

	private HashMap<String, Fabrika> fabrike = new HashMap<String, Fabrika>();
	private List<Fabrika> fabrikeOtvorenePrvo = new  ArrayList<>();
	
	public FabrikaDAO() {
	}

	public FabrikaDAO(String contextPath) {
		loadFabrike(contextPath);
	}

	private void loadFabrike(String contextPath) {
		BufferedReader in = null;
		try {
			String filePath = "C:/Users/user_name/Desktop/web/fabrike.txt";

			File file = new File(filePath);

			// File file2 = new File(contextPath + "/fabrike.txt");
			System.out.println(file.getCanonicalPath());
			in = new BufferedReader(new FileReader(file));

			String line, id = "", ime = "", slika = "", lokacija = "", pocetakRadnog = "", krajRadnog = "";
			boolean deleted = false, status = false;
			double prosecnaOcena = 10.0;
			// WorkHour radnoVreme = new WorkHour("", "");

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
					status = Boolean.parseBoolean(st.nextToken().trim());
					lokacija = st.nextToken().trim();
					prosecnaOcena = Double.parseDouble(st.nextToken().trim());
					pocetakRadnog = st.nextToken().trim();
					krajRadnog = st.nextToken().trim();

					WorkHour radnoVreme = new WorkHour(pocetakRadnog, krajRadnog);
					slika = st.nextToken().trim();
					fabrike.put(id, new Fabrika(id, deleted, ime, status, lokacija, prosecnaOcena, radnoVreme, slika));

				}
				

				fabrike.values().forEach(f -> f.setStatus(isOpened(f)));
		        fabrikeOtvorenePrvo = fabrike.values().stream()
		                .sorted(Comparator.comparing(Fabrika::isStatus).reversed())
		                .collect(Collectors.toList());

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

	public boolean isOpened(Fabrika f) {
		String openingTime = f.getRadnoVreme().getStartTime();
		String closingTime = f.getRadnoVreme().getEndTime();

		if (openingTime == null || closingTime == null) {
			return false;
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime openTime, closeTime, currentTime;

		try {
			openTime = LocalTime.parse(openingTime, formatter);
			closeTime = LocalTime.parse(closingTime, formatter);
			currentTime = LocalTime.now();
		} catch (DateTimeParseException e) {
			e.printStackTrace();
			return false;
		}

		return !currentTime.isBefore(openTime) && !currentTime.isAfter(closeTime);
	}
	// kreiraj fabriku
	/*
	 * administrator kreira fabriku Prilikom odabira menadžera neophodno je
	 * omogućiti odabir samo menadžera koji nije zadužen ni za jednu fabriku.
	 * Ukoliko takvi ne postoje, omogućiti administratoru da registruje novog
	 * menadžera koji će automatski biti povezan sa novododatom fabrikom.
	 */

	public Collection<Fabrika> findAll() {
		return fabrike.values();
	}

	
	public Collection<Fabrika> findAllByIds(List<String> ids) {
	    return ids.stream()
	              .filter(fabrike::containsKey)
	              .map(fabrike::get)
	              .collect(Collectors.toList());
	}

	public List<Fabrika> fabrikeOtvorenePrvo() {
		return fabrikeOtvorenePrvo;
	}
	// brises fabriku birses i cokolade njene \

	public Fabrika findFabrika(String id) {
		return fabrike.containsKey(id) ? fabrike.get(id) : null;
	}

	public Fabrika save(FabrikaDTO dto) {
		Integer maxId = -1;
		for (String id : fabrike.keySet()) {
			int idNum = Integer.parseInt(id);
			if (idNum > maxId) {
				maxId = idNum;
			}
		}
		maxId++;
		

		Fabrika fabrika = new Fabrika();
		fabrika.setIme(dto.getIme());
		fabrika.setLokacija(dto.getLokacija());
		fabrika.setSlika(dto.getPutanja());
		fabrika.setId(String.valueOf(maxId));
		fabrika.setProsecnaOcena(0);
		fabrika.setDeleted(false);
		fabrika.setRadnoVreme(new WorkHour(dto.getStart(), dto.getEnd()));
		fabrike.put(String.valueOf(fabrika.getId()), fabrika);

		saveNewFabrika(fabrika);
		
		return fabrika;
	}

	public void saveNewFabrika(Fabrika newFabrika) {
		BufferedWriter out = null;
		try {
			File file = new File("C:/Users/user_name/Desktop/web/fabrike.txt");
			System.out.println("Saving to: " + file.getCanonicalPath());

			out = new BufferedWriter(new FileWriter(file, true));
			out.write(newFabrika.getId() + ";" + newFabrika.isDeleted() + ";" + newFabrika.getIme() + ";"
					+ newFabrika.isStatus() + ";" + newFabrika.getLokacija() + ";" + newFabrika.getProsecnaOcena() + ";"
					+ newFabrika.getRadnoVreme().getStartTime() + ";"
					+ newFabrika.getRadnoVreme().getEndTime() + ";" + newFabrika.getSlika());
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

	public Fabrika update(String id, Fabrika updatedFabrika) {
		if (fabrike.containsKey(id)) {
			updatedFabrika.setId(id);
			fabrike.put(id, updatedFabrika);
			rewriteFile();
			return updatedFabrika;
		}
		return null;
	}

	public void delete(String id) {
		Fabrika fabrikaZaBrisanje = findFabrika(id);
		fabrikaZaBrisanje.setDeleted(true);
		update(id, fabrikaZaBrisanje);
		rewriteFile();
	}

	public void rewriteFile() {
		BufferedWriter out = null;
		try {
			File file = new File("C:/Users/user_name/Desktop/web/fabrike.txt");
			out = new BufferedWriter(new FileWriter(file, false));

			for (Fabrika fabrika : fabrike.values()) {
				out.write(fabrika.getId() + ";" + fabrika.isDeleted() + ";" + fabrika.getIme() + ";"
						+ fabrika.isStatus() + ";" + fabrika.getLokacija() + ";" + fabrika.getProsecnaOcena() + ";"
						+ fabrika.getRadnoVreme().getStartTime() + ";"
						+ fabrika.getRadnoVreme().getEndTime() + ";"
						+ fabrika.getSlika());
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
	
	

	public List<Fabrika> searchFabrike(String ime, Double minOcena, Double maxOcena) {
		return fabrike.values().stream().filter(
				user -> (ime == null || user.getIme().toLowerCase().contains(ime.toLowerCase()))
						&& (minOcena == null || user.matchesMin(minOcena)) && (maxOcena == null || user.matchesMax(maxOcena)))
				.collect(Collectors.toList());
	}




	public List<Fabrika> showOnlyOpenFactories() {
		return fabrike.values().stream().filter(Fabrika::isStatus).collect(Collectors.toList());
	}
}
