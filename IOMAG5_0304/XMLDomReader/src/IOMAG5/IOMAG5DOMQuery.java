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

public class IOMAG5DOMQuery {
    public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
        try {
            File xmlFile = new File("src/IOMAG5/IOMAG5_XML.xml");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);

            doc.getDocumentElement().normalize();

            // ── Query 1: Szakácsok akiknek van szakközépiskola végzettségük ──────────

            NodeList nList = doc.getElementsByTagName("szakacs");

            System.out.println("Szakácsok, akiknek van szakközépiskola végzettségük:");
            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element elem = (Element) nNode;

                    NodeList nVegzettseg = elem.getElementsByTagName("vegzettseg");
                    for (int j = 0; j < nVegzettseg.getLength(); j++) {
                        Node nTemp = nVegzettseg.item(j);
                        if (nTemp.getTextContent().contains("Szakközépiskola")) {

                            String id = elem.getAttribute("szKod");
                            String etteremId = elem.getAttribute("e_sz");
                            Node node1 = elem.getElementsByTagName("nev").item(0);
                            Node node2 = elem.getElementsByTagName("reszleg").item(0);
                            System.out.println("\nSzakács ID: " + id);
                            System.out.println("Étterem ID: " + etteremId);
                            System.out.println("Név: " + node1.getTextContent());
                            System.out.println("Részleg: " + node2.getTextContent());

                            String edu = "";
                            for (int k = 0; k < nVegzettseg.getLength(); k++) {
                                Node tempVeg = nVegzettseg.item(k);
                                if (k == nVegzettseg.getLength() - 1)
                                    edu += tempVeg.getTextContent();
                                else
                                    edu += tempVeg.getTextContent() + ", ";
                            }
                            System.out.println("Végzettség: " + edu);
                            break;
                        }
                    }
                }
            }

            // ── Query 2: Azok az éttermek, amelyek öt csillagosak ────────────────────

            nList = doc.getElementsByTagName("etterem");

            System.out.println("\nAzok az éttermek, amik öt csillagosak:");
            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element elem = (Element) nNode;

                    Node nodeCsillag = elem.getElementsByTagName("csillag").item(0);
                    if (nodeCsillag.getTextContent().trim().equals("5")) {

                        String id = elem.getAttribute("eKod");
                        Node node1 = elem.getElementsByTagName("nev").item(0);
                        Node node2 = elem.getElementsByTagName("varos").item(0);
                        Node node3 = elem.getElementsByTagName("utca").item(0);
                        Node node4 = elem.getElementsByTagName("hazszam").item(0);

                        String adr = node2.getTextContent() + ", " + node3.getTextContent()
                                + " utca " + node4.getTextContent() + ".";

                        System.out.println("\nÉtterem ID: " + id);
                        System.out.println("Név: " + node1.getTextContent());
                        System.out.println("Cím: " + adr);
                        System.out.println("Csillag: " + nodeCsillag.getTextContent().trim());
                    }
                }
            }

            // ── Query 3: Gyakornokok, akik délutánra be vannak osztva ────────────────

            nList = doc.getElementsByTagName("gyakornok");

            System.out.println("\nGyakornokok, akik délutánra be vannak osztva:");
            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element elem = (Element) nNode;

                    NodeList nMuszak = elem.getElementsByTagName("muszak");
                    boolean hasDelelutan = false;
                    for (int j = 0; j < nMuszak.getLength(); j++) {
                        if (nMuszak.item(j).getTextContent().contains("délután")) {
                            hasDelelutan = true;
                            break;
                        }
                    }

                    if (hasDelelutan) {
                        String id = elem.getAttribute("gyKod");
                        String etteremId = elem.getAttribute("e_gy");
                        Node node1 = elem.getElementsByTagName("nev").item(0);
                        Node node2 = elem.getElementsByTagName("kezdete").item(0);

                        System.out.println("\nGyakornok ID: " + id);
                        System.out.println("Étterem ID: " + etteremId);
                        System.out.println("Név: " + node1.getTextContent());
                        System.out.println("Gyakorlat kezdete: " + node2.getTextContent());

                        String mszList = "";
                        for (int j = 0; j < nMuszak.getLength(); j++) {
                            Node nTemp = nMuszak.item(j);
                            if (j == nMuszak.getLength() - 1)
                                mszList += nTemp.getTextContent();
                            else
                                mszList += nTemp.getTextContent() + ", ";
                        }
                        System.out.println("Műszak: " + mszList);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}