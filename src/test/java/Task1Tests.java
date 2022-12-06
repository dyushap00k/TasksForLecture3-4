import org.junit.jupiter.api.*;
import task1.Task1;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Task1Tests {
    private static final File IN1 = new File("src/test/resources/input1.xml");
    private static final File OUT1 = new File("src/test/resources/output1.xml");
    private static final File EXAMPLE1 = new File("src/test/resources/example1.xml");
    private static final File IN2 = new File("src/test/resources/input2.xml");
    private static final File OUT2 = new File("src/test/resources/output2.xml");
    private static final File EXAMPLE2 = new File("src/test/resources/example2.xml");
    private static BufferedReader readerExample;
    private static BufferedReader readerOut;

    @AfterEach
    void close() throws IOException {
        readerExample.close();
        readerOut.close();
    }

    /**
     * don`t save tabs in example files, but original method works good!
     * just look in test resources example1/2.xml or "Click to see difference" when test will crush
     */
    @Test
    void testWhenNameAndSurnameAreSeparateThenTheyBecomeOneArgument() throws FileNotFoundException {
        readerOut = new BufferedReader(new FileReader(OUT1));
        readerExample = new BufferedReader(new FileReader(EXAMPLE1));
        Task1.convertArguments(IN1, OUT1);

        StringBuilder inputText = new StringBuilder();
        StringBuilder outputText = new StringBuilder();
        fillBuilder(inputText, readerExample);
        fillBuilder(outputText, readerOut);


        assertEquals(inputText.toString(), outputText.toString());
    }

    /**
     * don`t save tabs in example files, but original method works good!
     * just look in test resources example1/2.xml or "Click to see difference" when test will crush
     */
    @Test
    void testWhenDuplicateNameAndSurnameAreSeparateThenTheyBecomeOneArgument() throws FileNotFoundException {
        readerOut = new BufferedReader(new FileReader(OUT2));
        readerExample = new BufferedReader(new FileReader(EXAMPLE2));
        Task1.convertArguments(IN2, OUT2);

        StringBuilder inputText = new StringBuilder();
        StringBuilder outputText = new StringBuilder();
        fillBuilder(inputText, readerExample);
        fillBuilder(outputText, readerOut);


        assertEquals(inputText.toString(), outputText.toString());
    }

    private static void fillBuilder(StringBuilder builder, BufferedReader reader) {
        if (builder != null && reader != null)
            try {
                while (reader.ready()) {
                    builder.append(reader.readLine()).append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
