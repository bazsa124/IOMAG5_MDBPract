xquery version "3.1";

for $s in doc("/db/IOMAG5_XML.xml")//szakacs 
where $s/vegzettseg = "Szakközépiskola"
return 
    <szakacs>  
        <SzakacsID>{data($s/@id)}</SzakacsID>  
        <Nev>{data($s/nev)}</Nev>  
        <Reszleg>{data($s/reszleg)}</Reszleg>  
        <Vegzettsegek>{$s/vegzettseg}</Vegzettsegek> 
    </szakacs>
