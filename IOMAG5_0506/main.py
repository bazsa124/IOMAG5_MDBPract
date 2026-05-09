import pymongo as mongo

# --- Kapcsolódás az adatbázishoz ---
client = mongo.MongoClient("mongodb://localhost:27017")
db = client["vendeglatas"]

etterem_coll = db["etterem"]
foszakacs_coll = db["foszakacs"]

# --- Korábbi adatok törlése ---
etterem_coll.delete_many({})
foszakacs_coll.delete_many({})

# --- 1. Adatok feltöltése ---

ettermek_adatok = [
    {
        "_id": "e1",
        "nev": "Aranyhal Étterem",
        "cim": {
            "varos": "Miskolc",
            "utca": "Széchenyi u.",
            "hazszam": 107
        },
        "csillag": 3
    },
    {
        "_id": "e2",
        "nev": "Kovács Dániel very good lokin restaurant",
        "cim": {
            "varos": "Kassa",
            "utca": "Petőfi Antal köz",
            "hazszam": 408
        },
        "csillag": 4
    },
    {
        "_id": "e3",
        "nev": "Creppy Palacsintaház Étterem",
        "cim": {
            "varos": "Miskolc",
            "utca": "Méylvölgy utca",
            "hazszam": 15
        },
        "csillag": 5
    }
]

foszakacsok_adatok = [
    {
        "_id": "f1",
        "e_f": "e1",
        "nev": "Kovács Dániel",
        "eletkor": 45,
        "vegzettseg": ["Szakközépiskola", "Főiskola"]
    },
    {
        "_id": "f2",
        "e_f": "e2",
        "nev": "Kovács János",
        "eletkor": 38,
        "vegzettseg": ["Szakközépiskola", "Főiskola"]
    },
    {
        "_id": "f3",
        "e_f": "e3",
        "nev": "Nemes Géza",
        "eletkor": 28,
        "vegzettseg": ["Főiskola"]
    }
]

foszakacs_coll.insert_many(foszakacsok_adatok)
etterem_coll.insert_many(ettermek_adatok)
print("Főszakácsok feltöltve.")

# --- 2a. Összes étterem lekérdezése ---

for etterem in etterem_coll.find():
    print(etterem)

print("------------------------------------------------------------")

# --- 2b. Összes főszakács lekérdezése ---

for foszakacs in foszakacs_coll.find():
    print(foszakacs)

print("------------------------------------------------------------")

# --- 2c. Egy étterem lekérdezése ID alapján (e1) ---

egyetlen_etterem = etterem_coll.find_one({"_id": "e1"})
print(egyetlen_etterem)

print("------------------------------------------------------------")

# --- 2d. Éttermek szűrése: csillagszám <= 4 ---

for etterem in etterem_coll.find({"csillag": {"$lte": 4}}):
    print(etterem)

print("-------------------------------------------------------------")

# --- 2e. Főszakácsok átlagos életkora (aggregáció) ---

pipeline_avg = [
    {
        "$group": {
            "_id": None,
            "atlagEletkor": {"$avg": "$eletkor"}
        }
    }
]
atlag_eredmeny = list(foszakacs_coll.aggregate(pipeline_avg))
atlag = atlag_eredmeny[0]['atlagEletkor']
print(f"A főszakácsok átlagos életkora: {atlag:.2f} év")

print("--------------------------------------------------------")

# --- 2f. Szakközépiskolás főszakácsok és éttermük (lookup/JOIN) ---

pipeline_lookup = [
    {
        "$match": {
            "vegzettseg": "Szakközépiskola"
        }
    },
    {
        "$lookup": {
            "from": "etterem",
            "localField": "e_f",
            "foreignField": "_id",
            "as": "etterem_adatok"
        }
    }
]

for doc in foszakacs_coll.aggregate(pipeline_lookup):
    etterem_nev = doc['etterem_adatok'][0]['nev'] if doc['etterem_adatok'] else "Nincs adat"
    print(f"Főszakács: {doc['nev']}----------------- Étterem: {etterem_nev}")

# --- 3a. Módosítás: e1-es étterem csillagszámának frissítése ---

print("---------------------------Update.-------------------------------")
print("\n--- 3.a) e1-es ID étterem módosítása ---")
etterem_coll.update_one(
    {"_id": "e1"},
    {"$set": {"csillag": 4}}
)

for etterem in etterem_coll.find():
    print(etterem)

print("--------------------------------------------------------")

# --- 3b. Törlés: f2-es főszakács törlése ---

foszakacs_coll.delete_one({"_id": "f2"})
print("f2-es főszakács törölve:")
for foszakacs in foszakacs_coll.find():
    print(foszakacs)

print("--------------------------------------------------------")

# --- 3c. Törlés: 30 év alatti főszakácsok törlése ---

torles_eredmeny = foszakacs_coll.delete_many(
    {"eletkor": {"$lt": 30}}
)
print(f"Törölt: {torles_eredmeny.deleted_count}")
for foszakacs in foszakacs_coll.find():
    print(foszakacs)
