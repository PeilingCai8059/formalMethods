package modelCheckCTL.controller;

import java.io.*;

public class FileParser {

    public static String fileToString(File fc){
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fc))) {
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            //delete after debug
            if(!Character.isLetter(sb.charAt(0))){
                sb.substring(1);
            };
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

}
