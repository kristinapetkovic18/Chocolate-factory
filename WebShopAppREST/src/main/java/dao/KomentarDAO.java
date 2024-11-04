	
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
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import beans.Komentar;
import enums.Status;

public class KomentarDAO {


    private Map<String, Komentar> komentari = new HashMap<>();
    private String filePath = "C:/Users/user_name/Desktop/web/komentari.txt";

    public KomentarDAO() {
        loadKomentari();
    }

    public Collection<Komentar> findAll() {
        return komentari.values();
    }

    public Komentar getById(String id) {
        return komentari.get(id);
    }

    public Komentar save(Komentar komentar) {
    	Integer maxId = -1;
		for (String id : komentari.keySet()) {
			int idNum = Integer.parseInt(id);
			if (idNum > maxId) {
				maxId = idNum;
			}
		}
		maxId++;
		komentar.setId(String.valueOf(maxId));
        komentari.put(komentar.getId(), komentar);
        saveToFile(komentar);
        return komentar;
    }

    private void saveToFile(Komentar komentar) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(formatKomentarForFile(komentar));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadKomentari() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) continue;
                Komentar komentar = parseKomentarFromLine(line);
                komentari.put(komentar.getId(), komentar);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Komentar update(String id, Komentar updatedKomentar) {
        if (komentari.containsKey(id)) {
            komentari.put(id, updatedKomentar);
            rewriteFile();
            return updatedKomentar;
        }
        return null;
    }

    public void delete(String id) {
        if (komentari.remove(id) != null) {
            rewriteFile();
        }
    }

    private void rewriteFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            for (Komentar komentar : komentari.values()) {
                writer.write(formatKomentarForFile(komentar));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Collection<Komentar> getByFabrikaId(String id) {
        return komentari.values().stream()
                .filter(k -> k.getFabrikaId().equals(id))
                .collect(Collectors.toList());
    }

    public Collection<Komentar> getByFabrikaIdOdobrene(String id) {
        return komentari.values().stream()
                .filter(k -> k.getFabrikaId().equals(id) && k.getStatus() == Status.Odobreno)
                .collect(Collectors.toList());
    }

    private String formatKomentarForFile(Komentar komentar) {
        return String.join(";",
                komentar.getId(),
                komentar.getStatus().toString(),
                komentar.getKupacId(),
                komentar.getKupacUsername(),
                komentar.getFabrikaId(),
                komentar.getTekst(),
                String.valueOf(komentar.getOcena())
        );
    }

    private Komentar parseKomentarFromLine(String line) {
        String[] parts = line.split(";");
        return new Komentar(
                parts[0],
                Status.valueOf(parts[1]),
                parts[2],
                parts[3],
                parts[4],
                parts[5],
                Integer.parseInt(parts[6])
        );
    }

  
}
