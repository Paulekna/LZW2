package com.company;
/**
 -----Encoder Java File-------
 @author Karthikeyan Thorali Krishnmaurthy Ragunath
 @version 1.0
 @student ID 800936747
 **/
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Encoder {

    private static String File_Input = null;
    private static double MAX_TABLE_SIZE; //Max Table size is based on the bit length input.
    private static String LZWfilename;


    /** Compress a string to a list of output symbols and then pass it for compress file creation.
     * @param Bit_Length //Provided as user input.
     * @param input_string //Filename that is used for encoding.
     * @throws IOException */

    public static void Encode_string(String input_string, double Bit_Length) throws IOException {

        MAX_TABLE_SIZE = Math.pow(2, Bit_Length);

        double table_Size =  255;

        Map<String, Integer> TABLE = new HashMap<String, Integer>();

        for (int i = 0; i < 255 ; i++)
            TABLE.put("" + (char) i, i);

        String initString = "";

        List<Integer> encoded_values = new ArrayList<Integer>();

        for (char symbol : input_string.toCharArray()) {
            String Str_Symbol = initString + symbol;
            if (TABLE.containsKey(Str_Symbol))
                initString = Str_Symbol;
            else {
                encoded_values.add(TABLE.get(initString));

                if(table_Size < MAX_TABLE_SIZE)
                    TABLE.put(Str_Symbol, (int) table_Size++);
                initString = "" + symbol;
            }
        }

        if (!initString.equals(""))
            encoded_values.add(TABLE.get(initString));

        //CreateLZWfile(encoded_values);

    }


/*
@param encoded_values , This hold the encoded text.
@throws IOException
*/

    private static void CreateLZWfile(List<Integer> encoded_values, String filename) throws IOException {

        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename),"UTF-16")); //The Charset UTF-16BE is  ISO-8859-15 used to write as 16-bit compressed file

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Iterator<Integer> Itr = encoded_values.iterator();
            while (Itr.hasNext()) {
                int a = Itr.next();
                if(a>65535){
                    System.out.println(a);
                }
                out.write(a);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        out.flush();
        out.close();
    }


    public static void main(String[] args) throws IOException {

// read prameters
        String filename = args[0];
        int dictSize = Integer.parseInt(args[1]);
        String param = args[2];

        // compress or decompress
        String rename = filename;
        boolean compress;
        if (param.equals("-c")) {
            compress = true;
            rename = rename+".dlap";
        } else if (param.equals("-d")) {
            compress = false;
            if(filename.contains(".dlap"))
                rename = filename.replace(".dlap","");
        }


        // dictionary
        Map<String, Integer> dictionary = new HashMap<>();
        for (int i = 0; i < dictSize; i++) {
            dictionary.put("" + (char) i, i);
        }

        List<Integer> result = new ArrayList<>();
        String w = "";
        // read file byte by byte with decimal representation
        try (FileInputStream fileInputStream = new FileInputStream(filename)) {
            int c;
            while (( c = fileInputStream.read()) != -1) {
                String wc= w + (char) c;
                if (dictionary.containsKey(wc)) {
                    w = wc;
                }
                else {
                    dictionary.put(wc, dictSize++);
                    result.add(dictionary.get(w));
                    // Add wc to the dictionary.
                    w = "" + (char) c;
                }
            }
            // Output the code for w.
            if (!w.equals("")) {
                result.add(dictionary.get(w));
            }
        } catch (IOException ex) {
            System.out.println("Cannot read from file!"+ex);
        }
        CreateLZWfile(result, rename);
    }

    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
}