// See https://aka.ms/new-console-template for more information
using System.Xml.Linq;

//Task 1
XDocument dokumentum = XDocument.Load("etterem.xml");
XElement gyoker = dokumentum.Descendants("vendeglatas").First();

//Task 2
Console.WriteLine("(0.) A teljes dokumentum: \n\n" + gyoker);

Console.WriteLine("(1.) Az ötcsillagos éttermek: \n");
var otCsillagosEttermek = gyoker.Descendants("etterem")
     .Where(elem => elem.Descendants("csillag").First().Value == "5")
     .ToList();
otCsillagosEttermek.ForEach(elem =>
  Console.WriteLine(" - " + elem.Descendants("nev").First().Value)
);

//Task 3
Console.WriteLine("(2.) Melyik vendég, melyik étteremben, mit rendelt, mennyiért: \n");

var harmasJoin = gyoker.Descendants("rendeles")
   .Select(elem =>
   {
       var vendegID = elem.Attribute("e_v_v").Value;
       var vendeg = gyoker.Descendants("vendeg")
       .Where(vendegElem => vendegElem.Attribute("vkod").Value == vendegID)
             .First()
             .Descendants("nev")
             .FirstOrDefault().Value;

       var etteremID = elem.Attribute("e_v_e").Value;
       var etterem = gyoker.Descendants("etterem")
            .First(etteremElem => etteremElem.Attribute("ekod").Value == etteremID)
           .Descendants("nev")
           .FirstOrDefault().Value;
       var rendeltEtel = elem.Descendants("etel").First().Value;
       var osszeg = elem.Descendants("osszeg").First().Value;

       return new
       {
           Vendeg = vendeg,
           Etterem = etterem,
           Etel = rendeltEtel,
           Osszeg = osszeg
       };
   })
    .ToList();

harmasJoin.ForEach(join => Console.WriteLine(join));

//Task 4 
var atlagKoltes = gyoker.Descendants("rendeles")
        .Select(rendeles => rendeles.Descendants("osszeg").First().Value)
        .Average(osszeg => double.Parse(osszeg));
Console.WriteLine($"Az átlagos költés: {atlagKoltes} Ft");

//Task 5
gyoker.Descendants("rendeles")
    .ToList()
    .ForEach(
        rendeles =>
        {
            var osszegElem = rendeles.Descendants("osszeg").First();
            osszegElem.Value = (double.Parse(osszegElem.Value) * 2).ToString();
        }
    );

XDocument modositott = new XDocument(gyoker);
modositott.Save("etterem_modositott.xml");
Console.WriteLine("Az új fájl neve: \"etterem_modositott.xml\"");

//Task 6
Console.WriteLine("(5.) Törlöm az összes 3 csillagos éttermet, majd elmentem egy új fájlba: \n");
gyoker.Descendants("etterem")
.Where(elem => elem.Descendants("csillag").First().Value == "3")
.ToList()
.ForEach(elem => elem.Remove());

XDocument toroltDokumentum = new XDocument(gyoker);
toroltDokumentum.Save("etterem_torolt.xml");
Console.WriteLine("Az új fájl neve: \"etterem_torolt.xml\"");

//Task Bonus
Console.WriteLine("(6.) Egy új XML dokumentum létrehozása: ");
XElement ujGyoker = new XElement("konyvtar",
     new XElement("konyv",
         new XAttribute("isbn", "1234567890"),
         new XElement("cim", "LINQ to XML példa"),
         new XElement("szerzo", "Nagyszerű Konrád"),
         new XElement("ar", "2990")
     ),
     new XElement("konyv",
         new XAttribute("isbn", "0987654321"),
         new XElement("cim", "C# programozás"),
         new XElement("szerzo", "Szerény Konrád"),
         new XElement("ar", "3990")
     )
 );

 ujGyoker.Descendants("konyv")
     .ToList()
     .ForEach(konyv => {
         var arElem = konyv.Descendants("ar").First();
         var ar = double.Parse(arElem.Value);
         ar *= 2;
         arElem.Value = ar.ToString();
         
         var szerzoElem = konyv.Descendants("szerzo").First();
         var szerzo = szerzoElem.Value;
         szerzo += " (best seller)";
         szerzoElem.Value = szerzo;
     });
 XDocument ujDokumentum = new XDocument(ujGyoker);
 ujDokumentum.Save("konyvtar.xml");
 Console.WriteLine("\nAz új fájl neve: \"konyvtar.xml\"");