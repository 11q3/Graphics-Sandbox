package org.elevenqtwo.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ResourceLoader {
    public static String loadResource(String fileName) {
        String result;
        try (InputStream in = ResourceLoader.class.getResourceAsStream(fileName)) {
            assert in != null;
            try (Scanner scanner = new Scanner(in, StandardCharsets.UTF_8)) {
                result = scanner.useDelimiter("\\A").next();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public static List<String> readAllLines(String fileName) {
        List<String> list = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while((line = br.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}