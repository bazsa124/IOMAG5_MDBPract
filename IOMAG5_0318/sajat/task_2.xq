xquery version "3.1";

for $k in doc("/db/IOMAG5_XML_sajat.xml")//konyv
where $k/kiado = "Teszt Kiadó"
return
    <konyv>
        <KonyvID>{data($k/@kKod)}</KonyvID>
        <Szerzo>{data($k/szerzo)}</Szerzo>
        <Cim>{data($k/cim)}</Cim>
        <Kiado>{data($k/kiado)}</Kiado>
        <KiadasDatuma>{data($k/kiadas_datuma)}</KiadasDatuma>
    </konyv>
