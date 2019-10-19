import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.master.awesomeapplication.R;
import com.example.master.awesomeapplication.RegisterActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Created by Master on 7/17/2017.
 */

//
//Using JUnit4 this time
//
//@RunWith(RobolectricTestRunner.class)
@RunWith(AndroidJUnit4.class)
@LargeTest
public class RegisterIntegrationTest {

    String a, e, g, n, p;

    @Rule
    public ActivityTestRule<RegisterActivity> registerRule = new ActivityTestRule<>(RegisterActivity.class);

    @Before
    public void initializeUserDetails(){
        a = "10";
        e = "someEmail@email.com";
        p = "password";
        n = "someName";
        g = "female";
    }

    @Test
    public void initializationOfGui(){
        boolean initialize = true;
        if(onView(withId(R.id.email)) == null) initialize = false;
        if(onView(withId(R.id.password)) == null) initialize = false;
        if(onView(withId(R.id.age)) == null) initialize = false;
        if(onView(withId(R.id.gender)) == null) initialize = false;
        if(onView(withId(R.id.Register)) == null) initialize = false;
        if(onView(withId(R.id.name)) == null) initialize = false;

        assertThat(initialize, is(true));
    }

    @Test
    public void testGetInput(){
        RegisterActivity activity = registerRule.getActivity();
        onView(withId(R.id.password)).perform(typeText("password"));
        closeSoftKeyboard();
        onView(withId(R.id.name)).perform(typeText("name"));
        closeSoftKeyboard();
        onView(withId(R.id.age)).perform(typeText("10"));
        closeSoftKeyboard();
        onView(withId(R.id.gender)).perform(typeText("male"));
        closeSoftKeyboard();
        onView(withId(R.id.email)).perform(typeText("some"));

        assertThat(activity.getInput(), is(true));
    }

    @Test
    //checking if the registration details are repelled when incorrect
    public void testCheckInput(){
        onView(withId(R.id.Register)).perform(click());

        onView(withText(R.string.Toast_name))
                .inRoot(withDecorView(not(is(registerRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));

        onView(withText(R.string.Toast_password))
                .inRoot(withDecorView(not(is(registerRule.getActivity().getWindow().getDecorView()))))
                .check(doesNotExist());

        onView(withText(R.string.Toast_age))
                    .inRoot(withDecorView(not(is(registerRule.getActivity().getWindow().getDecorView()))))
                    .check(doesNotExist());

        onView(withText(R.string.Toast_email))
                    .inRoot(withDecorView(not(is(registerRule.getActivity().getWindow().getDecorView()))))
                    .check(doesNotExist());

        onView(withText(R.string.Toast_gender))
                    .inRoot(withDecorView(not(is(registerRule.getActivity().getWindow().getDecorView()))))
                    .check(doesNotExist());


    }

    @Test
    public void testAddUser(){
        RegisterActivity activity = registerRule.getActivity();
        activity.addUser(a,e,g,n,p);

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
}
