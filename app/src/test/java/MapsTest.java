import com.example.master.awesomeapplication.MapsActivity;
import com.example.master.awesomeapplication.MapsLogic;
import com.example.master.awesomeapplication.User;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Master on 7/17/2017.
 */

public class MapsTest {

    ArrayList<User> users;
    MapsActivity mapsActivity;
    String gender;
    LatLng latLng1, latLng2;
    MapsLogic logic;

    @Before
    public void initializeVariables(){
        //mapsActivity = new MapsActivity();
        logic = new MapsLogic();
        users = new ArrayList<>();
        gender = "randomGender";

    }

    @Test
    public void testToPassGenderFilterUser(){
        String[] genderInputs = {"female", "male", "FEMALE", "MALE", "M", "F", "m", "f"};
        boolean itWorks = true;
        for(String gen : genderInputs){
            if(!testGenderFilterWithDifferentInputs(gen)) itWorks = false;
        }

        assertThat(itWorks, is(true));
    }

    public boolean testGenderFilterWithDifferentInputs(String input){
        return logic.checkGenderFilter(input);
    }

    @Test
    public void testToFailGenderFilterUser(){
        boolean fails = logic.checkGenderFilter(gender);
        assertThat(fails, is(false));
    }

    @Test
    public void testTryParseIntFail(){
        boolean trueOrFalse = !logic.tryParseIntFail("100");
        assertThat(trueOrFalse, is(true));
    }


}
