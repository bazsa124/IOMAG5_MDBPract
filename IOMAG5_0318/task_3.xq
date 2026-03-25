xquery version "3.1";

for $e in doc("/db/IOMAG5_XML.xml")//etterem
where $e/csillag = 5
return 
    <etterem>  
        <EtteremID>{data($e/@eKod)}</EtteremID>  
        <Nev>{data($e/nev)}</Nev>  
        <Cim>{$e/cim}</Cim>  
        <Csillag>{data($e/csillag)}</Csillag>
    </etterem>
