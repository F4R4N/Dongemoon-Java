public class Utils {
    public static boolean isStringEmptyOrNull(String string){
        return string==null || string.isEmpty();
    }

    public static int minOfTwoIntegers(int firstInteger, int secondInteger){
        if (firstInteger<secondInteger) {
            return firstInteger;
        }
        return secondInteger;
    }
}
