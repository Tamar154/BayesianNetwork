import java.io.File;

public class main {
    public static void main(String[] args) {
//        File file = new File("src/alarm_net.xml");
//        BayesianNetwork bn = new BayesianNetwork(file);

        CPT cpt = new CPT();
//        cpt.updateCPT("E", "T", 0.002F);
//        cpt.updateCPT("E", "F", 0.998F);


        System.out.println(cpt);
    }
}
