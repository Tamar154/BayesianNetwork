import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CPT {
    /*
    The key of the map will be the list outcomes (each outcome is T/F for example)
    The value of the map will be the corresponding probability.
     */
    private Map<List<String>, Float> cpt;

    public CPT() {
        this.cpt = new LinkedHashMap<>();
    }

    public void updateCPT(Variable variable, List<Variable> parents, float[] probabilities) {
        List<String> varOutcomes = variable.getOutcomes();
        List<List<String>> outcomes = new ArrayList<>();

        generateOutcomes(outcomes, new ArrayList<>(), variable, parents, 0);

        // Save the entries with the corresponding probability
        for (int i = 0; i < probabilities.length; i++) {
            this.cpt.put(outcomes.get(i), probabilities[i]);
        }
    }

    private void generateOutcomes(List<List<String>> outcomes, List<String> current, Variable variable, List<Variable> parents, int index) {
        if (index == parents.size()) {
            for (String outcome : variable.getOutcomes()) {
                List<String> newOutcome = new ArrayList<>(current);
                newOutcome.add(variable.getName() + "=" + outcome);
                outcomes.add(newOutcome);
            }
            return;
        }

        Variable parent = parents.get(index);
        for (String outcome : parent.getOutcomes()) {
            List<String> newCurrent = new ArrayList<>(current);
            newCurrent.add(parent.getName() + "=" + outcome);
            generateOutcomes(outcomes, newCurrent, variable, parents, index + 1);
        }
    }

    @Override
    public String toString() {
        StringBuilder cptString = new StringBuilder();
        for (Map.Entry<List<String>, Float> entry : cpt.entrySet()) {
            List<String> key = entry.getKey();
            float probability = entry.getValue();
            cptString.append(key).append(" : ").append(probability).append("\n");
        }
        return cptString.toString();
    }

    public Map<List<String>, Float> getTable() {
        return this.cpt;
    }
}
