import java.io.File;

public class Ex1 {
    public static void main(String[] args) {
        File input = new File("input10.txt");
        InputParser parser = new InputParser(input);

        BayesianNetwork bn = parser.bn;

        System.out.println("bayesBallQueries");
        for (String query : parser.bayesBallQueries) {
            BayesBall bayesBall = new BayesBall(bn, query);
            System.out.println(bayesBall.BayesBallAlgo());
        }

        System.out.println("variableEliminationQueries");
        for (String ve : parser.variableEliminationQueries) {
            System.out.println(ve);
        }

    }
}
