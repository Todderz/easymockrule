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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import org.easymock.internal.MocksControl;
import org.easymock.internal.RecordState;
import org.easymock.internal.ReplayState;
import org.junit.Rule;
import org.junit.Test;

/**
 * Unit tests for EasyMockRule.
 * 
 * @author Alistair Todd <ringracer@gmail.com>
 */
public class EasyMockRuleTest extends EasyMockRuleTestBase {

	private String expected = "expected";

	@Rule
	public EasyMockRule mocks = new EasyMockRule(this);

	@StrictMock
	private InterfaceMock interfaceMock;

	@NiceMock
	public ClassMock classMock;

	@StrictMock
	private WiredByTypeMock nameToCauseWiringByType;

	@NiceMock
	private WiredInTestSubjectSuperClassMock wiredInTestSubjectSuperClassMock;

	@Mock
	private WiredInTestSubjectSuperClassByTypeMock nameToCauseWiringInTestSubjectSuperClassByType;

	@TestSubject
	public EasyMockTestSubject testSubject;

	@Test
	public void shouldCreateAndInjectInterfaceMocks() throws Exception {

		expect(interfaceMock.getOneThing()).andReturn(new Thing(expected)).atLeastOnce();

		mocks.replayAll();

		Thing result = testSubject.useInterfaceMock();
		assertThat(result, hasProperty("name", equalTo(expected)));
	}

	@Test
	public void shouldCreateStrictMocks() throws Exception {
		assertThat(EasyMockUtils.getMockType(interfaceMock), is("STRICT"));
	}

	@Test
	public void shouldCreateNiceMocks() throws Exception {
		assertThat(EasyMockUtils.getMockType(wiredInTestSubjectSuperClassMock), is("NICE"));
	}

	@Test
	public void shouldCreateDefaultMocks() throws Exception {
		assertThat(EasyMockUtils.getMockType(nameToCauseWiringInTestSubjectSuperClassByType), is("DEFAULT"));
	}

	@Test
	public void shouldInjectMocksToPrivateFields() throws Exception {

		expect(interfaceMock.getOneThing()).andReturn(new Thing(expected)).atLeastOnce();

		mocks.replayAll();

		Thing result = testSubject.useInterfaceMock();
		assertThat(result, hasProperty("name", equalTo(expected)));
	}

	@Test
	public void shouldInjectByTypeWhenNameDoesntMatchField() throws Exception {

		expect(nameToCauseWiringByType.getOneThing()).andReturn(new Thing(expected)).atLeastOnce();

		mocks.replayAll();

		Thing result = testSubject.useWiredByTypeMock();
		assertThat(result, hasProperty("name", equalTo(expected)));
	}

	@Test
	public void shouldCreateAndInjectClassMocks() throws Exception {

		expect(classMock.getOneThing()).andReturn(new Thing(expected)).atLeastOnce();

		mocks.replayAll();

		Thing result = testSubject.useClassMock();
		assertThat(result, hasProperty("name", equalTo(expected)));
	}

	@Test
	public void shouldCreateAndInjectMocksDefinedInTestClassSuperClass() throws Exception {

		expect(classMock.getOneThing()).andReturn(new Thing(expected)).atLeastOnce();

		mocks.replayAll();

		Thing result = testSubject.useClassMock();
		assertThat(result, hasProperty("name", equalTo(expected)));
	}

	@Test
	public void shouldWireMocksInTestSubjectSuperClassByName() throws Exception {

		expect(wiredInTestSubjectSuperClassMock.getOneThing()).andReturn(new Thing(expected)).atLeastOnce();

		mocks.replayAll();

		Thing result = testSubject.useWiredInTestSubjectSuperClassMock();
		assertThat(result, hasProperty("name", equalTo(expected)));
	}

	@Test
	public void shouldWireMocksInTestSubjectSuperClassByTypeWhenNameDoesntMatchField() throws Exception {

		expect(nameToCauseWiringInTestSubjectSuperClassByType.getOneThing()).andReturn(new Thing(expected))
				.atLeastOnce();

		mocks.replayAll();

		Thing result = testSubject.useWiredInTestSubjectSuperClassByTypeMock();
		assertThat(result, hasProperty("name", equalTo(expected)));
	}

	@Test
	public void shouldAllowMocksToBeCreatedExternally() throws Exception {

		// Manually create and inject the mock
		ManualMock manuallyCreatedMock = createMock(ManualMock.class);
		testSubject.setManualMock(manuallyCreatedMock);

		// Confirm that the mock is in record state
		MocksControl interfaceMockControl = EasyMockUtils.getMockControl(manuallyCreatedMock);
		assertThat(interfaceMockControl.getState(), instanceOf(RecordState.class));

		expect(manuallyCreatedMock.getOneThing()).andReturn(new Thing(expected)).times(1);

		// Register the mock and use the Rule to switch it to reply
		mocks.registerMock(manuallyCreatedMock);
		mocks.replayAll();

		// Confirm that the mock has been switched into replay by the Rule
		assertThat(interfaceMockControl.getState(), instanceOf(ReplayState.class));

		// Check it actually works, just for fun
		Thing result = testSubject.useManuallySetMock();
		assertThat(result, hasProperty("name", equalTo(expected)));
	}

	@Test
	public void shouldAllowMocksToBeCreatedWithoutAnnotations() throws Exception {

		ManualMock assistedCreationMock = mocks.createMock("somename", ManualMock.class);

		// Confirm that the mock is in record state
		MocksControl assistedCreationMockControl = EasyMockUtils.getMockControl(assistedCreationMock);
		assertThat(assistedCreationMockControl.getState(), instanceOf(RecordState.class));

		EasyMockTestSubject thingThatCallsTheMocks = new EasyMockTestSubject();
		thingThatCallsTheMocks.setManualMock(assistedCreationMock);

		expect(assistedCreationMock.getOneThing()).andReturn(new Thing(expected)).times(1);

		mocks.replayAll();

		// Confirm that the mock has been switched into replay by the Rule
		assertThat(assistedCreationMockControl.getState(), instanceOf(ReplayState.class));

		Thing result = thingThatCallsTheMocks.useManuallySetMock();
		assertThat(result, hasProperty("name", equalTo(expected)));
	}

	@Test
	public void shouldMakeNamesSafeWhenAssistingMockCreation() throws Exception {

		ManualMock assistedCreationMock = mocks.createMock("some.illegal.name", ManualMock.class);

		assertThat(assistedCreationMock.toString(), is("some_illegal_name"));
	}

	/**
	 * Just a simple class we can use when mocking methods.
	 * 
	 * @author Alistair Todd <ringracer@gmail.com>
	 */
	public class Thing {

		private String name;

		public Thing(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	/**
	 * An interface that we will mock with EasyMockRule.
	 * 
	 * @author Alistair Todd <ringracer@gmail.com>
	 */
	public interface InterfaceMock {
		public Thing getOneThing();
	}

	/**
	 * An interface for which we will manually create a mock.
	 * 
	 * @author Alistair Todd <ringracer@gmail.com>
	 */
	public interface ManualMock {
		public Thing getOneThing();
	}

	/**
	 * An interface that we will mock with EasyMockRule and inject by type.
	 * 
	 * @author Alistair Todd <ringracer@gmail.com>
	 */
	public interface WiredByTypeMock {
		public Thing getOneThing();
	}

	/**
	 * A class that we will mock with EasyMockRule.
	 * 
	 * @author Alistair Todd <ringracer@gmail.com>
	 */
	public class ClassMock {
		public Thing getOneThing() {
			return null;
		}
	}

	/**
	 * A class that we will mock with EasyMockRule from an annotated declaration in the test case base class.
	 * 
	 * @author Alistair Todd <ringracer@gmail.com>
	 */
	public class DeclaredInTestCaseSuperClassMock {
		public Thing getOneThing() {
			return null;
		}
	}

	/**
	 * An interface that we will mock with EasyMockRule and inject by name into the TestSubject's base class.
	 * 
	 * @author Alistair Todd <ringracer@gmail.com>
	 */
	public interface WiredInTestSubjectSuperClassMock {
		public Thing getOneThing();
	}

	/**
	 * An interface that we will mock with EasyMockRule and inject by type into the TestSubject's base class.
	 * 
	 * @author Alistair Todd <ringracer@gmail.com>
	 */
	public interface WiredInTestSubjectSuperClassByTypeMock {
		public Thing getOneThing();
	}
}
