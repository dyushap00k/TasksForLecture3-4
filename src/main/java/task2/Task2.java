package task2;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * 2. У папці є перелік текстових файлів, кожен із яких є "зліпок" БД порушень правил дорожнього руху протягом певного року.
 * Кожен файл містить список json (або xml - на вибір) об'єктів - порушень приблизно такого виду:
 * {
 * "date_time: "2020-05-05 15:39:03", // час порушеня
 * "first_name": "Ivan",
 * "last_name": "Ivanov"
 * "type": "SPEEDING", // тип порушення
 * "fine_amount": 340.00 // сума штрафу
 * }
 * <p>
 * Прочитати дані з усіх файлів, розрахувати та вивести статистику порушень у файл.
 * В вихідному файлі повинні міститися загальні суми штрафів за кожним типом порушень за всі роки,
 * відсортовані за сумою (спочатку за найбільшою сумою штрафу).
 * Якщо вхідний файл був json, то вихідний повиннен бути xml. Якщо вхідний xml, то вихідний - json.
 * Щоб ви мали досвід роботи з обома форматами.
 * * Опціонально (на макс. бал): зробити так, щоб вхідні файли не завантажувалися цілком в пам'ять.
 */
public class Task2 {
    public static void convertFormat(File in, File out) throws JAXBException, IOException {
        JsonFactory jsonFactory = new JsonFactory();
        FineList fList = new FineList();
        Map<String, BigDecimal> container = new HashMap<>();

        //from json to xml
        if (out.getAbsolutePath().endsWith(".xml")) {
            for (File inFile : Objects.requireNonNull(in.listFiles())
            ) {
                if (inFile.getAbsolutePath().endsWith(".json") && out.getAbsolutePath().endsWith(".xml")) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(inFile));
                         JsonParser jsonParser = jsonFactory.createParser(reader)) {
                        while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                            String field;
                            String name = "";
                            BigDecimal amount = new BigDecimal(0);
                            while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                                if (jsonParser.currentName() != null) {

                                    field = jsonParser.currentName().toUpperCase(Locale.ROOT);
                                    if (field.equals("TYPE"))
                                        name = jsonParser.getValueAsString();
                                    if (field.equals("FINE_AMOUNT"))
                                        amount = BigDecimal.valueOf(jsonParser.getValueAsDouble());
                                }
                            }
                            container.put(name, container.getOrDefault(name, BigDecimal.ZERO).add(amount));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            marshalling(container, fList, out);
        }
        //from xml to json
        else if (out.getAbsolutePath().endsWith(".json")) {
            for (File file : Objects.requireNonNull(in.listFiles())) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    JAXBContext jaxbContext = JAXBContext.newInstance(FineList.class);
                    Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                    FineList list = (FineList) unmarshaller.unmarshal(reader);
                    list.getList()
                            .forEach(x -> container.put(x.getType(), container.getOrDefault(x.getType(), BigDecimal.ZERO).add(x.getAmount())));
                }
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(out));
            writer.write(mapping(container).toString());
            writer.flush();
            writer.close();
        }
    }

    private static ArrayNode mapping(Map<String, BigDecimal> container) {
        ObjectMapper oMapper = new ObjectMapper();
        ArrayNode arrayNode = new ArrayNode(JsonNodeFactory.instance);
        for (String str : container.keySet()) {
            ObjectNode oNode = oMapper.createObjectNode();
            oNode.put(str.toLowerCase(Locale.ROOT), container.get(str).doubleValue());
            arrayNode.add(oNode);
        }
        return arrayNode;
    }

    private static void marshalling(Map<String, BigDecimal> finesMap, FineList finesList, File out) throws JAXBException {
        List<Fine> list = new ArrayList<>();
        finesMap.entrySet().stream()
                .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                .forEach(x -> list.add(new Fine(x.getKey(), x.getValue())));
        finesList.getList().addAll(list);
        JAXBContext jaxbContext = JAXBContext.newInstance(FineList.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(finesList, out);
    }

}
