package vehiclesurvey.utils;

import vehiclesurvey.App;
import vehiclesurvey.Models.FactItem;
import vehiclesurvey.Models.SurveyFormat;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Abby on 5/05/2017.
 */
public class FileUtils {

    public Reader resourceReader(String resource) throws FileNotFoundException {
        return new InputStreamReader(App.class.getClassLoader().getResourceAsStream(resource));
    }

    public Reader fileReader(String fileName) throws Exception {
        try {
            return new FileReader(fileName);
        }
        catch(FileNotFoundException e) {
            throw new AppException("Error reading file : " + fileName);
        }
    }

    public List<String> validateInputStream(Reader input, String regex) throws Exception {
        List<String> inputLines = new ArrayList<>();
        String line;
        BufferedReader reader = new BufferedReader(input);
        while ((line = reader.readLine()) != null) {
            if (isValidPattern(line, regex)) {
                inputLines.add(line.toUpperCase(Locale.ENGLISH));
            } else {
                throw new AppException("Invalid input line : " + line);
            }
        }
        if(inputLines.isEmpty()) {
            throw new AppException("Empty Input");
        }
        return inputLines;
    }

    private boolean isValidPattern(String data, String regex){
        Pattern validator = Pattern.compile(regex);
        return validator.matcher(data).matches();
    }

    public String convertFactToFileFormat(Map<Integer, List<FactItem>> data, SurveyFormat fileFormat) throws Exception {
        if(data== null || data.isEmpty()) {
            throw new AppException("Cannot convert empty facts to file formatted string");
        }
        StringBuilder result = new StringBuilder();

        result.append(fileFormat.getType()).append(" going ").append(fileFormat.getDirection()).append(":: \n");

        data.forEach((integer, factItems) -> {
            result.append("\t").append(integer).append("\t").append(fileFormat.getInterval()).append("\n");
            if(factItems == null || factItems.size() == 0) {
                result.append("No Data\n");
            }
            else{
                factItems.forEach(factItem -> {
                    result.append("\t\t").append(factItem.getTimeIntervalString())
                      .append("::").append("\t").append(factItem.getFact()).append("\n");
                });
            }
        });

        return result.toString();
    }

    public String getFilePath(String name) throws Exception{
        File folder = new File("out/survey");

        if (!folder.exists()) {
            try{
                folder.mkdirs();
            }
            catch(SecurityException e) {
                throw new AppException("Unable to create folder : " + name);
            }
        }
        return "out/survey/" + name + ".txt";
    }

    public void writeTextFile(String filePath, String content) throws Exception {
        File file = new File(filePath);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            if (!file.exists()) {
                file.createNewFile();
            }
            byte[] contentInBytes = content.getBytes();
            fos.write(contentInBytes);
            fos.flush();
            fos.close();

        } catch (IOException e) {
            throw new AppException("Unable to write to file : " + filePath);
        }
    }


}
