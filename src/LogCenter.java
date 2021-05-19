import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class LogCenter {

    //Class Variables
    static String savedirectory = "";
    static String username;
    static String pw;
    static String ip = "localhost";
    static String port;
    static String contin = "y";
    static int log;
    static String caseid;
    static String identifier;
    static String version;
    static String uri;

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter Username");
        username = sc.nextLine();
        System.out.println("Enter Password");
        pw = sc.nextLine();
        System.out.println("Enter IP Address (initial value 'localhost')");
        ip = sc.nextLine();
        if(ip.isEmpty()){
            ip = "localhost";
        }
        System.out.println("Enter Port (initial value '8080')");
        port = sc.nextLine();
        if(port.isEmpty()){
            port = "8080";
        }

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
            System.exit(-1);
        }

        //Set save directory
        System.out.println("Where do you want to save your files? (initially C:)");
        savedirectory = sc.nextLine();
        if(savedirectory.isEmpty()){
            savedirectory = "C:\\";
        }

        while(contin.equals("y")){
            System.out.println("Which Log Type do you want to extract?");
            System.out.println("1: All Specifications      2: All Resource Events");
            System.out.println("3: Case Events Engine      4: Case_Events_ResourceService     5: Complete_Case_Log                        6: Merged_XES_Log");
            System.out.println("7: Specification_Events    8: Specification_XES_Log_Engine    9: Specification_XES_Log_ResourceService    10 : Complete_Case_Log_For_Specification");
            log = sc.nextInt();

            switch(log){
                //Logs without input
                case 1:
                    fileWriter(getPrettyString(engClient.allSpecification()), "All_Specifications.xml");
                    System.out.println("All_Specifications.xml printed");
                    break;
                case 2:
                    fileWriter(replaceTimestamp(resClient.getAllResourceEvents()), "All_Resource_Events.xml");
                    System.out.println("All_Resource_Events.xml printed");
                    break;

                //Logs with CaseID input
                case 3:
                    System.out.println("Input Case ID");
                    caseid = sc.next();
                    fileWriter(replaceTimestamp(engClient.getCaseEvents(caseid)), "Case_Events_Engine_" + caseid + ".xml");
                    System.out.println("Case_Events_Engine.xml printed");
                    break;
                case 4:
                    System.out.println("Input Case ID");
                    caseid = sc.next();
                    fileWriter(replaceTimestamp(resClient.getCaseEvents(caseid)), "Case_Events_ResourceService_" + caseid + ".xml");
                    System.out.println("Case_Events_ResourceService.xml printed");
                    break;
                case 5:
                    System.out.println("Input Case ID");
                    caseid = sc.next();
                    fileWriter(replaceTimestamp(engClient.getCompleteCaseLog(caseid)), "Complete_Case_Log_" + caseid + ".xml");
                    System.out.println("Complete_Case_Log.xml printed");
                    break;

                //Logs with specification input
                case 6:
                    System.out.println("Input Identifier");
                    identifier = sc.next();
                    System.out.println("Input Version");
                    version = sc.next();
                    System.out.println("Input URI");
                    uri = sc.next();
                    fileWriter(resClient.getMergedXESLog(identifier, version, uri), "Merged_XES_Log_" + uri + ".xes");
                    System.out.println("Merged_XES_Log.xes printed");
                    break;
                case 7:
                    System.out.println("Input Identifier");
                    identifier = sc.next();
                    System.out.println("Input Version");
                    version = sc.next();
                    System.out.println("Input URI");
                    uri = sc.next();
                    fileWriter(replaceTimestamp(resClient.getSpecificationEvents(identifier, version, uri)), "Specification_Events_" + uri + ".xml");
                    System.out.println("Specification_Events.xml printed");
                    break;
                case 8:
                    System.out.println("Input Identifier");
                    identifier = sc.next();
                    System.out.println("Input Version");
                    version = sc.next();
                    System.out.println("Input URI");
                    uri = sc.next();
                    fileWriter(engClient.getSpecificationXESLog(identifier, version, uri), "Specification_XES_Log_Engine_" + uri + ".xes");
                    System.out.println("Specification_XES_Log_Engine.xes printed");
                    break;
                case 9:
                    System.out.println("Input Identifier");
                    identifier = sc.next();
                    System.out.println("Input Version");
                    version = sc.next();
                    System.out.println("Input URI");
                    uri = sc.next();
                    fileWriter(resClient.getSpecificationXESLog(identifier, version, uri), "Specification_XES_Log_ResourceService_" + uri + ".xes");
                    System.out.println("Specification_XES_Log_ResourceService.xes printed");
                    break;
                case 10:
                    System.out.println("Input Identifier");
                    identifier = sc.next();
                    System.out.println("Input Version");
                    version = sc.next();
                    System.out.println("Input URI");
                    uri = sc.next();
                    fileWriter(replaceTimestamp(engClient.getCompleteCaseLogsForSpecification(identifier, version, uri)), "Complete_Case_Log_For_Specification_" + uri + ".xml");
                    System.out.println("Complete_Case_Log_For_Specification.xml printed");
                    break;
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
    private static void fileWriter(String input, String filename) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(savedirectory + filename + "", "UTF-8");
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


    public static String replaceTimestamp(String input) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

        try {
            PrintWriter writer = new PrintWriter("tmp.xml", "UTF-8");
            writer.print(input);
            writer.close();
            File fXmlFile = new File("tmp.xml");
            DocumentBuilderFactory dbFactory1 = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder1 = dbFactory1.newDocumentBuilder();
            if (!fXmlFile.exists()) {
                System.out.println("Error: File not found!");
            }

            Document doc = dBuilder1.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList nodelist = doc.getElementsByTagName("timestamp");

            for(int i = 0; i < nodelist.getLength(); ++i) {
                doc.getElementsByTagName("timestamp").item(i).setTextContent(dateFormat.format(Long.parseLong(nodelist.item(i).getTextContent())));
            }

            try {
                StringWriter sw = new StringWriter();
                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer transformer = tf.newTransformer();
                transformer.setOutputProperty("omit-xml-declaration", "no");
                transformer.setOutputProperty("method", "xml");
                transformer.setOutputProperty("indent", "yes");
                transformer.setOutputProperty("encoding", "UTF-8");
                transformer.transform(new DOMSource(doc), new StreamResult(sw));
                if (fXmlFile.exists()) {
                    fXmlFile.delete();
                }

                return sw.toString();
            } catch (Exception e) {
                throw new RuntimeException("Error converting to String", e);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
