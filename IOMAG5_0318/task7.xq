xquery version "3.1";

(:  

count(doc("/db/IOMAG5_XML.xml")//rendeles)

sum(doc("/db/IOMAG5_XML.xml")//rendeles/osszeg)

:)

avg(doc("/db/IOMAG5_XML.xml")//rendeles/osszeg)