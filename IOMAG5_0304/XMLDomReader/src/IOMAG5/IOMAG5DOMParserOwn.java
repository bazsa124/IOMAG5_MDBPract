package IOMAG5;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import org.w3c.dom.*;
import javax.xml.parsers.*;

public class IOMAG5DOMParserOwn {
    public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
        try {
            File xmlFile = new File("src/IOMAG5/IOMAG5_XML_sajat.xml");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);

            doc.getDocumentElement().normalize();

            Element root = doc.getDocumentElement();
            System.out.println("Root element: " + root.getNodeName());

            /////////////////////////////////////////////////

            NodeList nList = doc.getElementsByTagName("tag");

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);

                System.out.println("\nCurrent Element: " + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element elem = (Element) nNode;

                    String id = elem.getAttribute("tKod");
                    Node node1 = elem.getElementsByTagName("nev").item(0);
                    Node node2 = elem.getElementsByTagName("iranyitoszam").item(0);
                    Node node3 = elem.getElementsByTagName("utca").item(0);
                    Node node4 = elem.getElementsByTagName("hazszam").item(0);
                    Node node5 = elem.getElementsByTagName("tagdij").item(0);

                    String adr = node2.getTextContent() + ", " + node3.getTextContent() + " utca " + node4.getTextContent() + ".";

                    System.out.println("Tag ID: " + id);
                    System.out.println("Név: " + node1.getTextContent());
                    System.out.println("Lakcím: " + adr);
                    System.out.println("Tagdíj: " + node5.getTextContent() + " Ft");

                    NodeList nEmail = elem.getElementsByTagName("email");
                    String emails = "";
                    for (int j = 0; j < nEmail.getLength(); j++) {
                        Node nTemp = nEmail.item(j);
                        if (j == nEmail.getLength() - 1)
                            emails += nTemp.getTextContent();
                        else
                            emails += nTemp.getTextContent() + ", ";
                    }
                    System.out.println("Email: " + emails);

                    NodeList nTelefon = elem.getElementsByTagName("telefonszam");
                    String telefons = "";
                    for (int j = 0; j < nTelefon.getLength(); j++) {
                        Node nTemp = nTelefon.item(j);
                        if (j == nTelefon.getLength() - 1)
                            telefons += nTemp.getTextContent();
                        else
                            telefons += nTemp.getTextContent() + ", ";
                    }
                    System.out.println("Telefonszám: " + telefons);
                }
            }

            /////////////////////////////////////////////////

            nList = doc.getElementsByTagName("szamitogep");

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);

                System.out.println("\nCurrent Element: " + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element elem = (Element) nNode;

                    String id = elem.getAttribute("szKod");
                    Node node1 = elem.getElementsByTagName("operacios_rendszer").item(0);
                    Node node2 = elem.getElementsByTagName("tarhely").item(0);

                    System.out.println("Számítógép ID: " + id);
                    System.out.println("Operációs rendszer: " + node1.getTextContent());
                    System.out.println("Tárhely: " + node2.getTextContent());

                    NodeList nFelsz = elem.getElementsByTagName("felszereltseg");
                    String felsz = "";
                    for (int j = 0; j < nFelsz.getLength(); j++) {
                        Node nTemp = nFelsz.item(j);
                        if (j == nFelsz.getLength() - 1)
                            felsz += nTemp.getTextContent();
                        else
                            felsz += nTemp.getTextContent() + ", ";
                    }
                    System.out.println("Felszereltség: " + felsz);
                }
            }

            /////////////////////////////////////////////////

            nList = doc.getElementsByTagName("konyv");

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);

                System.out.println("\nCurrent Element: " + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element elem = (Element) nNode;

                    String id = elem.getAttribute("kKod");
                    Node node1 = elem.getElementsByTagName("szerzo").item(0);
                    Node node2 = elem.getElementsByTagName("cim").item(0);
                    Node node3 = elem.getElementsByTagName("kiado").item(0);
                    Node node4 = elem.getElementsByTagName("kiadas_datuma").item(0);
                    Node node5 = elem.getElementsByTagName("nyelv").item(0);

                    System.out.println("Könyv ID: " + id);
                    System.out.println("Szerző: " + node1.getTextContent());
                    System.out.println("Cím: " + node2.getTextContent());
                    System.out.println("Kiadó: " + node3.getTextContent());
                    System.out.println("Kiadás dátuma: " + node4.getTextContent());
                    System.out.println("Nyelv: " + node5.getTextContent());
                }
            }

            /////////////////////////////////////////////////

            nList = doc.getElementsByTagName("film");

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);

                System.out.println("\nCurrent Element: " + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element elem = (Element) nNode;

                    String id = elem.getAttribute("fKod");
                    Node node1 = elem.getElementsByTagName("cim").item(0);
                    Node node2 = elem.getElementsByTagName("kiado").item(0);
                    Node node3 = elem.getElementsByTagName("megjelenes_datuma").item(0);
                    Node node4 = elem.getElementsByTagName("hossz").item(0);

                    System.out.println("Film ID: " + id);
                    System.out.println("Cím: " + node1.getTextContent());
                    System.out.println("Kiadó: " + node2.getTextContent());
                    System.out.println("Megjelenés dátuma: " + node3.getTextContent());
                    System.out.println("Hossz: " + node4.getTextContent() + " perc");
                }
            }

            /////////////////////////////////////////////////

            nList = doc.getElementsByTagName("album");

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);

                System.out.println("\nCurrent Element: " + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element elem = (Element) nNode;

                    String id = elem.getAttribute("aKod");
                    Node node1 = elem.getElementsByTagName("cim").item(0);
                    Node node2 = elem.getElementsByTagName("kiado").item(0);
                    Node node3 = elem.getElementsByTagName("kiadas_eve").item(0);

                    System.out.println("Album ID: " + id);
                    System.out.println("Cím: " + node1.getTextContent());
                    System.out.println("Kiadó: " + node2.getTextContent());
                    System.out.println("Kiadás éve: " + node3.getTextContent());

                    NodeList nEloado = elem.getElementsByTagName("eloado");
                    String eloadok = "";
                    for (int j = 0; j < nEloado.getLength(); j++) {
                        Node nTemp = nEloado.item(j);
                        if (j == nEloado.getLength() - 1)
                            eloadok += nTemp.getTextContent();
                        else
                            eloadok += nTemp.getTextContent() + ", ";
                    }
                    System.out.println("Előadó: " + eloadok);

                    NodeList nMufaj = elem.getElementsByTagName("mufaj");
                    String mufajok = "";
                    for (int j = 0; j < nMufaj.getLength(); j++) {
                        Node nTemp = nMufaj.item(j);
                        if (j == nMufaj.getLength() - 1)
                            mufajok += nTemp.getTextContent();
                        else
                            mufajok += nTemp.getTextContent() + ", ";
                    }
                    System.out.println("Műfaj: " + mufajok);
                }
            }

            /////////////////////////////////////////////////

            nList = doc.getElementsByTagName("dal");

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);

                System.out.println("\nCurrent Element: " + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element elem = (Element) nNode;

                    String id = elem.getAttribute("dKod");
                    Node node1 = elem.getElementsByTagName("eloado").item(0);
                    Node node2 = elem.getElementsByTagName("cim").item(0);
                    Node node3 = elem.getElementsByTagName("hossz").item(0);
                    Node node4 = elem.getElementsByTagName("kiado").item(0);
                    Node node5 = elem.getElementsByTagName("kiadas_eve").item(0);

                    System.out.println("Dal ID: " + id);
                    System.out.println("Előadó: " + node1.getTextContent());
                    System.out.println("Cím: " + node2.getTextContent());
                    System.out.println("Hossz: " + node3.getTextContent() + " mp");
                    System.out.println("Kiadó: " + node4.getTextContent());
                    System.out.println("Kiadás éve: " + node5.getTextContent());
                }
            }

            /////////////////////////////////////////////////

            nList = doc.getElementsByTagName("hasznalat");

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);

                System.out.println("\nCurrent Element: " + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element elem = (Element) nNode;

                    String tagId  = elem.getAttribute("t_h");
                    String szamId = elem.getAttribute("h_sz");
                    Node node1 = elem.getElementsByTagName("datum").item(0);
                    Node node2 = elem.getElementsByTagName("hasznalat_ideje").item(0);

                    System.out.println("Tag ID: " + tagId);
                    System.out.println("Számítógép ID: " + szamId);
                    System.out.println("Dátum: " + node1.getTextContent());
                    System.out.println("Használat ideje: " + node2.getTextContent() + " perc");
                }
            }

            /////////////////////////////////////////////////

            nList = doc.getElementsByTagName("igenyles");

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);

                System.out.println("\nCurrent Element: " + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element elem = (Element) nNode;

                    String tagId   = elem.getAttribute("i_t");
                    String konyvId = elem.getAttribute("i_k");
                    Node node1 = elem.getElementsByTagName("beszer_ar").item(0);
                    Node node2 = elem.getElementsByTagName("szallitasi_datum").item(0);
                    Node node3 = elem.getElementsByTagName("igenylesi_datum").item(0);

                    System.out.println("Tag ID: " + tagId);
                    System.out.println("Könyv ID: " + konyvId);
                    System.out.println("Beszerzési ár: " + node1.getTextContent() + " Ft");
                    System.out.println("Szállítási dátum: " + node2.getTextContent());
                    System.out.println("Igénylési dátum: " + node3.getTextContent());
                }
            }

            /////////////////////////////////////////////////

            nList = doc.getElementsByTagName("k_kolcsonzes");

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);

                System.out.println("\nCurrent Element: " + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element elem = (Element) nNode;

                    String tagId   = elem.getAttribute("kk_t");
                    String konyvId = elem.getAttribute("kk_k");
                    Node node1 = elem.getElementsByTagName("kezdet").item(0);
                    Node node2 = elem.getElementsByTagName("veg").item(0);

                    System.out.println("Tag ID: " + tagId);
                    System.out.println("Könyv ID: " + konyvId);
                    System.out.println("Kezdet: " + node1.getTextContent());
                    System.out.println("Vég: " + node2.getTextContent());
                }
            }

            /////////////////////////////////////////////////

            nList = doc.getElementsByTagName("f_kolcsonzes");

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);

                System.out.println("\nCurrent Element: " + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element elem = (Element) nNode;

                    String tagId  = elem.getAttribute("fk_t");
                    String filmId = elem.getAttribute("fk_f");
                    Node node1 = elem.getElementsByTagName("kezdet").item(0);
                    Node node2 = elem.getElementsByTagName("veg").item(0);

                    System.out.println("Tag ID: " + tagId);
                    System.out.println("Film ID: " + filmId);
                    System.out.println("Kezdet: " + node1.getTextContent());
                    System.out.println("Vég: " + node2.getTextContent());
                }
            }

            /////////////////////////////////////////////////

            nList = doc.getElementsByTagName("a_kolcsonzes");

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);

                System.out.println("\nCurrent Element: " + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element elem = (Element) nNode;

                    String tagId   = elem.getAttribute("ak_t");
                    String albumId = elem.getAttribute("ak_a");
                    Node node1 = elem.getElementsByTagName("kezdet").item(0);
                    Node node2 = elem.getElementsByTagName("veg").item(0);

                    System.out.println("Tag ID: " + tagId);
                    System.out.println("Album ID: " + albumId);
                    System.out.println("Kezdet: " + node1.getTextContent());
                    System.out.println("Vég: " + node2.getTextContent());
                }
            }

            /////////////////////////////////////////////////

            nList = doc.getElementsByTagName("tartalmaz");

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);

                System.out.println("\nCurrent Element: " + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element elem = (Element) nNode;

                    String albumId = elem.getAttribute("ta_a");
                    String dalId   = elem.getAttribute("ta_d");

                    System.out.println("Album ID: " + albumId);
                    System.out.println("Dal ID: " + dalId);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}