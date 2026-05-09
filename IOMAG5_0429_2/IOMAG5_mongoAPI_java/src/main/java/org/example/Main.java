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

public class Main {
    public static void main(String[] args) {

        String connectionString = "mongodb://localhost:27017";
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            MongoDatabase database = mongoClient.getDatabase("VendeglatasDB");

            MongoCollection<Document> etteremColl = database.getCollection("etterem");
            MongoCollection<Document> szakacsColl = database.getCollection("szakacsok");

            Document e1 = new Document("_id", "e6")
                    .append("nev", "Gundel")
                    .append("cim", new Document("varos", "Budapest")
                            .append("utca", "Gundel Károly")
                            .append("hazszam", "14"))
                    .append("tipus", "Magyaros");

            Document e2 = new Document("_id", "e7")
                    .append("nev", "Trattoria")
                    .append("cim", new Document("varos", "Szeged")
                            .append("utca", "Oskola utca")
                            .append("hazszam", "10"))
                    .append("tipus", "Olasz");

            Document e3 = new Document("_id", "e8")
                    .append("nev", "Sakura")
                    .append("cim", new Document("varos", "Debrecen")
                            .append("utca", "Piac utca")
                            .append("hazszam", "22"))
                    .append("tipus", "Japán");

            etteremColl.insertMany(Arrays.asList(e1, e2, e3));

            String ettermekRaw = Files.readString(Paths.get("ettermek.json"));
            List<Document> ettermekList = Document.parse(ettermekRaw).getList("TempList", Document.class);
            List<Document> formattedEttermek = ettermekList.stream()
                    .map(doc -> {
                        doc.put("_id", doc.getString("ekod"));
                        doc.remove("ekod");
                        return doc;
                    })
                    .collect(Collectors.toList());
            etteremColl.insertMany(formattedEttermek);

            String szakacsokRaw = Files.readString(Paths.get("szakacsok.json"));
            List<Document> szakacsokList = Document.parse(szakacsokRaw).getList("TempList", Document.class);
            List<Document> formattedSzakacsok = szakacsokList.stream()
                    .map(doc -> {
                        doc.put("_id", doc.getString("sz_kod"));
                        doc.remove("sz_kod");
                        if (doc.containsKey("eletkor")) {
                            doc.put("eletkor", Integer.parseInt(doc.getString("eletkor")));
                        }
                        return doc;
                    })
                    .collect(Collectors.toList());
            szakacsColl.insertMany(formattedSzakacsok);

            System.out.println("--- a) Összes étterem lekérdezése ---");
            etteremColl.find().into(new ArrayList<>())
                    .forEach(doc -> System.out.println(doc.toJson()));

            System.out.println("\n--- b) Étterem lekérdezése (ekod: e3) ---");
            Document etteremE3 = etteremColl.find(eq("_id", "e3")).first();
            if (etteremE3 != null) {
                System.out.println(etteremE3.toJson());
            }

            System.out.println("\n--- c) Szakácsok, akik idősebbek 35 évnél ---");
            szakacsColl.find(gt("eletkor", 35)).into(new ArrayList<>())
                    .forEach(doc -> System.out.println(doc.toJson()));

            System.out.println("\n--- d) Szakács (40 éves) és étterme (JOIN) ---");
            szakacsColl.aggregate(Arrays.asList(
                    match(eq("eletkor", 40)),
                    lookup("etterem", "e_sz", "ekod", "etterem_info")
            )).into(new ArrayList<>())
                    .forEach(doc -> System.out.println(doc.toJson()));

            System.out.println("\n--- e) Szakácsok átlagos életkora ---");
            Document atlag = szakacsColl.aggregate(Arrays.asList(
                    group(null, avg("atlagEletkor", "$eletkor"))
            )).first();
            System.out.println("Átlagéletkor: " + atlag.get("atlagEletkor"));

            etteremColl.updateOne(
                    eq("_id", "e2"),
                    new Document("$set", new Document("nev", "Új Étterem Név"))
            );
            etteremColl.deleteOne(eq("_id", "e4"));
            szakacsColl.deleteMany(lt("kor", 35));

        } catch (Exception e) {
            System.err.println("Hiba történt: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
