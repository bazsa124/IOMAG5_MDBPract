package IOMAG5;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class IOMAG5DOMQueryOwn {
    public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
        try {
            File xmlFile = new File("src/IOMAG5/IOMAG5_XML_sajat.xml");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);

            doc.getDocumentElement().normalize();

            // ── Query 1: Könyvek amelyek angol nyelvűek ──────────────────────────────

            NodeList nList = doc.getElementsByTagName("konyv");

            System.out.println("Angol nyelvű könyvek:");
            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element elem = (Element) nNode;

                    Node nodeNyelv = elem.getElementsByTagName("nyelv").item(0);
                    if (nodeNyelv.getTextContent().trim().equals("Angol")) {

                        String id    = elem.getAttribute("kKod");
                        Node node1   = elem.getElementsByTagName("szerzo").item(0);
                        Node node2   = elem.getElementsByTagName("cim").item(0);
                        Node node3   = elem.getElementsByTagName("kiado").item(0);
                        Node node4   = elem.getElementsByTagName("kiadas_datuma").item(0);

                        System.out.println("\nKönyv ID: " + id);
                        System.out.println("Szerző: " + node1.getTextContent());
                        System.out.println("Cím: " + node2.getTextContent());
                        System.out.println("Kiadó: " + node3.getTextContent());
                        System.out.println("Kiadás dátuma: " + node4.getTextContent());
                        System.out.println("Nyelv: " + nodeNyelv.getTextContent().trim());
                    }
                }
            }

            // ── Query 2: Filmek amelyek 2000 után jelentek meg ───────────────────────

            nList = doc.getElementsByTagName("film");

            System.out.println("\nFilmek, amelyek 2000 után jelentek meg:");
            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element elem = (Element) nNode;

                    Node nodeDatum = elem.getElementsByTagName("megjelenes_datuma").item(0);
                    // dates are in yyyy-MM-dd format, string comparison works for year filtering
                    if (nodeDatum.getTextContent().trim().compareTo("2000-01-01") > 0) {

                        String id  = elem.getAttribute("fKod");
                        Node node1 = elem.getElementsByTagName("cim").item(0);
                        Node node2 = elem.getElementsByTagName("kiado").item(0);
                        Node node3 = elem.getElementsByTagName("hossz").item(0);

                        System.out.println("\nFilm ID: " + id);
                        System.out.println("Cím: " + node1.getTextContent());
                        System.out.println("Kiadó: " + node2.getTextContent());
                        System.out.println("Megjelenés dátuma: " + nodeDatum.getTextContent().trim());
                        System.out.println("Hossz: " + node3.getTextContent() + " perc");
                    }
                }
            }

            // ── Query 3: Albumok amelyek POP műfajúak ───────────────────────────────

            nList = doc.getElementsByTagName("album");

            System.out.println("\nPOP műfajú albumok:");
            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element elem = (Element) nNode;

                    NodeList nMufaj = elem.getElementsByTagName("mufaj");
                    boolean isPop = false;
                    for (int j = 0; j < nMufaj.getLength(); j++) {
                        if (nMufaj.item(j).getTextContent().equalsIgnoreCase("POP")) {
                            isPop = true;
                            break;
                        }
                    }

                    if (isPop) {
                        String id  = elem.getAttribute("aKod");
                        Node node1 = elem.getElementsByTagName("cim").item(0);
                        Node node2 = elem.getElementsByTagName("kiado").item(0);
                        Node node3 = elem.getElementsByTagName("kiadas_eve").item(0);

                        System.out.println("\nAlbum ID: " + id);
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
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}