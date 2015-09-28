package hackny.clarifyproject;

import java.util.ArrayList;

/**
 * Created by Tony on 9/27/2015.
 */
public class DataManager {
    private ArrayList<Extras> extrasList;

    public DataManager(){
        extrasList = new ArrayList<Extras>();
        populateExtrasArray();
    }

    public ArrayList<Extras> getExtrasList(){
        return extrasList;
    }

    private void populateExtrasArray(){
        extrasList.add(new Extras("2015",false));
        extrasList.add(new Extras("Pride",false));
        extrasList.add(new Extras("so",true));
        extrasList.add(new Extras("AndChill",false));
        extrasList.add(new Extras("YouDaRealMVP",false));
        extrasList.add(new Extras("Flow",false));
        extrasList.add(new Extras("trendy",true));
        extrasList.add(new Extras("Nation",false));
        extrasList.add(new Extras("team",true));
        //these are to give some tags no extras
        extrasList.add(new Extras("",false));
        extrasList.add(new Extras("",false));
        extrasList.add(new Extras("",false));
        extrasList.add(new Extras("",false));
    }
}
