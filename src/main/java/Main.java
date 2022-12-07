import task2.Task2;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws JAXBException, IOException {
        Task2.convertFormat(new File("src/main/resources/xml_fines"), new File("src/main/resources/result.json"));
    }
}
