package IOMAG5;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.sql.SQLOutput;

import org.w3c.dom.*;
import javax.xml.parsers.*;

public class IOMAG5DOMParser {
    public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
        try {
            File xmlFile = new File("src/IOMAG5/IOMAG5_XML.xml");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);

            doc.getDocumentElement().normalize();

            Element root = doc.getDocumentElement();
            System.out.println("Root element: " + root.getNodeName());

            /////////////////////////////////////////////////

            NodeList nList = doc.getElementsByTagName("etterem");

            for(int i=0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);

                System.out.println("\nCurrent Element: "+ nNode.getNodeName());

                if(nNode.getNodeType()==Node.ELEMENT_NODE){

                    Element elem = (Element) nNode;

                    String id =elem.getAttribute("eKod");
                    Node node1 = elem.getElementsByTagName("nev").item(0);
                    Node node2 = elem.getElementsByTagName("varos").item(0);
                    Node node3 = elem.getElementsByTagName("utca").item(0);
                    Node node4 = elem.getElementsByTagName("hazszam").item(0);
                    Node node5 = elem.getElementsByTagName("csillag").item(0);

                    String adr = node2.getTextContent() + ", " + node3.getTextContent() + " utca " + node4.getTextContent()+".";

                    System.out.println("Étterem ID: "+id);
                    System.out.println("Név: "+node1.getTextContent());
                    System.out.println("Cím: "+adr);
                    System.out.println("Csillag: "+node5.getTextContent());
                }
            }

            /////////////////////////////////////////////////

            nList = doc.getElementsByTagName("foszakacs");

            for(int i=0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);

                System.out.println("\nCurrent Element: " + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element elem = (Element) nNode;

                    String id = elem.getAttribute("fKod");
                    String etteremId = elem.getAttribute("e_f");
                    Node node1 = elem.getElementsByTagName("nev").item(0);
                    Node node2 = elem.getElementsByTagName("eletkor").item(0);
                    System.out.println("Főszakács ID: " + id);
                    System.out.println("Étterem ID: "+etteremId);
                    System.out.println("Név: " + node1.getTextContent());
                    System.out.println("Életkor: " + node2.getTextContent());

                    NodeList nVegzettseg = elem.getElementsByTagName("vegzettseg");
                    String edu="";
                    for (int j = 0; j < nVegzettseg.getLength(); j++) {
                        Node nTemp = nVegzettseg.item(j);
                        if(j==nVegzettseg.getLength()-1)
                            edu+=nTemp.getTextContent();
                        else
                            edu+=nTemp.getTextContent()+", ";
                    }
                    System.out.println("Végzettség: " + edu);
                }
            }

            /////////////////////////////////////////////////

            nList = doc.getElementsByTagName("szakacs");

            for(int i=0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);

                System.out.println("\nCurrent Element: " + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element elem = (Element) nNode;

                    String id = elem.getAttribute("szKod");
                    String etteremId = elem.getAttribute("e_sz");
                    Node node1 = elem.getElementsByTagName("nev").item(0);
                    Node node2 = elem.getElementsByTagName("reszleg").item(0);
                    System.out.println("Szakács ID: " + id);
                    System.out.println("Étterem ID: "+etteremId);
                    System.out.println("Név: " + node1.getTextContent());
                    System.out.println("Részleg: " + node2.getTextContent());

                    NodeList nVegzettseg = elem.getElementsByTagName("vegzettseg");
                    String edu ="";
                    for (int j = 0; j < nVegzettseg.getLength(); j++) {
                        Node nTemp = nVegzettseg.item(j);
                        if(j==nVegzettseg.getLength()-1)
                            edu+=nTemp.getTextContent();
                        else
                            edu+=nTemp.getTextContent()+", ";
                    }
                    System.out.println("Végzettség: " + edu);

                }
            }

            //////////////////////////////

            nList = doc.getElementsByTagName("gyakornok");

            for(int i=0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);

                System.out.println("\nCurrent Element: " + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element elem = (Element) nNode;

                    String id = elem.getAttribute("gyKod");
                    String etteremId = elem.getAttribute("e_gy");
                    Node node1 = elem.getElementsByTagName("nev").item(0);
                    Node node2 = elem.getElementsByTagName("kezdete").item(0);
                    Node node3 = elem.getElementsByTagName("idotartam").item(0);
                    System.out.println("Gyakornok ID: " + id);
                    System.out.println("Étterem ID: "+etteremId);
                    System.out.println("Név: " + node1.getTextContent());
                    System.out.println("Kezdet: " + node2.getTextContent());
                    System.out.println("Időtartam: " + node3.getTextContent());

                    NodeList nVegzettseg = elem.getElementsByTagName("muszak");
                    String msz ="";
                    for (int j = 0; j < nVegzettseg.getLength(); j++) {
                        Node nTemp = nVegzettseg.item(j);
                        if(j==nVegzettseg.getLength()-1)
                            msz+=nTemp.getTextContent();
                        else
                            msz+=nTemp.getTextContent()+", ";
                    }
                    System.out.println("Műszak: " + msz);

                }
            }

            /////////////////////////////

            nList = doc.getElementsByTagName("vendeg");

            for(int i=0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);

                System.out.println("\nCurrent Element: "+ nNode.getNodeName());

                if(nNode.getNodeType()==Node.ELEMENT_NODE){

                    Element elem = (Element) nNode;

                    String id =elem.getAttribute("vKod");
                    Node node1 = elem.getElementsByTagName("nev").item(0);
                    Node node2 = elem.getElementsByTagName("varos").item(0);
                    Node node3 = elem.getElementsByTagName("utca").item(0);
                    Node node4 = elem.getElementsByTagName("hazszam").item(0);
                    Node node5 = elem.getElementsByTagName("eletkor").item(0);

                    String adr = node2.getTextContent() + ", " + node3.getTextContent() + " utca " + node4.getTextContent()+".";

                    System.out.println("Vendég ID: "+id);
                    System.out.println("Életkor: "+node5.getTextContent());
                    System.out.println("Név: "+node1.getTextContent());
                    System.out.println("Cím: "+adr);
                }
            }

            ///////////////////////////

            nList = doc.getElementsByTagName("rendeles");

            for(int i=0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);

                System.out.println("\nCurrent Element: "+ nNode.getNodeName());

                if(nNode.getNodeType()==Node.ELEMENT_NODE){

                    Element elem = (Element) nNode;

                    String etteremId = elem.getAttribute("e_v_e");
                    String vendegId = elem.getAttribute("e_v_v");

                    Node node1 = elem.getElementsByTagName("etel").item(0);
                    Node node2 = elem.getElementsByTagName("osszeg").item(0);

                    System.out.println("Vendég ID: "+vendegId);
                    System.out.println("Étterem ID: "+etteremId);
                    System.out.println("Étel: "+node1.getTextContent());
                    System.out.println("Összeg: "+node2.getTextContent());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}