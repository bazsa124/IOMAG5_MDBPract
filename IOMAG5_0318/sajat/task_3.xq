xquery version "3.1";

for $f in doc("/db/IOMAG5_XML_sajat.xml")//film
where $f/kiado = "Disney"
return
    <film>
        <FilmID>{data($f/@fKod)}</FilmID>
        <Cim>{data($f/cim)}</Cim>
        <Kiado>{data($f/kiado)}</Kiado>
        <MegjelenesDatuma>{data($f/megjelenes_datuma)}</MegjelenesDatuma>
        <Hossz>{data($f/hossz)}</Hossz>
    </film>
