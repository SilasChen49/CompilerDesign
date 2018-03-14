package lexical;

import java.io.*;

class Cleaner{

    static void clean(String fileName){
        try {
            String source = readFile(fileName);
            source = source.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)","");
            PrintStream printStream = new PrintStream(new FileOutputStream(new File("testCases/intermediate")));
            printStream.print(source);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String readFile(String fileName) {

        File file = new File(fileName);

        char[] buffer = null;

        try {
            BufferedReader bufferedReader = new BufferedReader( new FileReader(file));

            buffer = new char[(int)file.length()];

            int i = 0;
            int c = bufferedReader.read();

            while (c != -1) {
                buffer[i++] = (char)c;
                c = bufferedReader.read();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new String(buffer);
    }

}    