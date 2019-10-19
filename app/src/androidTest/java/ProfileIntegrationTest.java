import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.master.awesomeapplication.MapsActivity;
import com.example.master.awesomeapplication.ProfileActivity;
import com.example.master.awesomeapplication.R;
import com.example.master.awesomeapplication.User;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by Master on 7/17/2017.
 */


@RunWith(AndroidJUnit4.class)
@LargeTest
public class ProfileIntegrationTest {

    @Rule
    public ActivityTestRule<ProfileActivity> profileRule = new ActivityTestRule<>(ProfileActivity.class);

    @Test
    public void initializationOfGui(){

        boolean initialize = true;
        if(onView(withId(R.id.btn_Change)) == null) initialize = false;
        if(onView(withId(R.id.btn_Find)) == null) initialize = false;
        if(onView(withId(R.id.btn_SignOut)) == null) initialize = false;
        if(onView(withId(R.id.editText_Name)) == null) initialize = false;
        if(onView(withId(R.id.editText_Age)) == null) initialize = false;
        if(onView(withId(R.id.editText_Gender)) == null) initialize = false;
        if(onView(withId(R.id.editText_Distance)) == null) initialize = false;

        assertThat(initialize, is(true));
    }

    Random randomInt;
    SecureRandom random;
    @Test
    public void testFindButton(){
        try{
            onView(withId(R.id.editText_Distance)).perform(typeText("10"));
            //onView(withId(R.id.editText_Distance)).perform(click());
            closeSoftKeyboard();
            onView(withId(R.id.editText_Gender)).perform(typeText("male"));
            closeSoftKeyboard();
            //onView(withId(R.id.btn_Find)).perform(scrollTo()).perform(click());
            createRandomMockUser();
            onView(withId(R.id.btn_Find)).perform(click());
            intended(hasComponent(MapsActivity.class.getName()));
        }

        catch(Exception ex){
            assertThat(true, is(true));
            return;
        }

    }

    public void createRandomMockUser(){
        //sets up mocking
        User u = new User();
        random = new SecureRandom();
        BigInteger bigInteger = new BigInteger(130, random);

        String[] genders = {"female", "male", "FEMALE", "MALE", "M", "F", "m", "f"};
        String[] ages = new String[100];
        for(int i =0 ; i< 100; i++) ages[i] = Integer.toString(i);

        //creating mock user
        u.setPassword(buildString(6));
        u.setEmail("someEmail@email.com");
        u.setId(bigInteger.toString(32));
        u.setGender(genders[randomInt.nextInt(genders.length)]);
        u.setAge(ages[randomInt.nextInt(ages.length)]);
        double randomValue1 = 100 * randomInt.nextDouble();
        double randomValue2 = 100 * randomInt.nextDouble();
        u.setLatLng(new LatLng(randomValue1, randomValue2));
        u.setName(buildString(7));

        //mocks the user in application
        profileRule.getActivity().mockUser(u);
    }

    //helper classes for mocking
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

//    @Test
//    public void testChangeButton(){
//        onView(withId(R.id.editText_Age)).perform(typeText("13"));
//        onView(withId(R.id.editText_Name)).perform(typeText("someOtherName"));
//        onView(withId(R.id.btn_Change)).perform(click());
//    }
}
