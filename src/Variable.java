import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Variable {
    private String name;
    //    private Map<Set<String>, Float> cpt;
    private CPT cpt = new CPT();
    private List<Variable> parents = new ArrayList<>();
    private List<Variable> children = new ArrayList<>();
    private List<String> outcomes = new ArrayList<>();


    public Variable(String name) {
        this.name = name;
    }

    public Variable(Variable original) {
        this.name = original.name;
        this.cpt = new CPT(original.cpt); // Assuming CPT has a copy constructor
        this.parents = original.parents.stream().map(Variable::new).collect(Collectors.toList());
        this.children = original.children.stream().map(Variable::new).collect(Collectors.toList());
        this.outcomes = new ArrayList<>(original.outcomes);
    }

    public void updateOutcome(String outcome) {
        this.outcomes.add(outcome);
    }

    public void addParent(Variable parent) {
        this.parents.add(parent);
    }

    public void addChild(Variable child) {
        this.children.add(child);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Variable variable = (Variable) obj;
        return Objects.equals(name, variable.name);
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