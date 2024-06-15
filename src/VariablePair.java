public class VariablePair {
    public Variable variable;
    public String direction;

    public VariablePair(Variable variable, String direction) {
        this.variable = variable;
        this.direction = direction;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        VariablePair that = (VariablePair) obj;
        if (!variable.equals(that.variable))
            return false;
        return direction.equals(that.direction);
    }

}
