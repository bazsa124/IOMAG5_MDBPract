xquery version "3.1";

(:

count(doc("/db/IOMAG5_XML_sajat.xml")//hasznalat)

sum(doc("/db/IOMAG5_XML_sajat.xml")//hasznalat/hasznalat_ideje)

:)

avg(doc("/db/IOMAG5_XML_sajat.xml")//hasznalat/hasznalat_ideje)
