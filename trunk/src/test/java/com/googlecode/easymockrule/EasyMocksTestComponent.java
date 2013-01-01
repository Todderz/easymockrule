package com.googlecode.easymockrule;

import java.util.List;

import com.googlecode.easymockrule.EasyMockRuleTest.OneDAO;
import com.googlecode.easymockrule.EasyMockRuleTest.Thing;
import com.googlecode.easymockrule.EasyMockRuleTest.TwoDAO;

public class EasyMocksTestComponent {

    private OneDAO interfaceDAO;
    public TwoDAO classDAO;

    public List<Thing> useDependencyOne() {
        return interfaceDAO.getListOfThings();
    }
    
    public Thing useDependencyTwo(){
    	return classDAO.getOneThing();
    }

}
