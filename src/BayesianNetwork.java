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
import java.util.stream.Collectors;

public class BayesianNetwork {
    //    private Map<Variable, List<Variable>> adjacencyList;
    private List<Variable> variables;

    public BayesianNetwork(File file) {
        variables = new ArrayList<>();
        parseXML(file);
    }

    public BayesianNetwork(BayesianNetwork original) {
        this.variables = original.variables.stream().map(Variable::new).collect(Collectors.toList());
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
    private void parseXML(File xmlFile) {
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

                    NodeList outcomeNodes = vElement.getElementsByTagName("OUTCOME");
                    for (int j = 0; j < outcomeNodes.getLength(); j++) {
                        variable.updateOutcome(outcomeNodes.item(j).getTextContent());
                    }

//                    // Check that outcomes are ok - can be deleted when done
//                    System.out.println(variable.getName() + " outcomes: ");
//                    for (String outcome : variable.getOutcomes()) {
//                        System.out.println(outcome);
//                    }

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
                    NodeList givenNodes = dElement.getElementsByTagName("GIVEN");
                    for (int j = 0; j < givenNodes.getLength(); j++) {
                        String givenVariable = givenNodes.item(j).getTextContent();
                        Variable parentVariable = variableMap.get(givenVariable);
                        variable.addParent(parentVariable);
                        parentVariable.addChild(variable);
                    }

                    // <TABLE> tag
                    String tableContent = dElement.getElementsByTagName("TABLE").item(0).getTextContent();
                    String[] probabilities = tableContent.split("\\s+");

                    // convert probability strings to floats
                    float[] numbers = new float[probabilities.length];
                    for (int num = 0; num < numbers.length; num++) {
                        numbers[num] = Float.parseFloat(probabilities[num]);
                    }

                    // update the CPT of variable with the probabilities
                    variable.getCpt().updateCPT(variable, variable.getParents(), numbers);
                    variables.add(variable);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /****************** Getters and Setters ******************/
    public List<Variable> getVariables() {
        return variables;
    }

    public Variable getVariable(String name) {
        for (Variable variable : this.variables) {
            if (variable.getName().equals(name))
                return variable;
        }
        return null;
    }
}


