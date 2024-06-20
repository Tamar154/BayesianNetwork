import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Factor {
    private Set<String> variables;
    private Map<Map<String, String>, Float> values;

    public Factor(Set<String> variables) {
        this.variables = new HashSet<>(variables);
        this.values = new HashMap<>();
    }

    public Set<String> getVariables() {
        return variables;
    }

    public Map<Map<String, String>, Float> getValues() {
        return values;
    }

    public void setValues(Map<Map<String, String>, Float> values) {
        this.values = values;
    }

    public void addValue(Map<String, String> assignment, Float value) {
        this.values.put(assignment, value);
    }

    public Factor multiply(Factor other, VariableElimination ve) {
        Set<String> newVariables = new HashSet<>(this.variables);
        newVariables.addAll(other.variables);

        Factor result = new Factor(newVariables);

        for (Map<String, String> assignment1 : this.values.keySet()) {
            for (Map<String, String> assignment2 : other.values.keySet()) {
                Map<String, String> combinedAssignment = new HashMap<>(assignment1);
                boolean consistent = true;

                for (String var : assignment2.keySet()) {
                    if (combinedAssignment.containsKey(var) &&
                            !combinedAssignment.get(var).equals(assignment2.get(var))) {
                        consistent = false;
                        break;
                    }
                    combinedAssignment.put(var, assignment2.get(var));
                }

                if (consistent) {
                    float value = this.values.get(assignment1) * other.values.get(assignment2);
                    ve.incrementMulCount();
                    result.addValue(combinedAssignment, value);
                }
            }
        }

        return result;
    }

    public Factor sumOut(String variable, VariableElimination ve) {
        Set<String> newVariables = new HashSet<>(this.variables);
        newVariables.remove(variable);

        Factor result = new Factor(newVariables);
        Map<Map<String, String>, Float> newValues = new HashMap<>();

        for (Map<String, String> assignment : this.values.keySet()) {
            Map<String, String> reducedAssignment = new HashMap<>(assignment);
            reducedAssignment.remove(variable);

            //  Add the current value if the reduced assignment is already present
            if (newValues.containsKey(reducedAssignment)) {
                newValues.put(reducedAssignment, newValues.get(reducedAssignment) + this.values.get(assignment));
                ve.incrementAddCount();
            } else {
                newValues.put(reducedAssignment, this.values.get(assignment));
            }
        }

        result.setValues(newValues);
        return result;
    }

    public void normalize(VariableElimination ve) {
        float total = 0;
        for (float value : values.values()) {
            total += value;
            ve.incrementAddCount();
        }
        for (Map.Entry<Map<String, String>, Float> entry : values.entrySet()) {
            values.put(entry.getKey(), entry.getValue() / total);
        }
    }

    public Boolean isOneValued() {
        return this.values.size() == 1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Map<String, String>, Float> entry : values.entrySet()) {
            sb.append(entry.getKey()).append(" : ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }
}