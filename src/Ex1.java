import java.io.*;

public class Ex1 {
    public static void main(String[] args) {
        try {
            File input = new File("input.txt");
            InputParser parser = new InputParser(input);

            BufferedReader reader = new BufferedReader(new FileReader(input));
            FileWriter fileWriter = new FileWriter("output.txt", true);

            String line;
            while ((line = reader.readLine()) != null) {
                BayesianNetwork bn = parser.bn;
                if (parser.isBayesBallQuery(line)) {
                    BayesBall bayesBall = new BayesBall(bn, line);
                    if (bayesBall.BayesBallAlgo()) {
                        fileWriter.write("yes\n");
                    } else {
                        fileWriter.write("no\n");
                    }
                } else if (parser.isVariableEliminationQuery(line)) {
                    VariableElimination ve = new VariableElimination(bn, line);
                    fileWriter.write(ve.variableEliminationAlgo() + "\n");
                }
            }

            reader.close();
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

