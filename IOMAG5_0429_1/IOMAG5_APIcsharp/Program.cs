using MongoDB.Driver;
using MongoTest.Models;

class Program
{
    static void Main(string[] args)
    {
        var client = new MongoClient("mongodb://localhost:27017");
        var database = client.GetDatabase("vendeglatas");
        var etteremCollection = database.GetCollection<Etterem>("ettermek");
        var szakacsCollection = database.GetCollection<Szakacs>("szakacsok");

        // --- 1. Feladat: Kiírások ---

        Console.WriteLine("=== ÉTTERMEK ===");
        var ettermek = etteremCollection.Find(_ => true).ToList();
        foreach (var e in ettermek)
        {
            Console.WriteLine("-------");
            Console.WriteLine($"Név: {e.nev}");
            Console.WriteLine($"Város: {e.cim?.varos}");
            Console.WriteLine($"Utca: {e.cim?.utca}");
            Console.WriteLine($"Házszám: {e.cim?.hazszam}");
            Console.WriteLine($"Csillag: {e.csillag}");
            Console.WriteLine($"Specialitások: {string.Join(", ", e.specialitasok)}");
        }

        Console.WriteLine("\n=== SZAKÁCSOK ===");
        var szakacsok = szakacsCollection.Find(_ => true).ToList();
        foreach (var sz in szakacsok)
        {
            Console.WriteLine("-------");
            Console.WriteLine($"Név: {sz.nev}");
            Console.WriteLine($"Életkor: {sz.eletkor}");
            Console.WriteLine($"Részleg: {sz.reszleg}");
            Console.WriteLine($"Étterem: {sz.etterem_nev}");
            Console.WriteLine("Végzettségek:");
            foreach (var v in sz.vegzettsegek)
                Console.WriteLine(" - " + v);
        }

        // --- 2. Feladat: DML ---

        Console.WriteLine("\n=== ÚJ ÉTTEREM BESZÚRÁS ===");
        var ujEtterem = new Etterem
        {
            nev = "Valhalla",
            cim = new Cim
            {
                varos = "Nyíregyháza",
                utca = "Sas",
                hazszam = 3
            },
            csillag = 5,
            specialitasok = new string[] { "viking konyha" }
        };
        etteremCollection.InsertOne(ujEtterem);
        Console.WriteLine("Sikeres beszúrás!");

        Console.WriteLine("\n=== ÚJ SZAKÁCS BESZÚRÁSA ===");
        var ujSzakacs = new Szakacs
        {
            nev = "Hegedűs Lajos",
            eletkor = 25,
            fizetes = 300000,
            reszleg = "hideg konyha",
            vegzettsegek = new List<string> { "Le Cordon Bleu" },
            etterem_nev = "Valhalla"
        };
        szakacsCollection.InsertOne(ujSzakacs);
        Console.WriteLine("Sikeres beszúrás!");

        Console.WriteLine("\n=== CSILLAG MÓDOSÍTÁS ===");
        var valhallaFilter = Builders<Etterem>.Filter.Eq(e => e.nev, "Valhalla");
        var csillagUpdate = Builders<Etterem>.Update.Set(e => e.csillag, 3);
        etteremCollection.UpdateOne(valhallaFilter, csillagUpdate);
        Console.WriteLine("Sikeres módosítás!");

        Console.WriteLine("\n=== 30 ÉLETKOR ALATTI SZAKÁCS TÖRLÉS ===");
        var torlesFilter = Builders<Szakacs>.Filter.Lt(s => s.eletkor, 30);
        szakacsCollection.DeleteMany(torlesFilter);
        Console.WriteLine("Sikeres törlés!");

        // --- 3. Feladat: Lekérdezések ---

        Console.WriteLine("\n=== SZAKÁCS RÉSZLEGEK SZERINT ===");
        var reszlegek = szakacsCollection.Find(_ => true).ToList();
        foreach (var sz in reszlegek)
            Console.WriteLine($"{sz.nev} - {sz.reszleg}");

        Console.WriteLine("\n=== 4+ CSILLAGOS ÉTTERMEK ===");
        var negyPlus = etteremCollection.Find(e => e.csillag >= 4).ToList();
        foreach (var e in negyPlus)
            Console.WriteLine($"Név: {e.nev} - Csillag: {e.csillag}");

        Console.WriteLine("\n=== NYÍREGYHÁZA VAGY 5 CSILLAG ===");
        var nyirFilter =
            Builders<Etterem>.Filter.Eq(e => e.cim.varos, "Nyíregyháza") |
            Builders<Etterem>.Filter.Eq(e => e.csillag, 5);
        var nyirResult = etteremCollection.Find(nyirFilter).ToList();
        foreach (var e in nyirResult)
            Console.WriteLine($"{e.nev} - {e.cim.varos} - {e.csillag}");

        // --- 4. Feladat: Aggregációs pipeline ---

        Console.WriteLine("\n=== VÁROSONKÉNTI ÁTLAG CSILLAG ===");
        var varosAtlag = etteremCollection.Aggregate()
            .Group(e => e.cim.varos, g => new
            {
                Varos = g.Key,
                Darab = g.Count(),
                AtlagCsillag = g.Average(x => x.csillag)
            })
            .ToList();
        foreach (var r in varosAtlag)
            Console.WriteLine($"{r.Varos} - db: {r.Darab} - átlag: {r.AtlagCsillag}");

        Console.WriteLine("\n=== SZAKÁCSOK SZÁMA ÉTTERMENKÉNT ===");
        var szakacsSzam = szakacsCollection.Aggregate()
            .Group(s => s.etterem_nev, g => new
            {
                EtteremNev = g.Key,
                Szam = g.Count()
            })
            .ToList();
        foreach (var r in szakacsSzam)
            Console.WriteLine($"{r.EtteremNev} - {r.Szam} fő");

        Console.WriteLine("\n=== LEGIDŐSEBB SZAKÁCS ÉTTERMENKÉNT ===");
        var legidosebb = szakacsCollection.Aggregate()
            .SortByDescending(s => s.eletkor)
            .Group(s => s.etterem_nev, g => new
            {
                EtteremNev = g.Key,
                LegidosebbNev = g.First().nev,
                Kor = g.First().eletkor
            })
            .ToList();
        foreach (var r in legidosebb)
            Console.WriteLine($"{r.EtteremNev} - {r.LegidosebbNev} ({r.Kor})");

        Console.WriteLine("\n=== ÉTTERMEK ÉS SZAKÁCSOK (LOOKUP) ===");
        var lookup = etteremCollection.Aggregate()
            .Lookup("szakacsok", "nev", "etterem_nev", "szakacsok")
            .ToList();
        foreach (var r in lookup)
        {
            Console.WriteLine($"Étterem: {r["nev"]}");
            var szakacsLista = r["szakacsok"].AsBsonArray;
            foreach (var s in szakacsLista)
                Console.WriteLine($"  Szakács: {s["nev"]}");
        }
    }
}
