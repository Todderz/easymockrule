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

import com.googlecode.easymockrule.EasyMockRuleTest.WiredInTestSubjectSuperClassByTypeMock;
import com.googlecode.easymockrule.EasyMockRuleTest.WiredInTestSubjectSuperClassMock;

/**
 * Base class for the TestSubject allowing us to test injection of the mocks by name and by type into a TestSubject's base class.
 * 
 * @author Alistair Todd <ringracer@gmail.com>
 */
public class EasyMockTestSubjectBase {

	protected WiredInTestSubjectSuperClassMock wiredInTestSubjectSuperClassMock;

	protected WiredInTestSubjectSuperClassByTypeMock wiredInTestSubjectSuperClassByTypeMock;

	public void setWiredInTestSubjectSuperClassMock(WiredInTestSubjectSuperClassMock wiredInTestSubjectSuperClassMock) {
		this.wiredInTestSubjectSuperClassMock = wiredInTestSubjectSuperClassMock;
	}

	public void setWiredInTestSubjectSuperClassByTypeMock(
			WiredInTestSubjectSuperClassByTypeMock wiredInTestSubjectSuperClassByTypeMock) {
		this.wiredInTestSubjectSuperClassByTypeMock = wiredInTestSubjectSuperClassByTypeMock;
	}
}
