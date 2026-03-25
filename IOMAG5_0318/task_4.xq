xquery version "3.1";

for $gy in doc("/db/IOMAG5_XML.xml")//gyakornok
    for $m in $gy/muszak
        where $m="délután"
        return $gy 