using MongoDB.Driver;
using MongoTest.Models;

class Program
{
    static void Main(string[] args)
    {
        var client = new MongoClient("mongodb://localhost:27017");
        var database = client.GetDatabase("konyvtar");
        var konyvCollection = database.GetCollection<Konyv>("konyvek");
        var tagCollection = database.GetCollection<Tag>("tagok");

        // --- 1. Feladat: Kiírások ---

        Console.WriteLine("=== KÖNYVEK ===");
        var konyvek = konyvCollection.Find(_ => true).ToList();
        foreach (var k in konyvek)
        {
            Console.WriteLine("-------");
            Console.WriteLine($"Cím: {k.cim}");
            Console.WriteLine($"Szerző: {k.szerzo}");
            Console.WriteLine($"Kiadó: {k.kiado}");
            Console.WriteLine($"Kiadás éve: {k.kiadas_eve}");
            Console.WriteLine($"Nyelv: {k.nyelv}");
            Console.WriteLine($"Műfajok: {string.Join(", ", k.mufajok)}");
        }

        Console.WriteLine("\n=== TAGOK ===");
        var tagok = tagCollection.Find(_ => true).ToList();
        foreach (var t in tagok)
        {
            Console.WriteLine("-------");
            Console.WriteLine($"Név: {t.nev}");
            Console.WriteLine($"Kor: {t.kor}");
            Console.WriteLine($"Lakcím: {t.lakcim?.iranyitoszam} {t.lakcim?.utca} {t.lakcim?.hazszam}");
            Console.WriteLine($"Kedvenc kiadó: {t.kedvenc_kiado}");
            Console.WriteLine("Érdeklődések:");
            foreach (var e in t.erdeklodesek)
                Console.WriteLine(" - " + e);
        }

        // --- 2. Feladat: DML ---

        Console.WriteLine("\n=== ÚJ KÖNYV BESZÚRÁS ===");
        var ujKonyv = new Konyv
        {
            cim = "A Gyűrűk Ura",
            szerzo = "J.R.R. Tolkien",
            kiado = "Allen & Unwin",
            kiadas_eve = 2001,
            nyelv = "Magyar",
            mufajok = new string[] { "fantasy", "kaland" }
        };
        konyvCollection.InsertOne(ujKonyv);
        Console.WriteLine("Sikeres beszúrás!");

        Console.WriteLine("\n=== ÚJ TAG BESZÚRÁSA ===");
        var ujTag = new Tag
        {
            nev = "Kiss Péter",
            lakcim = new Lakcim
            {
                iranyitoszam = "1011",
                utca = "Fő",
                hazszam = "12"
            },
            email = "kisspeter@teszt.hu",
            telefonszam = "06309876543",
            tagdij = 500,
            kor = 41,
            kedvenc_kiado = "Allen & Unwin",
            erdeklodesek = new List<string> { "fantasy", "történelem" }
        };
        tagCollection.InsertOne(ujTag);
        Console.WriteLine("Sikeres beszúrás!");

        Console.WriteLine("\n=== KIADÁSI ÉV MÓDOSÍTÁS ===");
        var duneFilter = Builders<Konyv>.Filter.Eq(k => k.cim, "Dune");
        var evUpdate = Builders<Konyv>.Update.Set(k => k.kiadas_eve, 2020);
        konyvCollection.UpdateOne(duneFilter, evUpdate);
        Console.WriteLine("Sikeres módosítás!");

        Console.WriteLine("\n=== 2005 ELŐTTI KÖNYVEK TÖRLÉSE ===");
        var torlesFilter = Builders<Konyv>.Filter.Lt(k => k.kiadas_eve, 2005);
        konyvCollection.DeleteMany(torlesFilter);
        Console.WriteLine("Sikeres törlés!");

        // --- 3. Feladat: Lekérdezések ---

        Console.WriteLine("\n=== TAGOK KEDVENC KIADÓ SZERINT ===");
        var tagLista = tagCollection.Find(_ => true).ToList();
        foreach (var t in tagLista)
            Console.WriteLine($"{t.nev} - {t.kedvenc_kiado}");

        Console.WriteLine("\n=== 2011 UTÁNI KÖNYVEK ===");
        var ujabbKonyvek = konyvCollection.Find(k => k.kiadas_eve >= 2011).ToList();
        foreach (var k in ujabbKonyvek)
            Console.WriteLine($"Cím: {k.cim} - Év: {k.kiadas_eve}");

        Console.WriteLine("\n=== PRENTICE HALL VAGY MAGYAR NYELVŰ ===");
        var szuroFilter =
            Builders<Konyv>.Filter.Eq(k => k.kiado, "Prentice Hall") |
            Builders<Konyv>.Filter.Eq(k => k.nyelv, "Magyar");
        var szurtKonyvek = konyvCollection.Find(szuroFilter).ToList();
        foreach (var k in szurtKonyvek)
            Console.WriteLine($"{k.cim} - {k.kiado} - {k.nyelv}");

        // --- 4. Feladat: Aggregációs pipeline ---

        Console.WriteLine("\n=== KIADÓNKÉNTI ÁTLAG KIADÁSI ÉV ===");
        var kiadoAtlag = konyvCollection.Aggregate()
            .Group(k => k.kiado, g => new
            {
                Kiado = g.Key,
                Darab = g.Count(),
                AtlagEv = g.Average(x => x.kiadas_eve)
            })
            .ToList();
        foreach (var r in kiadoAtlag)
            Console.WriteLine($"{r.Kiado} - db: {r.Darab} - átlag év: {r.AtlagEv}");

        Console.WriteLine("\n=== TAGOK SZÁMA KIADÓNKÉNT ===");
        var tagSzam = tagCollection.Aggregate()
            .Group(t => t.kedvenc_kiado, g => new
            {
                Kiado = g.Key,
                Szam = g.Count()
            })
            .ToList();
        foreach (var r in tagSzam)
            Console.WriteLine($"{r.Kiado} - {r.Szam} fő");

        Console.WriteLine("\n=== LEGIDŐSEBB TAG KIADÓNKÉNT ===");
        var legidosebb = tagCollection.Aggregate()
            .SortByDescending(t => t.kor)
            .Group(t => t.kedvenc_kiado, g => new
            {
                Kiado = g.Key,
                LegidosebbNev = g.First().nev,
                Kor = g.First().kor
            })
            .ToList();
        foreach (var r in legidosebb)
            Console.WriteLine($"{r.Kiado} - {r.LegidosebbNev} ({r.Kor})");

        Console.WriteLine("\n=== KÖNYVEK ÉS TAGOK (LOOKUP) ===");
        var lookup = konyvCollection.Aggregate()
            .Lookup("tagok", "kiado", "kedvenc_kiado", "tagok")
            .ToList();
        foreach (var r in lookup)
        {
            Console.WriteLine($"Könyv: {r["cim"]} ({r["kiado"]})");
            var tagLista2 = r["tagok"].AsBsonArray;
            foreach (var t in tagLista2)
                Console.WriteLine($"  Tag: {t["nev"]}");
        }
    }
}
