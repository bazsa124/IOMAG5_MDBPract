xquery version "3.1";

for $v in doc("/db/IOMAG5_XML.xml")//vendeg
let $rendelesek :=  doc("/db/IOMAG5_XML.xml")//rendeles[@e_v_v = $v/@vKod]
for $r in $rendelesek
return
    <adat>  
        <nev>{$v/nev}</nev>  
        <osszeg>{$r/osszeg}</osszeg>
    </adat>