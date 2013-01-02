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

import static com.googlecode.easymockrule.InjectionUtils.inject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * JUnit Rule that automates the creation, injection, and verification of mocks
 * for EasyMock. The instance of EasyMocks must be public and annotated with
 * &#64;Rule. Mocks must be annotated with one of the mock annotations from this
 * package, and should not be initialised - this rule will create them for you.
 * Annotate a class with &#64;TestSubject and the mocks will be injected
 * automatically. Wiring is attempted first by field name if the field name of
 * your mock in your test class matches the field name in the TestSubject. If
 * that fails, wiring by type will be attempted.
 * 
 * @author Alistair Todd <ringracer@gmail.com>
 */
public class EasyMockRule implements TestRule {

	private Object testClass;

	private List<Object> testSubjects = new ArrayList<Object>();

	private MockManager mocks = new MockManager();

	/**
	 * Supply a reference to the test class containing mock annotations.
	 * 
	 * @param testClass
	 */
	public EasyMockRule(Object testClass) {
		this.testClass = testClass;
	}

	@Override
	public Statement apply(final Statement base, Description description) {

		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				createMocks();
				base.evaluate();
				mocks.verifyAll();
			}
		};
	}

	private void createMocks() throws Exception {

		findTestSubjects();
		injectMocks();
	}

	private void findTestSubjects() throws Exception {

		for (Field f : testClass.getClass().getDeclaredFields()) {

			if (f.isAnnotationPresent(TestSubject.class)) {
				testSubjects.add(getOrCreateTestSubjectInstance(f));
			}
		}
	}

	private Object getOrCreateTestSubjectInstance(Field f) throws Exception {

		f.setAccessible(true);
		Object testSubject = f.get(testClass);

		if (testSubject == null) {
			return setNewInstance(f);
		}

		return testSubject;
	}

	private Object setNewInstance(Field f) throws Exception {

		Object testSubject = f.getType().newInstance();

		f.setAccessible(true);
		f.set(testClass, testSubject);

		return testSubject;
	}

	private void injectMocks() throws Exception {

		Class<?> currentClass = testClass.getClass();

		while (isUserDefined(currentClass)) {
			injectMocksForCurrentClass(currentClass);
			currentClass = currentClass.getSuperclass();
		}
	}

	private boolean isUserDefined(Class<?> currentClass) {
		return currentClass != Object.class;
	}

	private void injectMocksForCurrentClass(Class<?> currentClass)
			throws Exception {

		for (Field f : currentClass.getDeclaredFields()) {

			if (f.isAnnotationPresent(Mock.class)) {
				setAndInjectMock(f, mocks.createMock(f));
				continue;
			}

			if (f.isAnnotationPresent(NiceMock.class)) {
				setAndInjectMock(f, mocks.createNiceMock(f));
				continue;
			}

			if (f.isAnnotationPresent(StrictMock.class)) {
				setAndInjectMock(f, mocks.createStrictMock(f));
				continue;
			}
		}
	}

	private void setAndInjectMock(Field f, Object mock) throws Exception {

		f.setAccessible(true);
		f.set(testClass, mock);
		inject(testSubjects, mock, f.getName());
	}

	public void replayAll() {
		mocks.replayAll();
	}
}
