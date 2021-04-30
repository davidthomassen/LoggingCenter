import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.Scanner;

public class LogCenter {

    //Class Variables
    static String savedirectory = "C:\\";
    static String username;
    static String pw;
    static String ip = "localhost";
    static String port = "8080";
    static String contin = "y";
    static int log;
    static int caseid;

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter Username");
        username = sc.next();
        System.out.println("Enter Password");
        pw = sc.next();
        System.out.println("Enter IP Adress (intial value 'localhost')");
        ip = sc.next();
        System.out.println("Enter Port (initial value '8080')");
        port = sc.next();

        //creates urls
        String url = "http://" + ip + ":" + port + "/yawl/logGateway";
        String resurl = "http://" + ip + ":" + port + "/resourceService/logGateway";

        //Creates new Clients
        EngineClient engClient = new EngineClient(username, pw, url);
        ResourceClient resClient = new ResourceClient(username, pw, resurl);

        if(engClient.checkConnection() && resClient.checkConnection()){
            System.out.println("Connection successful!");
        }else{
            System.out.println("Connection failed!");
        }

        while(contin.equals("y")){
            System.out.println("Which Log Type do you want to extract?");
            log = sc.nextInt();

            switch(log){
                //Logs without input
                case 1:
                    fileWriter(getPrettyString(engClient.allSpecification()), "All_Specifications", "xml");
                    break;
                case 2:
                    fileWriter(getPrettyString(resClient.getAllResourceEvents()), "All_Ressource_Events", "xml");
                    break;
                //Logs with CaseID input
                case 3:
                case 4:
                case 5:

                //Logs with specification input
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
            }


            System.out.println("Continue? y/n");
            contin = sc.next();
        }




/*
        //Test run for file writing
        username = "admin";
        pw = "YAWL";
        String url = "http://localhost:8080/yawl/logGateway";
        String resurl = "http://localhost:8080/resourceService/logGateway";
        EngineClient engClient = new EngineClient(username, pw, url);
        engClient.checkConnection();
        ResourceClient resClient = new ResourceClient(username, pw, resurl);
        resClient.checkConnection();

        //fileWriter(getPrettyString(engClient.allSpecification()),  "allSpecification", "xml");
        //fileWriter(getPrettyString(engClient.getCompleteCaseLog("1")),  "getCompleteCaseLog", "xml");
        //fileWriter(getPrettyString(engClient.getCompleteCaseLogsForSpecification("1", "0.4", "")),  "getCompleteCaseLogForSpecification", "xml");
        //fileWriter(engClient.getSpecificationXESLog("UID_12135543-85dc-4a60-a5b4-b1aec185f609", "0.8", "ApplForLeave"),  "getSpecificationXESLog", "xml");
        //fileWriter(resClient.getSpecificationXESLog("UID_12135543-85dc-4a60-a5b4-b1aec185f609", "0.8", "ApplForLeave"),  "ResGetSpecificationXESLog", "xml");
        //fileWriter(getPrettyString(engClient.getCaseEvents("1")),  "getCaseEventsEngine", "xml");
        //fileWriter(getPrettyString(resClient.getCaseEvents("1")),  "getCaseEventsrsService", "xml");
        //fileWriter(resClient.getMergedXESLog("UID_12135543-85dc-4a60-a5b4-b1aec185f609", "0.8", "ApplForLeave"), "getMergedXESLog", "xml");
        //fileWriter(getPrettyString(resClient.getAllResourceEvents()), "getAllResourceEvents", "xml");
        //fileWriter(resClient.getMergedXESLog("UID_12135543-85dc-4a60-a5b4-b1aec185f609", "0.8", "ApplForLeave"), "getMergedXESLog", "xml");
        //fileWriter(getPrettyString(resClient.getSpecificationEvents("UID_12135543-85dc-4a60-a5b4-b1aec185f609", "0.8", "ApplForLeave")), "getSpecificationEvents", "xml");
*/


    }


    //Creates a Document in set Directory
    private static void fileWriter(String input, String filename, String extention) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(savedirectory + filename + "." + extention, "UTF-8");
        writer.print(input);
        writer.close();
    }


    //Converts input xml String to a readable Format
    public static String getPrettyString(String xmlData) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setAttribute("indent-number", 2);

        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        StringWriter stringWriter = new StringWriter();
        StreamResult xmlOutput = new StreamResult(stringWriter);

        Source xmlInput = new StreamSource(new StringReader(xmlData));
        transformer.transform(xmlInput, xmlOutput);

        return xmlOutput.getWriter().toString();
    }

}
