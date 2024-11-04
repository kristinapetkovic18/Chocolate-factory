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
import java.util.StringTokenizer;

import beans.Kupovina;
import beans.KupovinaItem;

public class KupovinaItemDAO {

    private HashMap<String, KupovinaItem> kupovinaItems = new HashMap<>();
    private String filePath = "C:/Users/user_name/Desktop/web/artikliKupovine.txt";

    public KupovinaItemDAO() {
        loadItems();
    }

    public KupovinaItemDAO(String contextPath) {
        this();
    }

    public Collection<KupovinaItem> findAll() {
        return kupovinaItems.values();
    }

    public KupovinaItem getById(String id) {
        return kupovinaItems.get(id);
    }

    public ArrayList<KupovinaItem> getByKupovinaId(String kupovinaId) {
        ArrayList<KupovinaItem> foundItems = new ArrayList<>();
        for (KupovinaItem item : kupovinaItems.values()) {
            if (item.getKupovinaId().equals(kupovinaId)) {
                foundItems.add(item);
            }
        }
        return foundItems;
    }

    public KupovinaItem save(KupovinaItem item) {
        int maxId = kupovinaItems.keySet().stream()
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(-1) + 1;

        item.setId(String.valueOf(maxId));
        kupovinaItems.put(item.getId(), item);
        saveNewItem(item);
        return item;
    }

    private void saveNewItem(KupovinaItem newItem) {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(filePath, true))) {
            out.write(formatItem(newItem));
            out.newLine();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadItems() {
        try (BufferedReader in = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                KupovinaItem item = parseItem(line);
                kupovinaItems.put(item.getId(), item);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public KupovinaItem update(String id, KupovinaItem updatedItem) {
        if (kupovinaItems.containsKey(id)) {
            updatedItem.setId(id);
            kupovinaItems.put(id, updatedItem);
            rewriteFile();
            return updatedItem;
        }
        return null;
    }

    public void delete(String id) {
        KupovinaItem itemToDelete = getById(id);
        if (itemToDelete != null) {
          //  itemToDelete.setDeleted(true);
            update(id, itemToDelete);
        }
    }

    private void rewriteFile() {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(filePath, false))) {
            for (KupovinaItem item : kupovinaItems.values()) {
                out.write(formatItem(item));
                out.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String formatItem(KupovinaItem item) {
        return String.join(";", item.getId(), item.getKupovinaId(), item.getCokoladaId(), 
        		item.getCokoladaIme(),String.valueOf(item.getKolicina()), item.getFabrikaId(), item.getFabrikaIme());
    }

    private KupovinaItem parseItem(String line) {
        StringTokenizer st = new StringTokenizer(line, ";");
        String id = st.nextToken().trim();
        String kupovinaId = st.nextToken().trim();
        String cokoladaId = st.nextToken().trim();
        String cokoladaIme = st.nextToken().trim();
        int kolicina = Integer.parseInt(st.nextToken().trim());
        String fabrikaId = st.nextToken().trim();
        String fabrikaIme = st.nextToken().trim();
        return new KupovinaItem(id, kupovinaId, cokoladaId,cokoladaIme, kolicina, fabrikaId, fabrikaIme);
    }

	public Collection<KupovinaItem> findAllByKupovinaId(String kupovinaId) {

		ArrayList<KupovinaItem> pronadjeneKupovine = new ArrayList<>();
		for (KupovinaItem c : this.kupovinaItems.values()) {
			if (c.getKupovinaId().equals(kupovinaId)) {
				pronadjeneKupovine.add(c);
			}
		}

		return pronadjeneKupovine;

	}
}
