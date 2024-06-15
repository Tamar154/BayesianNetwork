import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputParser {
    public BayesianNetwork bn;
    public List<String> bayesBallQueries = new ArrayList<>();
    public List<String> variableEliminationQueries = new ArrayList<>();

    public InputParser(File file) {
        try {
            BufferedReader reader;
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            // Initialize BayesianNetwork from the first line
            this.bn = new BayesianNetwork(new File(line));

            // parse queries
            line = reader.readLine();
            while (line != null) {
                if (isBayesBallQuery(line))
                    this.bayesBallQueries.add(line);
                else if (isVariableEliminationQuery(line))
                    this.variableEliminationQueries.add(line);
                else
                    System.out.println("doesn't match any known pattern");

                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isBayesBallQuery(String query) {
        String bbRegex = "^[a-zA-Z0-9]+-[a-zA-Z0-9]+\\|([a-zA-Z0-9]+=\\w+(,[a-zA-Z0-9]+=\\w+)*)?$";
        Pattern pattern = Pattern.compile(bbRegex);
        Matcher matcher = pattern.matcher(query);
        return matcher.matches();
    }

    public boolean isVariableEliminationQuery(String query) {
        String veRegex = "^P\\(.*\\) [A-Z](-[A-Z])*(-[A-Z])?$";
        Pattern pattern = Pattern.compile(veRegex);
        Matcher matcher = pattern.matcher(query);
        return matcher.matches();
    }

}
