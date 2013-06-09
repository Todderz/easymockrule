/*
 * Copyright 2012-2013 Alistair Todd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.easymockrule;

import com.googlecode.easymockrule.EasyMockRuleTest.ClassMock;
import com.googlecode.easymockrule.EasyMockRuleTest.InterfaceMock;
import com.googlecode.easymockrule.EasyMockRuleTest.ManualMock;
import com.googlecode.easymockrule.EasyMockRuleTest.Thing;
import com.googlecode.easymockrule.EasyMockRuleTest.WiredByTypeMock;

/**
 * A TestSubject that uses the mocks, with a mixture of classes and interfaces to mock, which will be wired by name or by type.
 * 
 * @author Alistair Todd <ringracer@gmail.com>
 */
public class EasyMockTestSubject extends EasyMockTestSubjectBase {

	private InterfaceMock interfaceMock;
	
	private WiredByTypeMock wiredByTypeMock;
	
	public ClassMock classMock;
	
	private ManualMock manualMock;

	public Thing useInterfaceMock() {
		return interfaceMock.getOneThing();
	}

	public Thing useWiredByTypeMock() {
		return wiredByTypeMock.getOneThing();
	}

	public Thing useClassMock() {
		return classMock.getOneThing();
	}

	public Thing useManuallySetMock() {
		return manualMock.getOneThing();
	}

	public Thing useWiredInTestSubjectSuperClassMock() {
		return wiredInTestSubjectSuperClassMock.getOneThing();
	}

	public Thing useWiredInTestSubjectSuperClassByTypeMock() {
		return wiredInTestSubjectSuperClassByTypeMock.getOneThing();
	}

	public void setManualMock(ManualMock manualMock) {
		this.manualMock = manualMock;
	}
}
