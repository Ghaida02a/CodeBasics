import java.util.Arrays;

public class Main{
public static void main(String[] args) {


    String[] names = {"Nasser", "Haitham", "Manal", "Hanin"};

    for(int i = 0; i < names.length; i++){
        String nameToReverse = names[i];
        String reversedName = reverseString(nameToReverse);
        names[i] = reversedName;
        //System.out.println(names.toString());

    }

    System.out.println(Arrays.toString(names));

}

public static String reverseString(String stringToReverse) {

    char[] characterArray = new char[stringToReverse.length()];
    for (int i = 0; i < stringToReverse.length(); i++) {
        characterArray[i] = stringToReverse.charAt(i);
    }


    StringBuilder reversedString = new StringBuilder();

    int lengthOfStringToReverse = stringToReverse.length();
    int lastIndexOfStringToReverse = lengthOfStringToReverse - 1;

    for (int i = lastIndexOfStringToReverse; i > -1; i--) {
        char c = characterArray[i];
        //System.out.println(c);
        reversedString.append(c);

    }
    //System.out.println(reversedString);
    return reversedString.toString();
}}