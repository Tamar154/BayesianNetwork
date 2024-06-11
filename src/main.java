import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class main {
    public static void main(String[] args) {
        File file = new File("src/alarm_net.xml");
//        File file = new File("src/alarm_net2.xml");
        BayesianNetwork bn = new BayesianNetwork(file);

        for (Variable var : bn.getVariables()){
            System.out.println("******CPT for " + var.getName() + ":  ******");
            System.out.println(var.getCpt());
        }

    }
}
