package task1;

import java.io.*;
import java.util.LinkedList;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 1. Розробити програму, яка на вхід отримує xml-файл з тегами <person>, в яких є атрибути name і surname.
 * Програма повинна створювати копію цього файлу, в якій значення атрибута surname об'єднане з name.
 * Наприклад name="Тарас" surname="Шевченко" у вхідному файлі повинно бути замінене на name="Тарас Шевченко"
 * (атрибут surname має бути видалений).
 * Вхідний файл може бути великий, тому завантажувати його цілком в оперативну пам'ять буде поганою ідеєю.
 * * Опціонально (на макс. бал): зробити так, щоб форматування вихідного файла повторювало форматування вхідного
 * файлу (мабуть, xml-парсер в такому разі тут не підійде).
 * <p>
 * <p>
 * Приклад вхідного файлу:
 * <persons>
 * <person name="Іван" surname="Котляревський" birthDate="09.09.1769" />
 * <person surname="Шевченко" name="Тарас" birthDate="09.03.1814" />
 * <person
 * birthData="27.08.1856"
 * name = "Іван"
 * surname = "Франко" />
 * <person name="Леся"
 * surname="Українка"
 * birthData="13.02.1871" />
 * </persons>
 * <p>
 * Приклад вихідного файлу:
 * <persons>
 * <person name="Іван Котляревський" birthDate="09.09.1769"  />
 * <person name="Тарас Шевченко" birthDate="09.03.1814" />
 * <person
 * birthData="27.08.1856"
 * name = "Іван Франко"
 * />
 * <person name="Леся Українка"
 * <p>
 * birthData="13.02.1871" />
 * </persons>
 */
public class Task1 {
    private Task1() {
    }

    private static final String GROUP_SURNAME_PATTERN = "(surname( |)=( |)\"[-А-Яа-яЇїІіЄєҐґ']+\"( |))";
    private static final String GROUP_NAME_PATTERN = "( name( |)=( |)\"[-А-Яа-яЇїІіЄєҐґ']+\")";
    private static final String CUT_NAME_PATTERN = "( (name)( |)=( |)\")";
    private static final String CUT_SURNAME_PATTERN = "((surname)( |)=( |)\"( |))";

    public static void convertArguments(File in, File out) {

        try (BufferedReader reader = new BufferedReader(new FileReader(in));
             BufferedWriter writer = new BufferedWriter(new FileWriter(out))) {

            StringBuilder tempStringBuilder = new StringBuilder();
            LinkedList<String> fullNamesList = new LinkedList<>();

            fillBuilder(reader, tempStringBuilder);

            Pattern surnamePattern = Pattern.compile(GROUP_SURNAME_PATTERN);
            Pattern namePattern = Pattern.compile(GROUP_NAME_PATTERN);
            Matcher surnameMatcherForBuilder = surnamePattern.matcher(tempStringBuilder);
            Matcher nameMatcherForBuilder = namePattern.matcher(tempStringBuilder);

            String result = tempStringBuilder.toString();

            namesCollector(nameMatcherForBuilder, surnameMatcherForBuilder, fullNamesList);
            result = result.replaceAll(surnamePattern.pattern(), "");
            Matcher nameMatcherForResultString = namePattern.matcher(result);

            for (String person : result.split("(/>)|(</)")) {
                if (nameMatcherForResultString.find()) {
                    result = result.replace(
                            person, person.replace(
                                    nameMatcherForResultString.group().replaceAll(
                                            CUT_NAME_PATTERN, "").replace(
                                            "\"", "").trim(), fullNamesList.pop()));
                }
            }
//            result = deleteEmptyLines(result);
            writer.write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private static String deleteEmptyLines(String source) throws IOException {
//        BufferedReader readerForEmptyLines = new BufferedReader(new StringReader(source));
//        StringBuilder result = new StringBuilder();
//        String temp;
//        while ((temp = readerForEmptyLines.readLine()) != null) {
//            if (temp.trim().length() > 0)
//                result.append(temp).append("\n");
//        }
//        return result.toString();
//    }

    private static void fillBuilder(BufferedReader reader, StringBuilder container) throws IOException {
        if (reader != null && container != null) {
            while (reader.ready()) {
                container.append(reader.readLine()).append("\n");
            }
        }
    }

    private static void namesCollector(Matcher name, Matcher surname, LinkedList<String> fullNames) {
        while (surname.find() && name.find()) {
            String fullName = name.group().replaceAll(CUT_NAME_PATTERN,
                    "").replace("\"", "").trim() + " " +
                    surname.group().replaceAll(CUT_SURNAME_PATTERN,
                            "").replace("\"", "").trim();
            fullNames.add(fullName);
        }
    }
}
