import pymongo as mongo

client = mongo.MongoClient("mongodb://localhost:27017")
db = client["konyvtar"]

konyv_coll = db["konyv"]
tag_coll = db["tagok"]

konyv_coll.delete_many({})
tag_coll.delete_many({})

# --- 1. Adatok feltöltése ---

konyvek_adatok = [
    {
        "_id": "k1",
        "cim": "Clean Code",
        "szerzo": "Robert C. Martin",
        "kiado": "Prentice Hall",
        "kiadas_eve": 2012,
        "nyelv": "Angol",
        "mufajok": ["programozás", "szoftverfejlesztés"]
    },
    {
        "_id": "k2",
        "cim": "Songs of Fire and Ice",
        "szerzo": "George R.R. Martin",
        "kiado": "Teszt Kiadó",
        "kiadas_eve": 2010,
        "nyelv": "Angol",
        "mufajok": ["fantasy", "kaland"]
    },
    {
        "_id": "k3",
        "cim": "Dune",
        "szerzo": "Frank Herbert",
        "kiado": "Teszt Kiadó",
        "kiadas_eve": 2013,
        "nyelv": "Angol",
        "mufajok": ["sci-fi", "kaland"]
    }
]

tagok_adatok = [
    {
        "_id": "t1",
        "nev": "Teszt Elek",
        "lakcim": {
            "iranyitoszam": "5600",
            "utca": "Alma",
            "hazszam": "1"
        },
        "kor": 28,
        "kedvenc_kiado": "Prentice Hall",
        "erdeklodesek": ["programozás", "sci-fi"]
    },
    {
        "_id": "t2",
        "nev": "Teszt Tímea",
        "lakcim": {
            "iranyitoszam": "3300",
            "utca": "Körte",
            "hazszam": "1"
        },
        "kor": 35,
        "kedvenc_kiado": "Teszt Kiadó",
        "erdeklodesek": ["fantasy", "történelem"]
    },
    {
        "_id": "t3",
        "nev": "Kiss Péter",
        "lakcim": {
            "iranyitoszam": "1011",
            "utca": "Fő",
            "hazszam": "12"
        },
        "kor": 41,
        "kedvenc_kiado": "Prentice Hall",
        "erdeklodesek": ["fantasy", "történelem", "sci-fi"]
    }
]

konyv_coll.insert_many(konyvek_adatok)
tag_coll.insert_many(tagok_adatok)
print("Adatok feltöltve.")

# --- 2. Összes könyv kiírása ---

for konyv in konyv_coll.find():
    print(konyv)

print("------------------------------------------------------------")

# --- 3. Összes tag kiírása ---

for tag in tag_coll.find():
    print(tag)

print("------------------------------------------------------------")

# --- 4. Egy könyv lekérdezése ID alapján ---

konyv_k1 = konyv_coll.find_one({"_id": "k1"})
print(konyv_k1)

print("------------------------------------------------------------")

# --- 5. 2011 után kiadott könyvek szűrése ---

for konyv in konyv_coll.find({"kiadas_eve": {"$gte": 2011}}):
    print(konyv)

print("-------------------------------------------------------------")

# --- 6. Tagok átlagos életkora (aggregáció) ---

pipeline_avg = [
    {
        "$group": {
            "_id": None,
            "atlagKor": {"$avg": "$kor"}
        }
    }
]
atlag_eredmeny = list(tag_coll.aggregate(pipeline_avg))
atlag = atlag_eredmeny[0]['atlagKor']
print(f"A tagok átlagos életkora: {atlag:.2f} év")

print("--------------------------------------------------------")

# --- 7. Tagok szűrése érdeklődés alapján + könyveik (lookup/JOIN) ---

pipeline = [
    {
        "$match": {
            "erdeklodesek": "programozás"
        }
    },
    {
        "$lookup": {
            "from": "konyv",
            "localField": "kedvenc_kiado",
            "foreignField": "kiado",
            "as": "konyv_adatok"
        }
    }
]

for doc in tag_coll.aggregate(pipeline):
    konyvek_str = ", ".join([k['cim'] for k in doc['konyv_adatok']]) if doc['konyv_adatok'] else "Nincs adat"
    print(f"Tag: {doc['nev']}----------------- Könyvek: {konyvek_str}")

# --- 8. Módosítás: k1-es könyv kiadási évének frissítése ---

print("---------------------------Update.-------------------------------")
print("\n--- k1-es ID könyv módosítása ---")
konyv_coll.update_one(
    {"_id": "k1"},
    {"$set": {"kiadas_eve": 2020}}
)

for konyv in konyv_coll.find():
    print(konyv)

print("--------------------------------------------------------")

# --- 9. Egy tag törlése (t2) ---

tag_coll.delete_one({"_id": "t2"})
print("t2-es tag törölve:")
for t in tag_coll.find():
    print(t)

print("--------------------------------------------------------")

# --- 10. 30 év alatti tagok törlése ---

torles_eredmeny = tag_coll.delete_many(
    {"kor": {"$lt": 30}}
)
print(f"Törölt: {torles_eredmeny.deleted_count}")
for t in tag_coll.find():
    print(t)
