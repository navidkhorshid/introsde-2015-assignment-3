package ehealth.model.bl;

import ehealth.model.da.HealthMeasureHistoryDA;
import ehealth.model.da.MeasureDefinitionDA;
import ehealth.model.da.PersonDA;
import ehealth.model.to.HealthMeasureHistory;
import ehealth.model.to.HealthProfile;
import ehealth.model.to.MeasureDefinition;
import ehealth.model.to.Person;

import javax.jws.WebService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebService(endpointInterface = "ehealth.model.bl.PeopleBL", serviceName="PeopleService")
public class PeopleImpl implements PeopleBL
{
    //PeopleImpl part

    //Method #1
    @Override
    public List<Person> readPersonList()
    {
        List<Person> persons = new ArrayList<Person>();
        try
        {
            persons = new PersonDA().selectAll();
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return persons;
    }

    //Method #2
    //get details of a Person
    @Override
    public Person readPerson(Long person_id)
    {
        Person person = new Person();
        try
        {
            person = new PersonDA().selectById(person_id);
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return person;
    }

    //Method #3
    //Edit Person
    @Override
    public void updatePerson(Person person)
    {
        try
        {
            new PersonDA().updateById(person);
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    //Method #4
    //UserRegistration
    @Override
    public Person createPerson(Person person)
    {
        try {
            person = new PersonDA().insert(person);

            //If it has healthProfile then also try to insert their values
            if(person.getMeasure().size()!=0)
                person.setMeasure(setMeasureHistory(person)); //this needs to be updated to get MIDs from database
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return person;
    }

    //Method #5
    @Override
    public void deletePerson(Person person)
    {
        try
        {
            new PersonDA().deleteById(new Person(person));
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    //get count of Person
    public long getPersonCount() throws Exception
    {
        PersonDA personDA = new PersonDA();
        long count = 0;
        count = personDA.selectCount();
        return count;
    }

    public String getFirstId() throws Exception
    {
        PersonDA personDA = new PersonDA();
        return String.valueOf(personDA.selectFirstPerson());
    }

    public String getLastId() throws Exception
    {
        PersonDA personDA = new PersonDA();
        return String.valueOf(personDA.selectLastPerson());
    }

    //MeasureDefinitionBL part

    //Method #7
    @Override
    public List<String> readMeasureTypes()
    {
        List<String> measureTypes = new ArrayList<String>();
        try
        {
            measureTypes = new MeasureDefinitionDA().selectAll();
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return measureTypes;
    }

    public MeasureDefinition getMeasureDefinition(String measureType)
    {
        MeasureDefinition measureDefinition = new MeasureDefinition();
        try
        {
            measureDefinition = new MeasureDefinitionDA().selectByMeasure(measureType);
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return measureDefinition;
    }

    //HealthMeasureHistoryBL part

    //Method #6
    @Override
    public List<HealthMeasureHistory> readPersonHistory(Long idPerson, String measureType)
    {
        List<HealthMeasureHistory> healthMeasureHistories = new ArrayList<HealthMeasureHistory>();
        try
        {
            int idMeasureDef = getMeasureDefinition(measureType).getIdMeasureDef();
            healthMeasureHistories = new HealthMeasureHistoryDA().selectByPerson_MeasureDef(idPerson, idMeasureDef);
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return healthMeasureHistories;
    }

    //Method #8
    @Override
    public HealthMeasureHistory readPersonMeasure(Long idPerson, String measureType, Long mid)
    {
        HealthMeasureHistory healthMeasureHistory = new HealthMeasureHistory();
        try
        {
            int idMeasureDef = getMeasureDefinition(measureType).getIdMeasureDef();
            healthMeasureHistory = new HealthMeasureHistoryDA().selectByPerson_MeasureDef_mId(idPerson, idMeasureDef, mid);
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return healthMeasureHistory;
    }

    //Method #9
    @Override
    public void savePersonMeasure(Long id, HealthMeasureHistory healthMeasureHistory)
    {
        try
        {
            //a new person with id
            Person person = new Person();
            person.setIdPerson(id);
            //a new MeasureDefinition
            MeasureDefinition measureDefinition = getMeasureDefinition(healthMeasureHistory.getMeasureType());

            healthMeasureHistory.setPerson(person);
            healthMeasureHistory.setMeasureDefinition(measureDefinition);
            new HealthMeasureHistoryDA().insert(healthMeasureHistory, false);
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    //Method #10
    @Override
    public HealthMeasureHistory updatePersonMeasure(Long id, HealthMeasureHistory healthMeasureHistory)
    {
        HealthMeasureHistory tmpHealthMeasureHistory = new HealthMeasureHistory();
        try
        {
            //a new person with id
            Person person = new Person();
            person.setIdPerson(id);
            //a new MeasureDefinition
            MeasureDefinition measureDefinition = getMeasureDefinition(healthMeasureHistory.getMeasureType());
            //In case the user forgot the MeasureValueType
            healthMeasureHistory.setMeasureValueType(measureDefinition.getMeasureType_Type());

            healthMeasureHistory.setPerson(person);
            healthMeasureHistory.setMeasureDefinition(measureDefinition);

            tmpHealthMeasureHistory = new HealthMeasureHistory(new HealthMeasureHistoryDA().update(healthMeasureHistory)) ;
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return tmpHealthMeasureHistory;

    }


    public ArrayList<HealthMeasureHistory> getMeasureHistories(Long idPerson, int idMeasureDef, Date before, Date after) throws Exception
    {
        HealthMeasureHistoryDA healthMeasureHistoryDA = new HealthMeasureHistoryDA();
        ArrayList<HealthMeasureHistory> healthMeasureHistories =
                healthMeasureHistoryDA.selectByPerson_MeasureDef_Date(idPerson, idMeasureDef, before, after);
        return healthMeasureHistories;
    }


    public List<HealthProfile> setMeasureHistory(Person person) throws Exception
    {
        List<HealthProfile> healthProfileList = new ArrayList<HealthProfile>();
        for(HealthProfile hp: person.getMeasure())
        {
            HealthMeasureHistoryDA healthMeasureHistoryDA = new HealthMeasureHistoryDA();
            HealthMeasureHistory healthMeasureHistory = new HealthMeasureHistory();
            HealthProfile healthProfile = new HealthProfile();
            //from Healthprofile to Healthmeasurehistory
            healthMeasureHistory.setPerson(new Person(person));
            healthMeasureHistory.setMeasureDefinition(getMeasureDefinition(hp.getMeasure()));
            healthMeasureHistory.setValue(hp.getValue());
            healthMeasureHistory.setCreated(hp.getCreated());
            //insert to healthMeasureHistory table
            healthMeasureHistory = healthMeasureHistoryDA.insert(healthMeasureHistory, false);
            //from healthHistory to Healthprofile
            healthProfile.setMid(healthMeasureHistory.getMid());
            healthProfile.setCreated(healthMeasureHistory.getCreated());
            healthProfile.setMeasure(healthMeasureHistory.getMeasureDefinition().getMeasureType());
            healthProfile.setMeasureValueType(healthMeasureHistory.getMeasureDefinition().getMeasureType_Type());
            healthProfile.setValue(healthMeasureHistory.getValue());
            //add to list
            healthProfileList.add(healthProfile);
        }
        return healthProfileList;
    }

    //IT IS IMPLEMENTED IN DATABASE AS A TRIGGER, IT WASN'T USED IN THIS PROJECT
    //public void deleteMeasureHistory(Person person) throws Exception
    //{
    //    HealthMeasureHistoryDA healthMeasureHistoryDA = new HealthMeasureHistoryDA();
    //    healthMeasureHistoryDA.deleteByPerson(person);
    //}

}
