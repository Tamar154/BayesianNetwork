import java.util.*;

public class CPT {
    /*
    The key of the map will be the set outcomes (each outcome is T/F for example)
    The value of the map will be the corresponding probability.
     */
    private Map<Set<String>, Float> cpt;

    public CPT() {
        this.cpt = new HashMap<>();
    }

    public void updateCPT(Variable variable, List<Variable> parents, float[] probabilities) {
        List<String> varOutcomes = variable.getOutcomes();

        if (parents.size() != 0) { // variable with dependencies
            List<String> variableOutcomes = variable.getOutcomes();
            List<Set<String>> outcomes = new ArrayList<>();

            // Initialize with outcomes of the variable
            for (int i = 0; i < variableOutcomes.size(); i++) {
                Set<String> outcome = new HashSet<>();
                outcome.add(variable.getName() + "=" + variableOutcomes.get(i));
                outcomes.add(outcome);
            }

            // Traverse the parents from the end of the List
            for (int i = parents.size() - 1; i >= 0; i--) {
                Variable parent = parents.get(i);
                List<String> parentOutcomes = parent.getOutcomes();
                List<Set<String>> newOutcomes = new ArrayList<>();

                for (String parentOutcome : parentOutcomes) {
                    for (Set<String> prevOutcome : outcomes) {
                        Set<String> newOutcome = new HashSet<>(prevOutcome);
                        newOutcome.add(parent.getName() + "=" + parentOutcome);
                        newOutcomes.add(newOutcome);
                    }
                }

                outcomes = newOutcomes;
            }

            // Save the entries with the corresponding probability
            for (int i = 0; i < probabilities.length; i++) {
                this.cpt.put(outcomes.get(i), probabilities[i]);
            }
        } else { // no dependencies
            for (int i = 0; i < probabilities.length; i++) {
                Set<String> outcome = new HashSet<>();
                outcome.add(variable.getName() + "=" + varOutcomes.get(i));
                this.cpt.put(outcome, probabilities[i]);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder cptString = new StringBuilder();
        for (Map.Entry<Set<String>, Float> entry : cpt.entrySet()) {
            Set<String> key = entry.getKey();
            float probability = entry.getValue();
            cptString.append(key).append(" : ").append(probability).append("\n");
        }
        return cptString.toString();
    }
}
