package ehealth;

import ehealth.model.bl.PeopleImpl;

import javax.xml.ws.Endpoint;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;

public class App
{
    public static void main(String[] args) throws IllegalArgumentException, IOException, URISyntaxException
    {
        String PROTOCOL = "http://";
        String HOSTNAME = InetAddress.getLocalHost().getHostAddress();
        if (HOSTNAME.equals("127.0.0.1"))
        {
            HOSTNAME = "localhost";
        }
        String PORT = "6901";
        String BASE_URL = "/ws/people";

        if (String.valueOf(System.getenv("PORT")) != "null"){
            PORT=String.valueOf(System.getenv("PORT"));
        }

        String endpointUrl = PROTOCOL+HOSTNAME+":"+PORT+BASE_URL;
        System.out.println("Starting People Service...");
        System.out.println("--> Published. Check out "+endpointUrl+"?wsdl");
        Endpoint.publish(endpointUrl, new PeopleImpl());    }

}
