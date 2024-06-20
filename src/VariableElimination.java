import java.util.*;

public class VariableElimination {
    BayesianNetwork bNet;
    List<Factor> factors;
    private String queryVariable;
    private Map<String, String> evidences;
    private List<String> hidden;
    private int additionCount = 0;
    private int multiplicationCount = 0;

    public VariableElimination(BayesianNetwork bNet, String query) {
        this.bNet = bNet;
        this.evidences = new HashMap<>();
        this.hidden = new LinkedList<>();
        this.factors = new LinkedList<>();
        parseQuery(query);
    }

    public String variableEliminationAlgo() {
        Set<String> irrelevantVariables = findIrrelevantVariables();

        // Initialize factors from CPTs of the network
        for (Variable variable : bNet.getVariables()) {
            if (!irrelevantVariables.contains(variable.getName())) {
                Factor factor = createFactor(variable);
                if (!factor.isOneValued()) {
                    factors.add(factor);
                }
            }
        }

        // Restrict factors based on evidence
        for (Map.Entry<String, String> evidence : evidences.entrySet()) {
            restrictFactor(evidence.getKey(), evidence.getValue());
        }

        // Eliminate hidden variables
        for (String hiddenVar : hidden) {
            if (!hiddenVar.equals(queryVariable.split("=")[0])) {
                eliminate(hiddenVar);
            }
        }

        // Multiply remaining factors
        Factor result = factors.get(0);
        for (int i = 1; i < factors.size(); i++) {
            result = result.multiply(factors.get(i), this);
            if (result.isOneValued()) {
                factors.remove(i);
                i--;
            }
        }

        // Normalize result factor
        result.normalize(this);

        String probability = formatAnswer(result);
        this.additionCount--;
        if (this.multiplicationCount == 0) additionCount = 0;
        return probability + "," + this.additionCount + "," + this.multiplicationCount;
    }

    private Set<String> findIrrelevantVariables() {
        Set<String> irrelevantVariables = new HashSet<>();
        Set<String> relevantVariables = new HashSet<>();

        // Collect query and evidence variables
        relevantVariables.add(queryVariable.split("=")[0]);
        relevantVariables.addAll(evidences.keySet());

        // Find ancestors of query and evidence variables
        Queue<String> queue = new LinkedList<>(relevantVariables);
        while (!queue.isEmpty()) {
            String current = queue.poll();
            for (Variable parent : bNet.getVariable(current).getParents()) {
                if (!relevantVariables.contains(parent.getName())) {
                    relevantVariables.add(parent.getName());
                    queue.add(parent.getName());
                }
            }
        }

        // Determine irrelevant variables
        for (Variable variable : bNet.getVariables()) {
            if (!relevantVariables.contains(variable.getName())) {
                irrelevantVariables.add(variable.getName());
            }
        }

        return irrelevantVariables;
    }

    private String formatAnswer(Factor result) {
        for (Map.Entry<Map<String, String>, Float> entry : result.getValues().entrySet()) {
            String[] parts = queryVariable.split("=");
            if (entry.getKey().get(parts[0]).equals(parts[1])) {
                return String.format("%.5f", entry.getValue());
            }
        }
        return "0.00000";
    }

    private void parseQuery(String query) {
        String[] parts = query.split("\\s+");

        if (parts.length < 1) {
            throw new IllegalArgumentException("Invalid query format: " + query);
        }

        // probability expression
        String probabilityExpression = parts[0];

        int startIndexOfQueryVariable = probabilityExpression.indexOf("(") + 1;
        int endIndexOfQueryVariable = probabilityExpression.indexOf("|");

        if (startIndexOfQueryVariable == 0 || endIndexOfQueryVariable == -1) {
            throw new IllegalArgumentException("Invalid probability expression: " + probabilityExpression);
        }

        queryVariable = probabilityExpression.substring(startIndexOfQueryVariable, endIndexOfQueryVariable);

        // Extract evidences (E1=e1, E2=e2, ..., Ek=ek)
        int startIndexOfEvidences = probabilityExpression.indexOf("|") + 1;
        int endIndexOfEvidences = probabilityExpression.indexOf(")");

        if (startIndexOfEvidences == 0 || endIndexOfEvidences == -1 || startIndexOfEvidences >= endIndexOfEvidences) {
            throw new IllegalArgumentException("Invalid probability expression: " + probabilityExpression);
        }

        String evidencesString = probabilityExpression.substring(startIndexOfEvidences, endIndexOfEvidences);
        String[] evidencesArray = evidencesString.split(",");

        for (String evidence : evidencesArray) {
            String[] evidenceParts = evidence.trim().split("=");
            evidences.put(evidenceParts[0], evidenceParts[1]);
        }

        // Check for hidden variables (H1-H2-...-Hj)
        if (parts.length > 1) {
            String hiddenVariables = parts[1];
            hidden.addAll(List.of(hiddenVariables.split("-")));
        }
    }

    private Factor createFactor(Variable variable) {
        List<String> factorVariables = new LinkedList<>();
        factorVariables.add(0, variable.getName());
        for (Variable parent : variable.getParents()) {
            factorVariables.add(parent.getName());
        }

        Factor factor = new Factor(factorVariables);

        for (Map.Entry<List<String>, Float> entry : variable.getCpt().getTable().entrySet()) {
            Map<String, String> assignment = new HashMap<>();
            for (String outcome : entry.getKey()) {
                String[] parts = outcome.split("=");
                assignment.put(parts[0], parts[1]);
            }
            factor.addValue(assignment, entry.getValue());
        }

        return factor;
    }

    private void restrictFactor(String variable, String value) {
        List<Factor> updatedFactors = new LinkedList<>();
        for (Factor factor : factors) {
            if (factor.getVariables().contains(variable)) {
                Factor restrictedFactor = new Factor(new LinkedList<>(factor.getVariables()));
                for (Map.Entry<Map<String, String>, Float> entry : factor.getValues().entrySet()) {
                    if (value.equals(entry.getKey().get(variable))) {
                        Map<String, String> newAssignment = new HashMap<>(entry.getKey());
                        newAssignment.remove(variable);
                        restrictedFactor.addValue(newAssignment, entry.getValue());
                    }
                }

                if (!restrictedFactor.isOneValued() && restrictedFactor.getVariables().size() > 1)
                    updatedFactors.add(restrictedFactor);
            } else {
                updatedFactors.add(factor);
            }
        }
        factors = updatedFactors;
    }

    private void eliminate(String variable) {
        List<Factor> newFactors = new LinkedList<>();
        List<Factor> factorsToMultiply = new LinkedList<>();

        for (Factor factor : factors) {
            if (factor.getVariables().contains(variable)) {
                factorsToMultiply.add(factor);
            } else {
                newFactors.add(factor);
            }
        }

        if (!factorsToMultiply.isEmpty()) {
            // Sort the factors
            factorsToMultiply.sort(Comparator.<Factor>comparingInt(f -> f.getVariables().size()).thenComparingInt(f -> f.getVariables().stream().mapToInt(v -> v.chars().sum()).sum()));

            Factor product = factorsToMultiply.get(0);
            for (int i = 1; i < factorsToMultiply.size(); i++) {
                product = product.multiply(factorsToMultiply.get(i), this);
            }
            Factor summedOutFactor = product.sumOut(variable, this);

            if (!summedOutFactor.isOneValued()) newFactors.add(summedOutFactor);
        }

        if (newFactors.isEmpty()) {
            newFactors = factors;
        }

        factors = newFactors;
    }

    public void incrementAddCount() {
        this.additionCount++;
    }

    public void incrementMulCount() {
        this.multiplicationCount++;
    }
}



