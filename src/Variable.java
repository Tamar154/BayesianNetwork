import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Variable {
    private String name;
    //    private Map<Set<String>, Float> cpt;
    private CPT cpt = new CPT();
    private List<Variable> parents = new ArrayList<>();
    private List<Variable> children = new ArrayList<>();

    private List<String> outcomes = new ArrayList<>();


    //    public Variable(String name, int[][] cpt, List<Variable> parents, List<Variable> children) {
    public Variable(String name) {
        this.name = name;
    }

    public void updateCPT(List<Variable> parents, float[] probabilities) {
        String variableName = "";
        String outcome = "";
        String target = "";
        float probability;
        int i = 0;
        for (Variable parent : this.parents) {
            variableName = parent.getName();


        }
//        this.cpt.updateCPT(variable, value, target, probability);
    }

    public void addParent(Variable parentVariable) {
    }

    public void addChild(Variable variable) {
    }


    /****************** Getters and Setters ******************/
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CPT getCpt() {
        return cpt;
    }

    public void setCpt(CPT cpt) {
        this.cpt = cpt;
    }

    public List<Variable> getParents() {
        return parents;
    }

    public void setParents(List<Variable> parents) {
        this.parents = parents;
    }

    public List<Variable> getChildren() {
        return children;
    }

    public void setChildren(List<Variable> children) {
        this.children = children;
    }

    public List<String> getOutcomes() {
        return outcomes;
    }

    public void setOutcomes(List<String> outcomes) {
        this.outcomes = outcomes;
    }
}
