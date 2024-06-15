import java.util.*;

public class BayesBall {
    private Variable start;
    private Variable end;
    private BayesianNetwork bNet;
    private Queue<VariablePair> queue;
    private List<VariablePair> visited;
    private List<Variable> evidences;

    public BayesBall(BayesianNetwork bNet, String query) {
        this.bNet = bNet;
        this.queue = new LinkedList<>();
        this.visited = new ArrayList<>();
        this.evidences = new ArrayList<>();
        parseQuery(query);
    }

    public boolean BayesBallAlgo() {
        queue.add(new VariablePair(start, "none"));

        while (!queue.isEmpty()) {
            VariablePair current = queue.poll();

            // if already visited - continue
            if (visited.contains(current))
                continue;
            visited.add(current);

            // path found
            if (current.variable.equals(end))
                return false;

            // handle evidence variable
            if (evidences.contains(current.variable)) {
//                if (current.direction.equals("up") || current.direction.equals("none")) {
                for (Variable parent : current.variable.getParents()) {
                    queue.add(new VariablePair(parent, "up"));
                }
//                }
            } else { // handle non-evidence variable
                if (current.direction.equals("up") || current.direction.equals("none")) {
                    for (Variable parent : current.variable.getParents()) {
                        queue.add(new VariablePair(parent, "up"));
                    }
                    for (Variable child : current.variable.getChildren()) {
                        queue.add(new VariablePair(child, "down"));
                    }
                }

                if (current.direction.equals("down")) {
                    for (Variable child : current.variable.getChildren()) {
                        queue.add(new VariablePair(child, "down"));
                    }

                }
            }
        }
        return true;
    }

    public void parseQuery(String query) {
        String[] parts = query.split("\\|");
        String[] nodes = parts[0].split("-");
        this.start = bNet.getVariable(nodes[0]);
        this.end = bNet.getVariable(nodes[1]);

        if (parts.length > 1) {
            String[] conditions = parts[1].split(",");
            for (String condition : conditions) {
                Variable evidence = bNet.getVariable(condition.split("=")[0]);
                if (evidence != null)
                    this.evidences.add(evidence);
            }
        }
    }
}