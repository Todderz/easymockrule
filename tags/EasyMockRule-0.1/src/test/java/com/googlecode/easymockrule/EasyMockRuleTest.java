package com.googlecode.easymockrule;

import static java.util.Arrays.asList;
import static org.easymock.EasyMock.expect;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;

import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;


import com.googlecode.easymockrule.EasyMockRule;
import com.googlecode.easymockrule.NiceMock;
import com.googlecode.easymockrule.StrictMock;
import com.googlecode.easymockrule.TestSubject;
import com.googlecode.easymockrule.EasyMockRuleTest.Thing;

public class EasyMockRuleTest extends EasyMockRuleExampleBase{

    @Rule
    public EasyMockRule mocks = new EasyMockRule(this);

    @StrictMock
    public OneDAO nameThatWontBeWiredByName;

    @TestSubject
    public EasyMocksTestComponent thingThatCallsTheMockDao;

    @Test
    public void shouldUseMocksWithoutAllTheClutter() throws Exception {

        String expected1 = "Name 1";
        String expected2 = "Name 2";

        expect(nameThatWontBeWiredByName.getListOfThings()).andReturn(asList(new Thing(expected1),new Thing(expected2))).atLeastOnce();
        expect(classDAO.getOneThing()).andReturn(new Thing(expected2)).atLeastOnce();

        mocks.replayAll();

        List<Thing> allThings = thingThatCallsTheMockDao.useDependencyOne();

        assertThat(allThings, hasSize(2));
        assertThat(allThings, hasItem(hasProperty("name", equalTo(expected1))));
        assertThat(allThings, hasItem(hasProperty("name", equalTo(expected2))));
        
        Thing oneThing = thingThatCallsTheMockDao.useDependencyTwo();
        assertThat(oneThing, hasProperty("name", equalTo(expected2)));
    }

    private Matcher<? super List<Thing>> hasItem(Matcher<Object> hasProperty) {
        return Matchers.<Thing> hasItem(hasProperty);
    }

    public class Thing {

        private String name;

        public Thing(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public interface OneDAO {
        public List<Thing> getListOfThings();
    }

    public class TwoDAO {
		public Thing getOneThing() {
			return null;
		}
	}

}
