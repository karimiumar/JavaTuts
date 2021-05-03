package com.umar.apps.java8;

import com.umar.apps.java8.optional.IsoFinder;
import com.umar.apps.java8.optional.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class IsoFinderTest {

    private static final List<User> users = new ArrayList<>();

    @BeforeAll
    static void beforeAll() {
        users.add(null);
        users.add(new User("sara", "Sara", null));
        users.add(new User("sara", "Sara", new User.Address("1234", "345566", null)));
        users.add(new User("sara", "Sara", new User.Address("1234", "345566", new User.Address.Country("India", null))));
        users.add(new User("sara", "Sara", new User.Address("1234", "345566", new User.Address.Country(null, null))));
        users.add(new User("sara", "Sara", new User.Address("1234", "345566", new User.Address.Country("India", "IND"))));
    }

    @Test
    void given_listOfUsers_when_size_then_returns_6(){
        assertEquals(6, users.size());
    }

    @Test
    void given_list_OfUsers_when_isoFinderImperativeStyle_called_for_User0_then_throws_Exception() {
        assertThrows(NullPointerException.class, () -> IsoFinder.isoFinderImperativeStyle(users.get(0)));
    }

    @Test
    void given_list_OfUsers_when_isoFinderImperativeStyle_called_for_User1_then_throws_Exception() {
        assertThrows(NullPointerException.class, () -> IsoFinder.isoFinderImperativeStyle(users.get(1)));
    }

    @Test
    void given_list_OfUsers_when_isoFinderImperativeStyle_called_for_User2_then_throws_Exception() {
        assertThrows(NullPointerException.class, () -> IsoFinder.isoFinderImperativeStyle(users.get(2)));
    }

    @Test
    void given_list_OfUsers_when_isoFinderImperativeStyle_called_for_User3_then_throws_Exception() {
        assertNull(IsoFinder.isoFinderImperativeStyle(users.get(3)));
    }

    @Test
    void given_list_OfUsers_when_isoFinderImperativeStyle_called_for_User4_then_throws_Exception() {
        assertNull(IsoFinder.isoFinderImperativeStyle(users.get(4)));
    }

    @Test
    void given_list_OfUsers_when_isoFinderImperativeStyle_called_for_User5_then_gives_IND() {
        String isoCode = IsoFinder.isoFinderImperativeStyle(users.get(5));
        assertEquals("IND", isoCode);
    }

    @Test
    void given_list_OfUsers_when_isoFinderFunctionalStyle_called_for_null_as_User_then_gives_default() {
        String defaultStr = IsoFinder.isoFinderFunctionalStyle(users.get(0));
        assertEquals("default", defaultStr);
    }

    @Test
    void given_list_OfUsers_when_isoFinderFunctionalStyle_called_for_null_as_Address_then_gives_default() {
        String defaultStr = IsoFinder.isoFinderFunctionalStyle(users.get(1));
        assertEquals("default", defaultStr);
    }

    @Test
    void given_list_OfUsers_when_isoFinderFunctionalStyle_called_for_null_as_Country_then_gives_default() {
        String defaultStr = IsoFinder.isoFinderFunctionalStyle(users.get(2));
        assertEquals("default", defaultStr);
    }

    @Test
    void given_list_OfUsers_when_isoFinderFunctionalStyle_called_for_null_as_isocode_then_gives_default() {
        String defaultStr = IsoFinder.isoFinderFunctionalStyle(users.get(3));
        assertEquals("default", defaultStr);
    }

    @Test
    void given_list_OfUsers_when_isoFinderFunctionalStyle_called_for_IND_as_isocode_then_gives_IND() {
        String isoCode = IsoFinder.isoFinderFunctionalStyle(users.get(5));
        assertEquals("IND", isoCode);
    }
}
