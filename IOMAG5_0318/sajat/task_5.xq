xquery version "3.1";

for $t in doc("/db/IOMAG5_XML_sajat.xml")//tag
let $kolcsonzesek := doc("/db/IOMAG5_XML_sajat.xml")//k_kolcsonzes[@kk_t = $t/@tKod]
for $kk in $kolcsonzesek
return
    <adat>
        <Nev>{$t/nev}</Nev>
        <KonyvID>{data($kk/@kk_k)}</KonyvID>
        <Kezdet>{$kk/kezdet}</Kezdet>
        <Veg>{$kk/veg}</Veg>
    </adat>
