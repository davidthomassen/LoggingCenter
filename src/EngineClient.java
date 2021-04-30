import org.yawlfoundation.yawl.engine.interfce.interfaceE.YLogGatewayClient;

import java.io.IOException;

public class EngineClient {

    //Class Variables
    private YLogGatewayClient client;
    private String handle;

    //Creates and connects new client to engine; Creates handle which contains information about successful connection
    public EngineClient(String userID, String password, String url) {
        this.client = new YLogGatewayClient(url);

        try {
            this.handle = this.client.connect(userID, password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Checks if connection to Engine was successful
    public boolean checkConnection() {
        try {
            this.client.checkConnection(this.handle);
            System.out.println("Engine Client connection successful");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Gets an summary xml list of all the specifications logged
    public String allSpecification() {
        try {
            return this.client.getAllSpecifications(this.handle);
        } catch (IOException e) {
            System.out.println("Error allSpecification");
            e.printStackTrace();
            return null;
        }
    }

    //Gets a comprehensive listing of all of the data logged for the case passed, including all net & task instances, events and data items
    public String getCompleteCaseLog(String caseID) {
        try {
            return this.client.getCompleteCaseLog(caseID, this.handle);
        } catch (IOException e) {
            System.out.println("Error getCompleteCaseLog");
            e.printStackTrace();
            return null;
        }
    }

    //Gets a complete listing of all the cases launched from the specification key passed, including all net & task instances, events and data items
    public String getCompleteCaseLogsForSpecification(String identifier, String version, String uri) {
        try {
            return this.client.getCompleteCaseLogsForSpecification(identifier, version, uri, this.handle);
        } catch (IOException e) {
            System.out.println("getCompleteCaseLogsForSpecification");
            e.printStackTrace();
            return null;
        }
    }

    //Gets a complete listing of all the cases launched from the specification data passed, in OpenXES format
    public String getSpecificationXESLog(String identifier, String version, String uri) {
        try {
            return this.client.getSpecificationXESLog(identifier, version, uri, handle);
        } catch (IOException e) {
            System.out.println("getSpecificationXESLog");
            e.printStackTrace();
            return null;
        }
    }

    //Gets all the case level events for the case id passed
    public String getCaseEvents(String caseId) {
        try {
            return this.client.getCaseEvents(caseId, this.handle);
        } catch (IOException e) {
            System.out.println("getCaseEvents");
            e.printStackTrace();
            return null;
        }
    }


}
