package org.elevenqtwo.util;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ResourceLoader {
    public static String loadResource(String fileName) throws Exception {
        String result;
        try (InputStream in = ResourceLoader.class.getResourceAsStream(fileName)) {
            assert in!= null;
            try (Scanner scanner = new Scanner(in, StandardCharsets.UTF_8)) {
                result = scanner.useDelimiter("\\A").next();
            }
        }

        return result;
    }
}