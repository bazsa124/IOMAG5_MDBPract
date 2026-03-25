xquery version "3.1";

for $ig in doc("/db/IOMAG5_XML_sajat.xml")//igenyles
return
    update value $ig/beszer_ar with $ig/beszer_ar + 500
