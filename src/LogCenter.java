import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

public class LogCenter {

    //Class Variables
    static String savedirectory = "C:\\";
    private EngineClient client;

    public static void main(String[] args) throws Exception {

        //creates urls
        String url = "http://localhost:8080/yawl/logGateway";
        String resurl = "http://localhost:8080/resourceService/logGateway";

        //Creates new Clients
        EngineClient engClient = new EngineClient("admin", "YAWL", url);
        engClient.checkConnection();
        ResourceClient resClient = new ResourceClient("admin", "YAWL", resurl);
        resClient.checkConnection();

        //Test run for file writing
        fileWriter(getPrettyString(engClient.allSpecification()),  "allSpecification", "xml");
        fileWriter(getPrettyString(engClient.getCompleteCaseLog("1")),  "getCompleteCaseLog", "xml");
        fileWriter(getPrettyString(engClient.getCompleteCaseLogsForSpecification("1", "0.4", "")),  "getCompleteCaseLogForSpecification", "xml");
        fileWriter(engClient.getSpecificationXESLog("UID_12135543-85dc-4a60-a5b4-b1aec185f609", "0.8", "ApplForLeave"),  "getSpecificationXESLog", "xml");
        fileWriter(getPrettyString(engClient.getCaseEvents("1")),  "getCaseEventsEngine", "xml");
        fileWriter(getPrettyString(resClient.getCaseEvents("1")),  "getCaseEventsrsService", "xml");
        fileWriter(resClient.getMergedXESLog("UID_12135543-85dc-4a60-a5b4-b1aec185f609", "0.8", "ApplForLeave"), "getMergedXESLog", "xml");
        fileWriter(getPrettyString(resClient.getAllResourceEvents()), "getAllResourceEvents", "xml");
        fileWriter(resClient.getMergedXESLog("UID_12135543-85dc-4a60-a5b4-b1aec185f609", "0.8", "ApplForLeave"), "getMergedXESLog", "xml");
        fileWriter(getPrettyString(resClient.getSpecificationEvents("UID_12135543-85dc-4a60-a5b4-b1aec185f609", "0.8", "ApplForLeave")), "getSpecificationEvents", "xml");


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
