import java.io.*;

public class AuthUtil {

    private static final String FILE_NAME = "users.csv";

    public static boolean validateLogin(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 2) {
                    if (values[0].trim().equals(username) && values[1].trim().equals(password)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading users.csv: " + e.getMessage());
        }
        return false;
    }


    public static boolean signUp(String username, String password) {

        if (validateLogin(username, password)) {
            return false; 
        }

        try (FileWriter fw = new FileWriter(FILE_NAME, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            out.println(username + "," + password);
            return true;

        } catch (IOException e) {
            System.out.println("Error writing to users.csv: " + e.getMessage());
        }
        return false;
    }
}

