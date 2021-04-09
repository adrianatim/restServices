package ro.ubb.catalog.core.model.validators;

public class ExtraFunctions {

    /**
     * Extra function to verify if a string contains only digits
     *
     * @param string - given string
     * @return true or false
     */
    public static boolean OnlyDigits(String string) {

        int length = string.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isDigit(string.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
