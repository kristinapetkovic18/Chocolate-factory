package dao;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import beans.Kupovina;
import enums.Status;

public class KupovinaDAO {

    private Map<String, Kupovina> kupovine = new HashMap<>();
    private String filePath = "C:/Users/user_name/Desktop/web/kupovine.txt";

    public KupovinaDAO(String contextPath) {
        loadKupovine();
    }

    public Collection<Kupovina> findAll() {
        return kupovine.values();
    }

    public Kupovina getById(String id) {
        return kupovine.get(id);
    }

    public Kupovina save(Kupovina kupovina) {
        kupovina.setId(generateNewId());
        kupovine.put(kupovina.getId(), kupovina);
        saveToFile(kupovina);
        return kupovina;
    }

    private String generateNewId() {
        return String.format("%010d", kupovine.size() + 1);
    }

    private void saveToFile(Kupovina kupovina) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(formatKupovinaForFile(kupovina));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadKupovine() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) continue;
                Kupovina kupovina = parseKupovinaFromLine(line);
                kupovine.put(kupovina.getId(), kupovina);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Kupovina update(String id, Kupovina updatedKupovina) {
        if (kupovine.containsKey(id)) {
            updatedKupovina.setId(id);
            kupovine.put(id, updatedKupovina);
            rewriteFile();
            return updatedKupovina;
        }
        return null;
    }

    public void delete(String id) {
        if (kupovine.remove(id) != null) {
            rewriteFile();
        }
    }

    private void rewriteFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            for (Kupovina kupovina : kupovine.values()) {
                writer.write(formatKupovinaForFile(kupovina));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Collection<Kupovina> findAllByKupacId(String kupacId) {
        return kupovine.values().stream()
                .filter(k -> k.getKupacId().equals(kupacId))
                .collect(Collectors.toList());
    }

    public Collection<Kupovina> findAllByFabrikaId(String menadzerId) {
    	 return kupovine.values().stream()
                 .filter(k -> k.getFabrikaId().equals(menadzerId))
                 .collect(Collectors.toList());
    }

    private String formatKupovinaForFile(Kupovina kupovina) {
        return String.join(";",
                kupovina.getId(),
                kupovina.getKupacId(),
                kupovina.getImeIprezimeKupca(),
                String.valueOf(kupovina.getCena()),
                kupovina.getVremeKupovine().toString(),
                kupovina.getStatus().toString(),
                kupovina.getPorukaMenadzera(),
                kupovina.getFabrikaId()
        );
    }


    public Collection<Kupovina> findAllByCenaAndDatum(double minCena, double maxCena, LocalDate startDatum, LocalDate endDatum) {
        return kupovine.values().stream()
                .filter(k -> k.getCena() >= minCena && k.getCena() <= maxCena)
                .filter(k -> !k.getVremeKupovine().isBefore(startDatum) && !k.getVremeKupovine().isAfter(endDatum))
                .collect(Collectors.toList());
    }
    
    
    private Kupovina parseKupovinaFromLine(String line) {
        String[] parts = line.split(";");
        return new Kupovina(
                parts[0],
                parts[1],
                parts[2],
                Double.parseDouble(parts[3]),
                LocalDate.parse(parts[4]),
                Status.valueOf(parts[5]),
                parts[6],
                parts[7]
        );
    }
}