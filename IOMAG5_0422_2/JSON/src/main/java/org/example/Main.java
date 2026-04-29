package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException {
        ObjectMapper m = new ObjectMapper ();
        JsonNode root = m.readTree( new File ("src/main/JSON.json "));
        JsonNode vendeglatas = root . get ( "vendeglatas" );
        JsonNode foszakacsok = vendeglatas . get ( "foszakacs" );
        JsonNode szakacsok = vendeglatas . get ( "szakacs" );
        JsonNode ettermek = vendeglatas . get ( "etterem" );
        JsonNode rendelesek = vendeglatas . get ( "rendeles" );
        JsonNode vendegek = vendeglatas . get ( "vendeg" );

        //0. feladat
        JsonNode schemaNode = m . readTree ( new File ("src/main/JSON_SCHEMA.json "));
        JsonSchema schema = JsonSchemaFactory
                . getInstance ( SpecVersion . VersionFlag . V4 ) .getSchema ( schemaNode );
        Set < ValidationMessage > errors = schema . validate ( root );
        if ( errors . isEmpty ()) {
            System . out . println ( " Valid JSON " );
        } else {
            System . out . println ( " Hib ´a s JSON : " );
            errors . forEach ( e -> System . out . println ( e . getMessage ()));
        }

        //1. feladat
        System . out . println ( " === F }o szak ´a csok adatai === " );
        if ( foszakacsok != null && foszakacsok . isArray ()) {
            for ( JsonNode szakacs : foszakacsok ) {
                String nev = szakacs . get ( "nev" ). asText ();
                String kor = szakacs . get ( "eletkor" ). asText ();
                String iskola = szakacs . get ( "vegzettseg" ). asText ();
                System . out . println ( " N ´e v : " + nev + " | Kor : " + kor + " | V ´e gzetts ´e g : "
                        + iskola );
            }
        }

        // 2. feladat

        System . out . println ( " === ´E TTERMEK ´E S SZAK ´A CSAIK === " );
        for ( JsonNode etterem : ettermek ) {
            String eKod = etterem . get ( "_ekod" ). asText ();
            String eNev = etterem . get ( "nev" ). asText ();
            System . out . println ( "\nÉtterem : " + eNev + " [ " + eKod + " ] " );
            System . out . println ( " ---------------------------" );
            for ( JsonNode szakacs : szakacsok ) {
                if ( szakacs . get ( "_e_sz" ). asText (). equals ( eKod )) {
                    System . out . println ( " - " + szakacs . get ( "nev" ). asText () +
                            " ( " + szakacs . get ( "reszleg" ). asText () + " ) " );
                }
            }
        }

        // 3. feladat
        System . out . println ( " === ´A tlagos rendel ´e si ´e rt ´e k === " );
        double osszeg = 0;
        int db = 0;
        for ( JsonNode r : rendelesek ) {
            osszeg += r . get ( "osszeg" ). asDouble ();
            db ++;
        }
        System . out . println ( " AVG : " + ( osszeg / db ) + " Ft " );

        // 4. feladat
        System . out . println ( " \n === 2. Feladat : 5 csillagos ´e ttermek === " );
        for ( JsonNode e : ettermek ) {
            if ( e . get ( "csillag" ). asInt () == 5) {
                System . out . println ( " - " + e . get ( "nev" ). asText ());
            }
        }

        // 5. feladat
        System . out . println ("\n === 3. Feladat : Ki , hol , mit rendelt ? ( JOIN ) === " );
        for ( JsonNode r : rendelesek ) {
            String vKod = r. get ("_vkod" ). asText ();
            String eKod = r. get ("_ekod" ). asText ();
            String vNev = "";
            for ( JsonNode v : vendegek )
                if (v. get ("_vkod" ). asText (). equals ( vKod )) vNev = v. get ("nev" ). asText ();
            String eNev = "";
            for ( JsonNode e : ettermek )
                if (e. get ("_ekod" ). asText (). equals ( eKod )) eNev = e. get ("nev" ). asText ();
            System . out . println ( vNev + " ordered : " + r. get ("etel" ). asText () + " (" + eNev + " )" );
        }

        // 6. feladat
        System . out . println ( " \n === 5. Feladat : JSON adatok manipul ´a ci ´o ja === " );
        for ( JsonNode e : ettermek ) {
            ObjectNode obj = (ObjectNode) e ;
            obj . put ( "ellenorzott" , true );
            obj . remove ( "csillag" );
        }

        System . out . println ( " === F }o szak ´a csok adatai === " );
        if ( foszakacsok != null && foszakacsok . isArray ()) {
            for ( JsonNode szakacs : foszakacsok ) {
                String nev = szakacs . get ( "nev" ). asText ();
                String kor = szakacs . get ( "eletkor" ). asText ();
                String iskola = szakacs . get ( "vegzettseg" ). asText ();
                System . out . println ( " N ´e v : " + nev + " | Kor : " + kor + " | V ´e gzetts ´e g : "
                        + iskola );
            }
        }

        // 7. feladat
        System . out . println ("\n === 6. Feladat : VIP Vend ´eg ( Legt ¨o bbet k ¨o lt }o) === " );
        Map < String , Double > koltesek = new HashMap< >();
        for ( JsonNode r : rendelesek ) {
            String vKod = r. get ("_vkod" ). asText ();
            double ar = r. get ("osszeg" ). asDouble ();
            koltesek . put ( vKod , koltesek . getOrDefault ( vKod , 0.0) + ar );
        }
        String maxVkod = "";
        double maxOsszeg = 0;
        for ( Map.Entry < String , Double > entry : koltesek . entrySet ()) {
            if ( entry . getValue () > maxOsszeg ) {
                maxOsszeg = entry . getValue ();
                maxVkod = entry . getKey ();
            }
        }
        String vipNev = " ";
        for ( JsonNode v : vendegek ) {
            if (v . get ("_vkod" ). asText (). equals ( maxVkod )) {
                vipNev = v. get ("nev" ). asText ();
                break ;
            }
        }
        System . out . println (" VIP guest : " + vipNev + " ( Total spent : " + maxOsszeg + " HUF )" );

    }
}