package ehealth.model.bl;

import ehealth.model.to.HealthMeasureHistory;
import ehealth.model.to.Person;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import java.util.List;

/**
 * Created by Navid on 12/3/2015.
 */
@WebService
@SOAPBinding(style = Style.DOCUMENT, use=Use.LITERAL) //optional
public interface PeopleBL {

    @WebMethod(operationName="readPersonList")
    @WebResult(name="people")
    public List<Person> readPersonList();

    @WebMethod(operationName="readPerson")
    @WebResult(name="person")
    public Person readPerson(@WebParam(name="idPerson") Long id);

    @WebMethod(operationName="updatePerson")
    public void updatePerson(@WebParam(name="person") Person person);

    @WebMethod(operationName="createPerson")
    @WebResult
    public Person createPerson(@WebParam(name="person") Person person);

    @WebMethod(operationName="deletePerson")
    public void deletePerson(@WebParam(name="person") Person person);

    @WebMethod(operationName="readPersonHistory")
    @WebResult(name="measure")
    public List<HealthMeasureHistory> readPersonHistory(@WebParam(name="idPerson") Long idPerson,@WebParam(name="measureType") String measureType);

    @WebMethod(operationName="readMeasureTypes")
    @WebResult(name="measureTypes")
    public List<String> readMeasureTypes();

    @WebMethod(operationName="readPersonMeasure")
    @WebResult(name="measure")
    public HealthMeasureHistory readPersonMeasure(@WebParam(name="idPerson") Long idPerson,@WebParam(name="measureType") String measureType,@WebParam(name="mid") Long mid);

    @WebMethod(operationName="savePersonMeasure")
    @WebResult
    public HealthMeasureHistory savePersonMeasure(@WebParam(name="idPerson") Long id,@WebParam(name="measure") HealthMeasureHistory healthMeasureHistory);

    @WebMethod(operationName="updatePersonMeasure")
    @WebResult
    public HealthMeasureHistory updatePersonMeasure(@WebParam(name="idPerson") Long id,@WebParam(name="measure") HealthMeasureHistory healthMeasureHistory);
}
