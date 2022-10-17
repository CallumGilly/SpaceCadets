package Challenge1;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;

class WebService {
  public static void main(String[] args) throws IOException {
    //This allows for input using arguments or readline, felt like learning how that worked ¯\_(ツ)_/¯.
    String emailID;
    if (args.length == 0) {
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      System.out.println("Enter the email ID:");
      emailID = reader.readLine();
    } else {
      emailID = args[0];
    }

    //Make a URL object using the users input.
    URL webPage = new URL("https://www.ecs.soton.ac.uk/people/" + emailID);

    //Create a reader object
    BufferedReader webReader = new BufferedReader(new InputStreamReader(webPage.openStream()));
    boolean flag = true;
    //Loop through each line of the site until the name has been found or we reach the end of the page.
    while (flag) {
      String currentString = webReader.readLine();
      //Check to see if we are at the end of the page.
      if (currentString == null) {
        flag = false;
      } else {
        //Check if the current line contains "name": and does not contain Southampton (some name property's are that of the University).
        if (currentString.toLowerCase().indexOf("\"name\": \"") != -1 & currentString.indexOf("Southampton") == -1) {
          //Print out the name and mark the name as found.
          System.out.println(currentString.substring(21, currentString.lastIndexOf("\"")));
          flag = true;
        }
      }
    }
    //Close the web reader object.
    webReader.close();
  }
}