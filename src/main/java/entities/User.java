package entities;

import org.json.JSONObject;

public class User {

    private int id;
    private String userName;
    private boolean isAdmin;
    private String dateOfBirth;
    private String email;
    private String name;
    private String password;
    private String superpower;


    public String getUserName() {
        return userName;
    }

    public User withUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public User withAdmin(boolean admin) {
        isAdmin = admin;
        return this;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public User withDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User withEmail(String email) {
        this.email = email;
        return this;
    }

    public String getName() {
        return name;
    }

    public User withName(String name) {
        this.name = name;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User withPassword(String password) {
        this.password = password;
        return this;
    }

    public String getSuperpower() {
        return superpower;
    }

    public User withSuperpower(String superpower) {
        this.superpower = superpower;
        return this;
    }

    public int getId() {
        return id;
    }

    public User withId(int id) {
        this.id = id;
        return this;
    }

    public JSONObject toJson(){
        JSONObject userJson = new JSONObject();

        userJson.put("id", this.getId());
        userJson.put("username", this.getUserName());
        userJson.put("name", this.getName());
        userJson.put("dateOfBirth", this.getDateOfBirth());
        userJson.put("email", this.getEmail());
        userJson.put("password", this.getPassword());
        userJson.put("superpower", this.getSuperpower());
        userJson.put("isAdmin", this.isAdmin);

        return userJson;
    }
}
