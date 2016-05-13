package com.v4ivstudio.whistler;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Vaibhav Sharma on 5/12/2016.
 **/
public class CommonUtils {
    public static String convertInputStreamReaderToString(InputStreamReader inputStreamReader) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }
        bufferedReader.close();
        return stringBuilder.toString();
    }
}
