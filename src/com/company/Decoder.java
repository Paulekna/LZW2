package com.company;

/**
 -----Decoder Java File-------
 @author Karthikeyan Thorali Krishnmaurthy Ragunath
 @version 1.0
 @student ID 800936747
 **/

import java.io.*;

import java.util.*;

public class Decoder {


    private static String File_Input = null;
    private static double MAX_TABLE_SIZE; //Max Table size is based on the bit length input.
    private static String LZWfilename;



    /* Decodes the the compressed file to a decoded input file.
     * @param file_Input2 //The name of compressed file.
     * @param bit_Length  //Provided as user input.
     * @throws IOException */


    public static void Decode_String(String file_Input2, double bit_Length) throws IOException {




        List<Integer> get_compress_values = new ArrayList<Integer>();
        int table_Size = 256;


        BufferedReader br = null;
        InputStream inputStream  = new FileInputStream(file_Input2);
        Reader inputStreamReader = new InputStreamReader(inputStream, "ISO-8859-15"); // The Charset UTF-16BE is used to read the 16-bit compressed file.

        br = new BufferedReader(inputStreamReader);

        double value=0;

        // reads to the end of the stream
        while((value = br.read()) != -1)
        {
            get_compress_values.add((int) value);
        }

        br.close();

        Map<Integer, String> TABLE = new HashMap<Integer, String>();
        for (int i = 0; i < 256; i++)
            TABLE.put(i, "" + (char) i);

        String Encode_values = "" + (char) (int) get_compress_values.remove(0);

        StringBuffer decoded_values = new StringBuffer(Encode_values);

        String get_value_from_table = null;
        for (int check_key : get_compress_values) {

            if (TABLE.containsKey(check_key))
                get_value_from_table = TABLE.get(check_key);
            else if (check_key == table_Size)
                get_value_from_table = Encode_values + Encode_values.charAt(0);

            decoded_values.append(get_value_from_table);

            if(table_Size < 256)
                TABLE.put(table_Size++, Encode_values + get_value_from_table.charAt(0));

            Encode_values = get_value_from_table;
        }

        //Create_decoded_file(decoded_values.toString());



    }

/*
@param String , This hold the decoded text.
@throws IOException
*/

    private static void Create_decoded_file(String decoded_values, String filename) throws IOException {


        LZWfilename = filename.substring(0,filename.indexOf("."))+"musu";

        FileWriter writer = new FileWriter(LZWfilename, true);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);


        try {

            bufferedWriter.write(decoded_values);

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        bufferedWriter.flush();

        bufferedWriter.close();
    }

    public static void main(String[] args) throws IOException {

        File_Input = args[0];
        List<Integer> get_compress_values = new ArrayList<Integer>();
        int table_Size = 255;


        BufferedReader br = null;
        InputStream inputStream  = new FileInputStream(File_Input);
        Reader inputStreamReader = new InputStreamReader(inputStream, "UTF-16"); // The Charset UTF-16BE is used to read the 16-bit compressed file.

        br = new BufferedReader(inputStreamReader);

        double value=0;

        // reads to the end of the stream
        while((value = br.read()) != -1)
        {
            get_compress_values.add((int) value);
        }

        br.close();


        int dictSize = 256;
        Map<Integer,String> dictionary = new HashMap<Integer,String>();
        for (int i = 0; i < 256; i++)
            dictionary.put(i, "" + (char)i);

        String w = "" + (char)(int)get_compress_values.remove(0);
        StringBuffer result = new StringBuffer(w);
        for (int k : get_compress_values) {
            String entry;
            if (dictionary.containsKey(k))
                entry = dictionary.get(k);
            else if (k == dictSize)
                entry = w + w.charAt(0);
            else

                throw new IllegalArgumentException("Bad compressed k: " + k);

            result.append(entry);

            // Add w+entry[0] to the dictionary.
            dictionary.put(dictSize++, w + entry.charAt(0));

            w = entry;
        }
        try(FileOutputStream fileOutputStream = new FileOutputStream(File_Input+"lelele")){
            for(int i =0;i<result.length();i++){
                fileOutputStream.write((int)result.charAt(i));
            }
        }
    }
}