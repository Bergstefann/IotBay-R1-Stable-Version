package controller;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validator class to validate user input.
 * This class implements Serializable to allow session storage.
 */
public class Validator implements Serializable {

    private String emailPattern = "([a-zA-Z0-9]+)(([._-])([a-zA-Z0-9]+))*(@)([a-z]+)(.)([a-z]{3})((([.])[a-z]{0,2})*)";
    private String namePattern = "([A-Z][a-z]+[\\s])+[A-Z][a-z]*";
    private String passwordPattern = "[a-z0-9]{4,}";

    /**
     * Default constructor
     */
    public Validator() {
    }

    /**
     * Validates input against a pattern
     * 
     * @param pattern The regex pattern to match against
     * @param input The input string to validate
     * @return true if input matches the pattern, false otherwise
     */
    public boolean validate(String pattern, String input) {
        Pattern regEx = Pattern.compile(pattern);
        Matcher match = regEx.matcher(input);

        return match.matches();
    }

    /**
     * Checks if email and password fields are empty
     * 
     * @param email The email input
     * @param password The password input
     * @return true if either field is empty, false otherwise
     */
    public boolean checkEmpty(String email, String password) {
        return email.isEmpty() || password.isEmpty();
    }

    /**
     * Validates email format
     * 
     * @param email The email to validate
     * @return true if email matches the pattern, false otherwise
     */
    public boolean validateEmail(String email) {
        return validate(emailPattern, email);
    }

    /**
     * Validates name format
     * 
     * @param name The name to validate
     * @return true if name matches the pattern, false otherwise
     */
    public boolean validateName(String name) {
        return validate(namePattern, name);
    }

    /**
     * Validates password format
     * 
     * @param password The password to validate
     * @return true if password matches the pattern, false otherwise
     */
    public boolean validatePassword(String password) {
        return validate(passwordPattern, password);
    }
}