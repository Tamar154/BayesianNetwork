import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;


import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BayesianNetwork {
    private Map<Variable, List<Variable>> adjacencyList;

    public BayesianNetwork(File file) {
        Boolean flag = parseXML(file);
    }

    /**
     * XML structure:
     * <NETWORK> - root element
     * |  <VARIABLE> elements:
     * |  |    <NAME>: variable name
     * |  |   <outcome>: possible outcomes
     * |  <DEFINITION> elements:
     * |  |    <FOR>: indicates which variable it is
     * |  |    <GIVEN>: parent variables (optional)
     * |  |    <TABLE>: Conditional Probability Table (CPT) values, separated by spaces, and arranged according to the order in which the values of the parents and of the variable appeared.
     *
     * @param xmlFile an XML file representing the Bayesian Network structure.
     * @return true on success, false on error.
     */
    private Boolean parseXML(File xmlFile) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();

            Map<String, Variable> variableMap = new HashMap<>();

            // <VARIABLE> tags
            NodeList varNodes = document.getElementsByTagName("VARIABLE");
            for (int i = 0; i < varNodes.getLength(); i++) {
                Node varNode = varNodes.item(i);

                if (varNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element vElement = (Element) varNode;
                    String name = vElement.getElementsByTagName("NAME").item(0).getTextContent();
                    Variable variable = new Variable(name);
                    variableMap.put(name, variable);
                }
            }

            // <DEFINITION> tags
            NodeList defNodes = document.getElementsByTagName("DEFINITION");
            for (int i = 0; i < defNodes.getLength(); i++) {
                Node defNode = defNodes.item(i);

                if (defNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element dElement = (Element) defNode;

                    String forVariable = dElement.getElementsByTagName("FOR").item(0).getTextContent();
                    Variable variable = variableMap.get(forVariable);

                    // <GIVEN> tags
                    NodeList givenNodes = document.getElementsByTagName("GIVEN");
                    for (int j = 0; j < givenNodes.getLength(); j++) {
                        String givenVariable = givenNodes.item(j).getTextContent();
                        Variable parentVariable = variableMap.get(givenVariable);
                        variable.addParent(parentVariable);
                        parentVariable.addChild(variable);
                    }

                    // <TABLE> tag
                    String tableContent = dElement.getElementsByTagName("TABLE").item(0).getTextContent();
                    String[] probabilities = tableContent.split("\\s+");
//                    variable.updateCPT(probabilities);
                    System.out.println(tableContent);

                    // convert probability strings to floats
                    float[] numbers = new float[probabilities.length];
                    for (int num = 0; num < numbers.length; i++){
                        numbers[num] = Float.parseFloat(probabilities[num]);
                    }

                    // update the CPT of variable with the probabilities
                    variable.updateCPT(variable.getParents(), numbers);

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return false;
    }

}


