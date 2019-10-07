package utils;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

public class WaesUtils {

    /**
     * Encoded a given string key in based64 value.
     *
     * @param key Input string to be encoded.
     * @return Encoded string.
     *
     * @author Matías Cárdenas
     */
    public static String base64Encode(String key) {
        byte[] bytesEncoded = Base64.encodeBase64(key.getBytes());
        return new String(bytesEncoded);
    }

    /**
     * Returns time stamp in a String
     *
     * @author Matías Cárdenas
     */
    public static String getTimestamp() {
        return String.valueOf(System.currentTimeMillis()) + RandomStringUtils.random(4, true, false).toLowerCase();
    }

    /**
     * Returns key for specific index number
     *
     * @param index Index for associated key
     *
     * @author Matías Cárdenas
     */
    public static String getKeyForIndex(int index){
        switch(index){
            case 1: return "id";
            case 2: return "name";
            case 3: return "username";
            case 4: return "email";
            case 5: return "superpower";
            case 6: return "dateOfBirth";
            case 7: return "isAdmin";
            case 8: return "password";
            default: return "";
        }
    }


    /**
     * Returns default generated values for user keys.
     *
     * @param key Specific user key
     *
     * @author Matías Cárdenas
     */
    public static Object getDefaultKeyValue(String key){
        int userNumber = new Random().nextInt(1000);

        if(key == "id")
            return userNumber;
        if(key == "name")
            return "test user" + userNumber;
        if(key == "username")
            return "test-user-" + userNumber;
        if(key == "email")
            return "newtestuser" + userNumber + "@wearewaes.com";
        if(key=="superpower")
            return "Kaoiken " + userNumber;
        if(key=="dateOfBirth")
            return "2018-12-12";
        if(key=="isAdmin")
            return false;
        if(key=="password")
            return "waestestpass";

        return "";
    }
}
