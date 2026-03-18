package IOMAG5;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
public class IOMAG5DOMModify {
    public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
        try {
            File xmlFile = new File("src/IOMAG5/IOMAG5_XML.xml");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("vendeg");

            for(int i=0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);

                if(nNode.getNodeType()==Node.ELEMENT_NODE){

                    Element elem = (Element) nNode;

                    String id =elem.getAttribute("vKod");
                    if(id.equals("v1")){
                        elem.getElementsByTagName("nev").item(0).setTextContent("Alma Palma");
                        elem.getElementsByTagName("eletkor").item(0).setTextContent("25");
                    }
                }
            }

            nList = doc.getElementsByTagName("gyakornok");

            for(int i=0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);

                if(nNode.getNodeType()==Node.ELEMENT_NODE){

                    Element elem = (Element) nNode;

                    String id =elem.getAttribute("gyKod");
                    if(id.equals("gy2")){
                        elem.setAttribute("e_gy","e3");
                    }
                }
            }

            nList=doc.getElementsByTagName("rendeles");
            do{
                doc.getDocumentElement().removeChild(nList.item(0));
            }while(nList.getLength()>0);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();
            t.transform(new DOMSource(doc), new StreamResult(System.out));
            t.transform(new DOMSource(doc), new StreamResult(new FileOutputStream("src/IOMAG5/modifiedIOMAG5.xml")));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
