import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Class representing the mission of Genesis
public class MissionGenesis {

    // Private fields
    private MolecularData molecularDataHuman; // Molecular data for humans
    private MolecularData molecularDataVitales; // Molecular data for Vitales

    // Getter for human molecular data
    public MolecularData getMolecularDataHuman() {
        return molecularDataHuman;
    }

    // Getter for Vitales molecular data
    public MolecularData getMolecularDataVitales() {
        return molecularDataVitales;
    }

    // Method to read XML data from the specified filename
    // This method should populate molecularDataHuman and molecularDataVitales fields once called
    public void readXML(String filename) {

        /* YOUR CODE HERE */

        try {
            File file = new File(filename);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();

            NodeList humanList = doc.getElementsByTagName("HumanMolecularData");
            Node humanNode = humanList.item(0);
            Element humanElement = (Element) humanNode;
            NodeList humanMolecules = humanElement.getElementsByTagName("Molecule");

            this.molecularDataHuman = parseMolecularData(humanMolecules);

            NodeList vitalesList = doc.getElementsByTagName("VitalesMolecularData");
            Node vitalesNode = vitalesList.item(0);
            Element vitalesElement = (Element) vitalesNode;
            NodeList vitalesMolecules = vitalesElement.getElementsByTagName("Molecule");

            this.molecularDataVitales = parseMolecularData(vitalesMolecules);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MolecularData parseMolecularData(NodeList molecules) {
        List<Molecule> moleculeList = new ArrayList<>();

        for (int i = 0; i < molecules.getLength(); i++) {
            Node moleculeNode = molecules.item(i);
            if (moleculeNode.getNodeType() == Node.ELEMENT_NODE) {
                Element moleculeElement = (Element) moleculeNode;
                String id = moleculeElement.getElementsByTagName("ID").item(0).getTextContent();
                int bondStrength = Integer.parseInt(moleculeElement.getElementsByTagName("BondStrength").item(0).getTextContent());

                NodeList bondList = moleculeElement.getElementsByTagName("Bonds");
                List<String> bonds = new ArrayList<>();
                for (int j = 0; j < bondList.getLength(); j++) {
                    Node bondNode = bondList.item(j);
                    if (bondNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element bondElement = (Element) bondNode;
                        NodeList moleculeIdList = bondElement.getElementsByTagName("MoleculeID");
                        for (int k = 0; k < moleculeIdList.getLength(); k++) {
                            Node moleculeIdNode = moleculeIdList.item(k);
                            if (moleculeIdNode.getNodeType() == Node.ELEMENT_NODE) {
                                String moleculeId = moleculeIdNode.getTextContent();
                                bonds.add(moleculeId);
                            }
                        }

                    }
                }
                Molecule molecule = new Molecule(id, bondStrength, bonds);
                moleculeList.add(molecule);
            }
        }
        return new MolecularData(moleculeList);
    }
}
