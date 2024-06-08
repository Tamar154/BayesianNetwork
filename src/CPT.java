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

    public void updateCPT(List<Variable> parents, float probability) {
        if (parents != null){
//            for (String outcome : )
            for (Variable parent: parents){
                List<String> outcomes = parent.getOutcomes();
            }
        }
        else{

        }
    }

    private Set<String> createKey(String[] outcomes) {
        return new HashSet<>(Arrays.asList(outcomes));
    }


    @Override
    public String toString() {
        StringBuilder cptString = new StringBuilder();
        for (Map.Entry<Set<String>, Float> entry : cpt.entrySet()) {
            Set<String> key = entry.getKey();
            float probability = entry.getValue();
//            System.out.println(key + " : " + probability);
            cptString.append(key).append(" : ").append(probability).append("\n");
        }
        return cptString.toString();
    }


//    public static void main(String[] args) {
//        CPT cpt = new CPT();
//        cpt.updateCPT(new String[]{"A=T", "B=T", "C=T"}, 0.9f);
//        cpt.updateCPT(new String[]{"A=T", "B=T", "C=F"}, 0.1f);
//        cpt.updateCPT(new String[]{"A=T", "B=F", "C=T"}, 0.7f);
//        cpt.updateCPT(new String[]{"A=T", "B=F", "C=F"}, 0.3f);
//        cpt.updateCPT(new String[]{"A=F", "B=T", "C=T"}, 0.4f);
//        cpt.updateCPT(new String[]{"A=F", "B=T", "C=F"}, 0.6f);
//        cpt.updateCPT(new String[]{"A=F", "B=F", "C=T"}, 0.2f);
//        cpt.updateCPT(new String[]{"A=F", "B=F", "C=F"}, 0.8f);
//
//        System.out.println(cpt);
//    }
}
