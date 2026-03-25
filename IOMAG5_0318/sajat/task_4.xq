xquery version "3.1";

for $a in doc("/db/IOMAG5_XML_sajat.xml")//album
    for $m in $a/mufaj
        where upper-case($m) = "POP"
        return $a
