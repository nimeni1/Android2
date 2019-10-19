//import android.content.Context;

import com.example.master.awesomeapplication.RegisterLogic;

import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
/**
 * Created by Master on 7/17/2017.
 */

public class RegisterTest {

    public String stringToBeParsed;
    public BigInteger aux;
    public SecureRandom random;
    public boolean check;
    public RegisterLogic logic;

    @Before
    public void initializeString(){
        logic = new RegisterLogic();
        random = new SecureRandom();
        aux = new BigInteger(130, random);
        stringToBeParsed = aux.toString(32);

    }


    @Test
    public void testTryParseIntFail(){
        check = logic.tryParseIntFail(stringToBeParsed);
        assertThat(check, is(true));
    }

    @Test
    public void testNextSessionId(){
        String sessionId = logic.nextSessionId();
        boolean unique = true;
        List<String> generated_ids = new ArrayList<String>();

        //check that the length is the expected one
        assertThat(sessionId.length(), is(26));

        //check for a big number of generated ids that all values are unique
        for(int i = 0; i< Math.pow(10, 6); i++) {
            generated_ids.add(logic.nextSessionId());
        }

        for(String s : generated_ids){
            if(sessionId.equals(s)) {
                unique = false;
                break;
            }
        }

        assertThat(unique, is(true));
    }

}
