package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class WaesProperties {

    Properties waesProp = new Properties();

    public String baseUrl;
    public String getUsersUsername;
    public String getUsersPassword;
    public String loginUsername;
    public String loginPassword;
    public String updateUserUsername;
    public String updateUserPassword;
    public String deleteUserUsername;
    public String deleteUserPassword;


    public WaesProperties() throws IOException {
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "/src/main/java/data/env.properties");
        waesProp.load(fis);

        baseUrl = waesProp.getProperty("baseUrl");
        getUsersUsername = waesProp.getProperty("getUsersUsername");
        getUsersPassword = waesProp.getProperty("getUsersPassword");
        loginUsername = waesProp.getProperty("loginUsername");
        loginPassword = waesProp.getProperty("loginPassword");
        updateUserUsername = waesProp.getProperty("updateUserUsername");
        updateUserPassword = waesProp.getProperty("updateUserPassword");
        deleteUserUsername = waesProp.getProperty("deleteUserUsername");
        deleteUserPassword = waesProp.getProperty("deleteUserPassword");
    }

}
