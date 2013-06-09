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
import static com.googlecode.easymockrule.InjectionUtils.isUserDefined;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.integration.EasyMock2Adapter;
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

	/**
	 * Register a Hamcrest matcher, allowing it to be used in EasyMock
	 * expectations.
	 * 
	 * @param matcher
	 * @return
	 */
	public static <T> T with(Matcher<T> matcher) {
		EasyMock2Adapter.adapt(matcher);
		return null;
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

	private void injectMocksForCurrentClass(Class<?> currentClass) throws Exception {

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

	/**
	 * Replay all mocks. Note that verify is called automatically by the Rule.
	 */
	public void replayAll() {
		mocks.replayAll();
	}

	/**
	 * Register a mock to be managed by this instance for verifyAll etc.
	 * 
	 * @param mock
	 */
	public void registerMock(Object mock) {
		mocks.registerMock(mock);
	}

	/**
	 * Remove a mock from the list being managed.
	 * 
	 * @param mock
	 */
	public void deregisterMock(Object mock) {
		mocks.deregisterMock(mock);
	}

	/**
	 * Create a Mock that will be managed by the rule, ie just like using
	 * "@Mock".
	 * 
	 * @param name
	 *            Must be a valid java identifier eg no periods
	 * @param toMock
	 *            may be class or interface
	 * @return
	 */
	public <T> T createMock(String name, Class<T> toMock) {
		return mocks.createMock(name, toMock);
	}

	/**
	 * Create a NiceMock that will be managed by the rule, ie just like using
	 * "@NiceMock".
	 * 
	 * @param name
	 *            Must be a valid java identifier eg no periods
	 * @param toMock
	 *            may be class or interface
	 * @return
	 */
	public <T> T createNiceMock(String name, Class<T> toMock) {
		return mocks.createNiceMock(name, toMock);
	}

	/**
	 * Create a StrictMock that will be managed by the rule, ie just like using
	 * "@StrictMock".
	 * 
	 * @param name
	 *            Must be a valid java identifier eg no periods
	 * @param toMock
	 *            may be class or interface
	 * @return
	 */
	public <T> T createStrictMock(String name, Class<T> toMock) {
		return mocks.createStrictMock(name, toMock);
	}

	/**
	 * Create a Mock that will be managed by the rule, ie just like using
	 * "@Mock", with a name that will be derived from class.getName().
	 * 
	 * @param toMock
	 *            may be class or interface
	 * @return
	 * @deprecated Use createMock(String name, Class<T> toMock)
	 */
	@Deprecated
	public <T> T createMock(Class<T> toMock) {
		return mocks.createMock(toMock);
	}

	/**
	 * Create a NiceMock that will be managed by the rule, ie just like using
	 * "@NiceMock", with a name that will be derived from class.getName().
	 * 
	 * @param toMock
	 *            may be class or interface
	 * @return
	 * @deprecated Use createNiceMock(String name, Class<T> toMock)
	 */
	@Deprecated
	public <T> T createNiceMock(Class<T> toMock) {
		return mocks.createNiceMock(toMock);
	}

	/**
	 * Create a StrictMock that will be managed by the rule, ie just like using
	 * "@StrictMock", with a name that will be derived from class.getName().
	 * 
	 * @param toMock
	 *            may be class or interface
	 * @return
	 * @deprecated Use createStrictMock(String name, Class<T> toMock)
	 */
	@Deprecated
	public <T> T createStrictMock(Class<T> toMock) {
		return mocks.createStrictMock(toMock);
	}
}
