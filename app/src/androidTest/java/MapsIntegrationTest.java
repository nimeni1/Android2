import android.location.Location;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.master.awesomeapplication.MapsActivity;
import com.example.master.awesomeapplication.User;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Master on 7/17/2017.
 */

@RunWith(AndroidJUnit4.class)
@SmallTest
public class MapsIntegrationTest {

    User u;
    BigInteger bigInteger;
    SecureRandom random;
    ArrayList<User> users;
    Random randomInt;
    LatLng latLng1, latLng2;
    MapsActivity activity;

    String[] genders = {"female", "male", "FEMALE", "MALE", "M", "F", "m", "f"};

    @Rule
    public ActivityTestRule<MapsActivity> mapsRule = new ActivityTestRule<MapsActivity>(MapsActivity.class);


    @Before
    public void initializeVariables(){
        latLng1 = new LatLng(44.56, 67.12);
        latLng2 = new LatLng(67.23, 90.41);

        users = new ArrayList<>();

        u = new User();
        random = new SecureRandom();
        bigInteger = new BigInteger(130, random);
        randomInt = new Random();

        //generate random user
        u.setId(bigInteger.toString(32));
        u.setLatLng(new LatLng(34.67, 10.99));
        u.setAge("10");
        u.setName("someName");
        u.setGender("male");
        u.setPassword("password");
        u.setEmail("someEmail@email.com");

        //populate arraylist of 20 random users

        String[] ages = new String[100];
        for(int i =0 ; i< 100; i++) ages[i] = Integer.toString(i);
        //generate latLng
        for(int i=0; i< 20; i++){
            User u = new User();
            u.setPassword("password");
            u.setEmail("someEmail@email.com");
            bigInteger = new BigInteger(130, random);
            u.setId(bigInteger.toString(32));
            u.setGender(genders[randomInt.nextInt(genders.length)]);
            u.setAge(ages[randomInt.nextInt(ages.length)]);
            double randomValue1 = 100 * randomInt.nextDouble();
            double randomValue2 = 100 * randomInt.nextDouble();
            u.setLatLng(new LatLng(randomValue1, randomValue2));
            u.setName(buildString(7));

            users.add(u);
        }
    }

    public String buildString(int length){
        char[] symbols = makeCharactersPool();
        char[] buf = new char[length];
        for (int i = 0; i < buf.length; i++) {
            buf[i] = symbols[random.nextInt(symbols.length)];
        }
        return new String(buf);
    }

    public char[] makeCharactersPool(){
        char[] symbols;
        StringBuilder tmp = new StringBuilder();
        for (char ch = '0'; ch <= '9'; ch++) {
            tmp.append(ch);
        }
        for (char ch = 'a'; ch <= 'z'; ch++) {
            tmp.append(ch);
        }
        symbols = tmp.toString().toCharArray();
        return  symbols;
    }

    @Test
    public void testGetLastKnownLocation(){
        assertThat(mapsRule.getActivity().getLastKnownLocation().getClass().equals(Location.class), is(true));
    }

    boolean displayWorks;
    @Test
    public void testDisplayEachAtATime(){

        activity = mapsRule.getActivity();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                displayWorks = activity.displayEachAtATime(u);
                assertThat(displayWorks, is(true));
            }
        });
        //mapsRule.getActivity().displayEachAtATime(u);
//        UiDevice device = UiDevice.getInstance(getInstrumentation());
//        UiObject marker = device.findObject(new UiSelector().descriptionContains());


    }


    boolean filterWorks;
    @Test
    public void testFilterUsers(){
        activity = mapsRule.getActivity();

        activity.mockFiltering(randomInt.nextInt(1000), genders[randomInt.nextInt(genders.length)]);

        activity.runOnUiThread(new Runnable() {
            public void run() {
                filterWorks = activity.filterUser(users);
                assertThat(filterWorks, is(false));
            }
        });

//        UiDevice device = UiDevice.getInstance(getInstrumentation());
//        UiObject marker = null;
//        boolean foundAtLeastOne = false;
//        int counter =0;
//        do{
//            marker = device.findObject(new UiSelector().descriptionContains("sefsegsrgersrgs"));
//            if(marker != null) foundAtLeastOne = true;
//            counter++;
//        }while(marker == null && counter < users.size());
//
//        assertThat(foundAtLeastOne, is(true));


    }

    @Test
    public void testCalculate(){
        float[] expected = new float[3];
        float actualResult;
        actualResult = mapsRule.getActivity().calculate(latLng1, latLng2);
        Location.distanceBetween(latLng1.latitude, latLng1.longitude, latLng2.latitude, latLng2.longitude, expected);
        assertThat(actualResult, is(expected[0]));
    }

}
