import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidator {
    static public boolean validateMovieName(String name){
        // this regex make sure that the movie name starts and ends with a latter or a number
        // can't start or end with an empty space, even tho we already used .trim()... the more the merrier
        // example of start with number: 12 years a slave
        // this regex also only work for English movie names
        // maximum number of char is 32 and minimum is 2 (first and last)
        String regex = "^[A-Za-z0-9][A-Za-z0-9\\s:,'\\-!?.()]{0,30}[A-Za-z0-9)]$";
        // Compile the regex into a pattern
        Pattern pattern = Pattern.compile(regex);
        // Match against the pattern
        Matcher matcher = pattern.matcher(name);
        // validation, return the match result
        return matcher.matches();
    }

    static public boolean validateDirectorName(String name){
        // this regex make sure that the Director name starts and ends with a latter
        // example of a name with ' is my boy Conan O'Brien
        // this regex also only work for English names
        // maximum number of char is 32 and minimum is 2 (first and last)
        String regex = "^[A-Za-z][A-Za-z '-]{0,30}[A-Za-z]$";
        // Compile the regex into a pattern
        Pattern pattern = Pattern.compile(regex);
        // Match against the pattern
        Matcher matcher = pattern.matcher(name);
        // validation, return the match result
        return matcher.matches();
    }

    static public boolean validateMovieDuration(String name){
        // this regex make sure that the duration is either 1 or 2
        String regex = "^[12]$";
        // Compile the regex into a pattern
        Pattern pattern = Pattern.compile(regex);
        // Match against the pattern
        Matcher matcher = pattern.matcher(name);
        // validation, return the match result
        return matcher.matches();
    }

    static public boolean validateMovieDescription(String name){
        // this regex starts with a latter and ends with a latter,number or ".".
        // this regex also only work for English names
        // maximum number of char is 256 and minimum is 2 (first and last)
        String regex = "^[A-Za-z][^\\n]{1,254}[A-Za-z0-9.]$";
        // Compile the regex into a pattern
        Pattern pattern = Pattern.compile(regex);
        // Match against the pattern
        Matcher matcher = pattern.matcher(name);
        // validation, return the match result
        return matcher.matches();
    }

    static public boolean validateMovieGenre(String name){
        // this regex for movie genre and only allow one word
        // it also allow - for special genres like Sci-Fi
        String regex = "^[A-Za-z\\-]{1,32}$";
        // Compile the regex into a pattern
        Pattern pattern = Pattern.compile(regex);
        // Match against the pattern
        Matcher matcher = pattern.matcher(name);
        // validation, return the match result
        return matcher.matches();
    }

    static public boolean validateRoomNumber(String name){
        // this regex for room number
        String regex = "^\\d{1}$|^\\d{2}$";
        // Compile the regex into a pattern
        Pattern pattern = Pattern.compile(regex);
        // Match against the pattern
        Matcher matcher = pattern.matcher(name);
        // validation, return the match result
        return matcher.matches();
    }

    static public boolean validateShowtimeDate(String name){
        // this regex for date
        // it makes sure that the input is the same format as sql DATE format YYYY-MM-DD
        // also made sure that the months are 1-12 and day are 1-31
        // didn't make an exception for February because it will take alot of time.. sorry
        String regex = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$";
        // Compile the regex into a pattern
        Pattern pattern = Pattern.compile(regex);
        // Match against the pattern
        Matcher matcher = pattern.matcher(name);
        // validation, return the match result
        return matcher.matches();
    }

    static public boolean validateTimeSlot(String name){
        // this regex for time slot
        // there are only 7 time slots
        String regex = "^[1-7]$";
        // Compile the regex into a pattern
        Pattern pattern = Pattern.compile(regex);
        // Match against the pattern
        Matcher matcher = pattern.matcher(name);
        // validation, return the match result
        return matcher.matches();
    }

    static public boolean validateMaintenanceReport(String name){
        // this regex starts with a latter and ends with a latter,number or ".".
        // maximum number of char is 256 and minimum is 2 (first and last)
        String regex = "^[A-Za-z][^\\n]{1,254}[A-Za-z0-9.]$";
        // Compile the regex into a pattern
        Pattern pattern = Pattern.compile(regex);
        // Match against the pattern
        Matcher matcher = pattern.matcher(name);
        // validation, return the match result
        return matcher.matches();
    }

}
