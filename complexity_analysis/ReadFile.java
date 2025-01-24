import java. lang.*;
import java.io.*;
import java.util.*;

public class ReadFile {

    public static int[] getData(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            List<Integer> salaries = new ArrayList<>();
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] splitted = line.split(",");
                salaries.add(Integer.parseInt(splitted[6]));
            }
            reader.close();
            int[] resultArray = new int[salaries.size()];
            for (int i = 0; i < salaries.size(); i++) {
                resultArray[i] = salaries.get(i);
            }
            return resultArray;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}