import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Ex1 {
    public static void main(String[] args) {

        try {
            File input = new File("input2.txt");
            InputParser parser = new InputParser(input);

            BayesianNetwork bn = parser.bn;

            FileWriter fileWriter = new FileWriter("output.txt", true);

            for (String query : parser.bayesBallQueries) {
                BayesBall bayesBall = new BayesBall(bn, query);
                if (bayesBall.BayesBallAlgo()) {
                    fileWriter.write("yes\n");
                } else fileWriter.write("no\n");

            }

            for (String query : parser.variableEliminationQueries) {
                VariableElimination ve = new VariableElimination(bn, query);
                fileWriter.write(ve.variableEliminationAlgo() + "\n");
            }

            fileWriter.close();

        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

}

