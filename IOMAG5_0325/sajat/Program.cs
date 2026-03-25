using System.Xml.Linq;

// Task 1
XDocument dokumentum = XDocument.Load("IOMAG5_XML_sajat.xml");
XElement gyoker = dokumentum.Descendants("konyvtar").First();

Console.WriteLine("(0.) A teljes dokumentum:\n");
Console.WriteLine(gyoker);

Console.WriteLine("\n(1.) Az angol nyelvű könyvek:\n");
var angolKonyvek = gyoker.Descendants("konyv")
    .Where(elem => elem.Descendants("nyelv").First().Value == "Angol")
    .ToList();

angolKonyvek.ForEach(elem =>
    Console.WriteLine(" - " + elem.Descendants("cim").First().Value
                      + " (" + elem.Descendants("szerzo").First().Value + ")")
);

// Task 2
Console.WriteLine("\n(2.) Melyik tag, melyik könyvet kölcsönözte, mikor:\n");

var haromasJoin = gyoker.Descendants("k_kolcsonzes")
    .Select(elem =>
    {
        var tagID = elem.Attribute("kk_t").Value;
        var tag = gyoker.Descendants("tag")
            .Where(t => t.Attribute("tKod").Value == tagID)
            .First()
            .Descendants("nev")
            .FirstOrDefault().Value;

        var konyvID = elem.Attribute("kk_k").Value;
        var konyv = gyoker.Descendants("konyv")
            .First(k => k.Attribute("kKod").Value == konyvID)
            .Descendants("cim")
            .FirstOrDefault().Value;

        var kezdet = elem.Descendants("kezdet").First().Value;
        var veg    = elem.Descendants("veg").First().Value;

        return new
        {
            Tag    = tag,
            Konyv  = konyv,
            Kezdet = kezdet,
            Veg    = veg
        };
    })
    .ToList();

haromasJoin.ForEach(join => Console.WriteLine(join));

// Task 4
var atlagHasznalat = gyoker.Descendants("hasznalat")
    .Select(h => h.Descendants("hasznalat_ideje").First().Value)
    .Average(ertek => double.Parse(ertek));

Console.WriteLine($"\n(3.) Az átlagos számítógép-használati idő: {atlagHasznalat} perc");

// Task 5
Console.WriteLine("\n(4.) Az igénylések beszerzési árát megduplázom, majd elmentem:\n");

gyoker.Descendants("igenyles")
    .ToList()
    .ForEach(igenyles =>
    {
        var arElem = igenyles.Descendants("beszer_ar").First();
        arElem.Value = (double.Parse(arElem.Value) * 2).ToString();
    });

XDocument modositott = new XDocument(gyoker);
modositott.Save("konyvtar_modositott.xml");
Console.WriteLine("Az új fájl neve: \"konyvtar_modositott.xml\"");

// Task 6
Console.WriteLine("\n(5.) Törlöm az összes tartalmaz elemet, majd elmentem egy új fájlba:\n");

gyoker.Descendants("tartalmaz")
    .ToList()
    .ForEach(elem => elem.Remove());

XDocument toroltDokumentum = new XDocument(gyoker);
toroltDokumentum.Save("konyvtar_torolt.xml");
Console.WriteLine("Az új fájl neve: \"konyvtar_torolt.xml\"");

// Task Bonus
Console.WriteLine("\n(6.) Egy új XML dokumentum létrehozása:\n");

XElement ujGyoker = new XElement("konyvtar",
    new XElement("konyv",
        new XAttribute("kKod", "k10"),
        new XElement("szerzo", "Újonc Szerző"),
        new XElement("cim", "LINQ to XML példa"),
        new XElement("kiado", "Új Kiadó"),
        new XElement("kiadas_datuma", "2024-01-01"),
        new XElement("nyelv", "Magyar")
    ),
    new XElement("konyv",
        new XAttribute("kKod", "k11"),
        new XElement("szerzo", "Tapasztalt Szerző"),
        new XElement("cim", "C# haladóknak"),
        new XElement("kiado", "Haladó Kiadó"),
        new XElement("kiadas_datuma", "2023-06-15"),
        new XElement("nyelv", "Magyar")
    )
);

ujGyoker.Descendants("konyv")
    .ToList()
    .ForEach(konyv =>
    {
        var cimElem = konyv.Descendants("cim").First();
        cimElem.Value = "[ÚJ] " + cimElem.Value;

        var szerzoElem = konyv.Descendants("szerzo").First();
        szerzoElem.Value += " (debütáló)";
    });

XDocument ujDokumentum = new XDocument(ujGyoker);
ujDokumentum.Save("konyvtar_uj.xml");
Console.WriteLine(ujGyoker);
Console.WriteLine("\nAz új fájl neve: \"konyvtar_uj.xml\"");