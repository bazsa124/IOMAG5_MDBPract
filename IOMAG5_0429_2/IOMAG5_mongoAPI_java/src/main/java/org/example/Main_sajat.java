package org.example;

import com.mongodb.client.*;
import org.bson.Document;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Accumulators.avg;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.*;

public class Main_sajat {
    public static void main(String[] args) {

        String connectionString = "mongodb://localhost:27017";
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            MongoDatabase database = mongoClient.getDatabase("KonyvtarDB");

            MongoCollection<Document> konyvColl = database.getCollection("konyv");
            MongoCollection<Document> tagColl = database.getCollection("tagok");

            // --- 2a) 3 könyv kézi beszúrása (_id > k3) ---

            Document k4 = new Document("_id", "k4")
                    .append("cim", "A Gyűrűk Ura")
                    .append("szerzo", "J.R.R. Tolkien")
                    .append("kiado", "Allen & Unwin")
                    .append("kiadas_eve", 2001)
                    .append("nyelv", "Magyar")
                    .append("mufajok", Arrays.asList("fantasy", "kaland"));

            Document k5 = new Document("_id", "k5")
                    .append("cim", "1984")
                    .append("szerzo", "George Orwell")
                    .append("kiado", "Secker & Warburg")
                    .append("kiadas_eve", 1949)
                    .append("nyelv", "Angol")
                    .append("mufajok", Arrays.asList("disztópia", "sci-fi"));

            Document k6 = new Document("_id", "k6")
                    .append("cim", "Egri csillagok")
                    .append("szerzo", "Gárdonyi Géza")
                    .append("kiado", "Dante Kiadó")
                    .append("kiadas_eve", 1901)
                    .append("nyelv", "Magyar")
                    .append("mufajok", Arrays.asList("történelmi", "kaland"));

            konyvColl.insertMany(Arrays.asList(k4, k5, k6));

            // --- 2b) JSON fájlok beolvasása ---

            String konyvekRaw = Files.readString(Paths.get("konyvek_11.json"));
            List<Document> konyvekList = Document.parse(konyvekRaw).getList("TempList", Document.class);
            List<Document> formattedKonyvek = konyvekList.stream()
                    .map(doc -> {
                        doc.put("_id", doc.getString("kkod"));
                        doc.remove("kkod");
                        if (doc.containsKey("kiadas_eve")) {
                            doc.put("kiadas_eve", Integer.parseInt(doc.getString("kiadas_eve")));
                        }
                        return doc;
                    })
                    .collect(Collectors.toList());
            konyvColl.insertMany(formattedKonyvek);

            String tagokRaw = Files.readString(Paths.get("tagok_11.json"));
            List<Document> tagokList = Document.parse(tagokRaw).getList("TempList", Document.class);
            List<Document> formattedTagok = tagokList.stream()
                    .map(doc -> {
                        doc.put("_id", doc.getString("tkod"));
                        doc.remove("tkod");
                        if (doc.containsKey("kor")) {
                            doc.put("kor", Integer.parseInt(doc.getString("kor")));
                        }
                        if (doc.containsKey("tagdij")) {
                            doc.put("tagdij", Integer.parseInt(doc.getString("tagdij")));
                        }
                        return doc;
                    })
                    .collect(Collectors.toList());
            tagColl.insertMany(formattedTagok);

            // --- 3a) Összes könyv lekérdezése ---

            System.out.println("--- a) Összes könyv lekérdezése ---");
            konyvColl.find().into(new ArrayList<>())
                    .forEach(doc -> System.out.println(doc.toJson()));

            // --- 3b) Könyv lekérdezése (_id: k3) ---

            System.out.println("\n--- b) Könyv lekérdezése (kkod: k3) ---");
            Document konyvK3 = konyvColl.find(eq("_id", "k3")).first();
            if (konyvK3 != null) {
                System.out.println(konyvK3.toJson());
            }

            // --- 3c) Tagok, akik idősebbek 35 évnél ---

            System.out.println("\n--- c) Tagok, akik idősebbek 35 évnél ---");
            tagColl.find(gt("kor", 35)).into(new ArrayList<>())
                    .forEach(doc -> System.out.println(doc.toJson()));

            // --- 3d) 41 éves tag és kedvenc könyvei (JOIN) ---

            System.out.println("\n--- d) Tag (41 éves) és kedvenc kiadójának könyvei (JOIN) ---");
            tagColl.aggregate(Arrays.asList(
                    match(eq("kor", 41)),
                    lookup("konyv", "kedvenc_kiado", "kiado", "konyv_info")
            )).into(new ArrayList<>())
                    .forEach(doc -> System.out.println(doc.toJson()));

            // --- 3e) Tagok átlagos életkora ---

            System.out.println("\n--- e) Tagok átlagos életkora ---");
            Document atlag = tagColl.aggregate(Arrays.asList(
                    group(null, avg("atlagKor", "$kor"))
            )).first();
            System.out.println("Átlagéletkor: " + atlag.get("atlagKor"));

            // --- 4a) k2 könyv címének módosítása ---

            konyvColl.updateOne(
                    eq("_id", "k2"),
                    new Document("$set", new Document("cim", "A Tűz és Jég Dala"))
            );

            // --- 4b) k4 könyv törlése ---

            konyvColl.deleteOne(eq("_id", "k4"));

            // --- 4c) 30 év alatti tagok törlése ---

            tagColl.deleteMany(lt("kor", 30));

        } catch (Exception e) {
            System.err.println("Hiba történt: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
