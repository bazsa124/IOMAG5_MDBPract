xquery version "3.1";

for $r in doc("/db/IOMAG5_XML.xml")//rendeles
return 
    update value $r/osszeg with $r/osszeg+1000