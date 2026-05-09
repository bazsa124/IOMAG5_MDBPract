package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Main_sajat {
    public static void main(String[] args) throws IOException {
        ObjectMapper m = new ObjectMapper();
        JsonNode root = m.readTree(new File("src/main/JSON_sajat.json"));
        JsonNode konyvtar = root.get("konyvtar");
        JsonNode tagok = konyvtar.get("tag");
        JsonNode szamitogepek = konyvtar.get("szamitogep");
        JsonNode konyvek = konyvtar.get("konyv");
        JsonNode filmek = konyvtar.get("film");
        JsonNode albumok = konyvtar.get("album");
        JsonNode kKolcsonzesek = konyvtar.get("k_kolcsonzes");
        JsonNode fKolcsonzesek = konyvtar.get("f_kolcsonzes");
        JsonNode aKolcsonzesek = konyvtar.get("a_kolcsonzes");
        JsonNode hasznalatok = konyvtar.get("hasznalat");

        // 0. feladat - JSON Schema validáció
        JsonNode schemaNode = m.readTree(new File("src/main/JSON_SCHEMA_sajat.json"));
        JsonSchema schema = JsonSchemaFactory
                .getInstance(SpecVersion.VersionFlag.V4).getSchema(schemaNode);
        Set<ValidationMessage> errors = schema.validate(root);
        if (errors.isEmpty()) {
            System.out.println("Valid JSON");
        } else {
            System.out.println("Hibás JSON:");
            errors.forEach(e -> System.out.println(e.getMessage()));
        }

        // 1. feladat - Könyvek adatai (Egyszerű iteráció)
        System.out.println("=== Könyvek adatai ===");
        if (konyvek != null && konyvek.isArray()) {
            for (JsonNode konyv : konyvek) {
                String cim = konyv.get("cim").asText();
                String szerzo = konyv.get("szerzo").asText();
                String kiado = konyv.get("kiado").asText();
                System.out.println("Cím: " + cim + " | Szerző: " + szerzo + " | Kiadó: " + kiado);
            }
        }

        // 2. feladat - Tagok és használt számítógépeik (Beágyazott keresés)
        System.out.println("\n=== TAGOK ÉS SZÁMÍTÓGÉPEIK ===");
        for (JsonNode tag : tagok) {
            String tKod = tag.get("_tkod").asText();
            String tNev = tag.get("nev").asText();
            System.out.println("\nTag: " + tNev + " [" + tKod + "]");
            System.out.println("---------------------------");
            for (JsonNode h : hasznalatok) {
                if (h.get("_tkod").asText().equals(tKod)) {
                    String szKod = h.get("_szkod").asText();
                    for (JsonNode sz : szamitogepek) {
                        if (sz.get("_szkod").asText().equals(szKod)) {
                            System.out.println(" - " + sz.get("operacios_rendszer").asText() +
                                    " (" + sz.get("tarhely").asText() + ") - " +
                                    h.get("hasznalat_ideje").asText() + " perc");
                        }
                    }
                }
            }
        }

        // 3. feladat - Átlagos használati idő (Aggregáció)
        System.out.println("\n=== Átlagos használati idő ===");
        double osszeg = 0;
        int db = 0;
        for (JsonNode h : hasznalatok) {
            osszeg += h.get("hasznalat_ideje").asDouble();
            db++;
        }
        System.out.println("AVG: " + (osszeg / db) + " perc");

        // 4. feladat - 2010 után kiadott könyvek (Egyszerű szűrés)
        System.out.println("\n=== 2010 után kiadott könyvek ===");
        for (JsonNode k : konyvek) {
            String datum = k.get("kiadas_datuma").asText();
            int ev = Integer.parseInt(datum.substring(0, 4));
            if (ev > 2010) {
                System.out.println(" - " + k.get("cim").asText() + " (" + ev + ")");
            }
        }

        // 5. feladat - Ki, mit kölcsönzött? (JOIN)
        System.out.println("\n=== Ki, mit kölcsönzött? (JOIN) ===");
        for (JsonNode kk : kKolcsonzesek) {
            String tKod = kk.get("_tkod").asText();
            String kKod = kk.get("_kkod").asText();
            String tNev = "";
            for (JsonNode t : tagok)
                if (t.get("_tkod").asText().equals(tKod)) tNev = t.get("nev").asText();
            String kCim = "";
            for (JsonNode k : konyvek)
                if (k.get("_kkod").asText().equals(kKod)) kCim = k.get("cim").asText();
            System.out.println(tNev + " kölcsönözte: " + kCim +
                    " (" + kk.get("kezdet").asText() + " - " + kk.get("veg").asText() + ")");
        }
        for (JsonNode fk : fKolcsonzesek) {
            String tKod = fk.get("_tkod").asText();
            String fKod = fk.get("_fkod").asText();
            String tNev = "";
            for (JsonNode t : tagok)
                if (t.get("_tkod").asText().equals(tKod)) tNev = t.get("nev").asText();
            String fCim = "";
            for (JsonNode f : filmek)
                if (f.get("_fkod").asText().equals(fKod)) fCim = f.get("cim").asText();
            System.out.println(tNev + " kölcsönözte: " + fCim +
                    " (" + fk.get("kezdet").asText() + " - " + fk.get("veg").asText() + ")");
        }

        // 6. feladat - JSON adatok manipulációja
        System.out.println("\n=== JSON adatok manipulációja ===");
        for (JsonNode k : konyvek) {
            ObjectNode obj = (ObjectNode) k;
            obj.put("elerheto", true);
            obj.remove("kiadas_datuma");
        }
        System.out.println("Könyvek módosítva (elerheto hozzáadva, kiadas_datuma törölve):");
        for (JsonNode k : konyvek) {
            System.out.println(" - " + k.get("cim").asText() + " | elérhető: " + k.get("elerheto").asBoolean());
        }

        // 7. feladat - Legtöbb kölcsönzéssel rendelkező tag (VIP)
        System.out.println("\n=== VIP Tag (Legtöbb kölcsönzés) ===");
        Map<String, Integer> kolcsonzesek = new HashMap<>();
        for (JsonNode kk : kKolcsonzesek) {
            String tKod = kk.get("_tkod").asText();
            kolcsonzesek.put(tKod, kolcsonzesek.getOrDefault(tKod, 0) + 1);
        }
        for (JsonNode fk : fKolcsonzesek) {
            String tKod = fk.get("_tkod").asText();
            kolcsonzesek.put(tKod, kolcsonzesek.getOrDefault(tKod, 0) + 1);
        }
        for (JsonNode ak : aKolcsonzesek) {
            String tKod = ak.get("_tkod").asText();
            kolcsonzesek.put(tKod, kolcsonzesek.getOrDefault(tKod, 0) + 1);
        }
        String maxTkod = "";
        int maxDb = 0;
        for (Map.Entry<String, Integer> entry : kolcsonzesek.entrySet()) {
            if (entry.getValue() > maxDb) {
                maxDb = entry.getValue();
                maxTkod = entry.getKey();
            }
        }
        String vipNev = "";
        for (JsonNode t : tagok) {
            if (t.get("_tkod").asText().equals(maxTkod)) {
                vipNev = t.get("nev").asText();
                break;
            }
        }
        System.out.println("VIP tag: " + vipNev + " (Összes kölcsönzés: " + maxDb + " db)");

        // 8. feladat - Új JSON fájl készítése és mentése
        System.out.println("\n=== Új JSON fájl készítése és mentése ===");
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode ujLista = mapper.createArrayNode();
        for (JsonNode tag : tagok) {
            String tKod = tag.get("_tkod").asText();
            int kolcsonzesSzam = 0;
            for (JsonNode kk : kKolcsonzesek)
                if (kk.get("_tkod").asText().equals(tKod)) kolcsonzesSzam++;
            for (JsonNode fk : fKolcsonzesek)
                if (fk.get("_tkod").asText().equals(tKod)) kolcsonzesSzam++;
            for (JsonNode ak : aKolcsonzesek)
                if (ak.get("_tkod").asText().equals(tKod)) kolcsonzesSzam++;

            ObjectNode csomopont = mapper.createObjectNode();
            csomopont.put("tag_nev", tag.get("nev").asText());
            csomopont.put("osszes_kolcsonzes", kolcsonzesSzam);
            ujLista.add(csomopont);
        }
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File("uj_kolcsonzesek.json"), ujLista);
        System.out.println("Fájl kiírva: uj_kolcsonzesek.json");
    }
}
