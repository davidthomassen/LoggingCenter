import org.yawlfoundation.yawl.resourcing.rsInterface.ResourceLogGatewayClient;

import java.io.IOException;

public class ResourceClient {

    //Class variables
    private ResourceLogGatewayClient client;
    private String handle;

    //Constructor; Creates and connects new client to resource service; Creates handle which contains information about successful connection
    public ResourceClient(String userId, String password, String url){
        this.client = new ResourceLogGatewayClient(url);

        try{
            this.handle = client.connect(userId, password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //checks connection
    public boolean checkConnection(){
        try{
            this.client.checkConnection(this.handle);
            System.out.println("Resource Client connection successful");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Gets all events for all cases of the specification passed, from both the resource service and the engine, merged together, optionally including the data value changes from the engine's log
    public String getMergedXESLog(String identifier, String version, String uri){
        try{
            return this.client.getMergedXESLog(identifier, version, uri, true, this.handle);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Gets an summary xml list of all the logged events for a case
    public String getCaseEvents(String caseId){
        try{
            return this.client.getCaseEvents(caseId, this.handle);
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Gets an xml list of all the resource events logged
    public String getAllResourceEvents() {
        try{
            return this.client.getAllResourceEvents(this.handle);
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Gets an xml list of all work item events involving the specified resource (can be a Participant or a NonHumanResource)
    public String getResourceHistory(String id){
        try{
            return this.client.getResourceHistory(id, this.handle);
        } catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }

    //Gets an xml list of all case events for all case instances of a specification
    public String getSpecificationEvents(String identifier, String version, String uri){
        try{
            return this.client.getSpecificationEvents(identifier, version, uri, this.handle);
        } catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }

    //Gets all events for all cases of the specification passed
    public String getSpecificationXESLog(String identifier, String version, String uri){
        try{
            return this.client.getSpecificationXESLog(identifier, version, uri, this.handle);
        } catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }

}
