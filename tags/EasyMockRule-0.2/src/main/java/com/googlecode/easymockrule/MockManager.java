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

import java.lang.reflect.Field;

import org.easymock.EasyMockSupport;

/**
 * Allows creation of mocks for classes or interfaces, and operations on the
 * collection of created mocks such as verifyAll, replayAll, for EasyMock 252.
 * 
 * @author Alistair Todd <ringracer@gmail.com>
 */
public class MockManager {

	private ExternalMockSupport interfaceMocks = new ExternalMockSupport();
	private org.easymock.classextension.EasyMockSupport classMocks = new org.easymock.classextension.EasyMockSupport();

	/**
	 * Replay all mocks known by this instance.
	 */
	public void replayAll() {
		interfaceMocks.replayAll();
		classMocks.replayAll();
	}

	/**
	 * Verify all mocks known by this instance, first switching all mocks to
	 * replay mode if not already in replay mode, so that simple test cases that
	 * don't use the mocks do not need to call replayAll. A test that does use
	 * the mocks will still fail if it forgets to call replayAll before
	 * exercising the mocks.
	 */
	public void verifyAll() {

		safeVerifyAll(interfaceMocks);
		safeVerifyAll(classMocks);
	}

	private void safeVerifyAll(EasyMockSupport mockSupport) {
		try {
			mockSupport.verifyAll();
		} catch (IllegalStateException e) {
			mockSupport.replayAll();
			mockSupport.verifyAll();
		}
	}

	/**
	 * Create a default mock for the given Field, whether it is a class or an interface, using the field name as the mock name.
	 * 
	 * @param f Field for which to create mock
	 * @return Default mock for the Field
	 */
	public Object createMock(Field f)  {
		return createMock(f.getName(), f.getType());
	}

	/**
	 * Create a default mock for the given Class, whether it is a class or an interface, using the class name as the mock name.
	 * 
	 * @param toMock Class for which to create mock
	 * @return Default mock for the Class
	 */
	public <T> T createMock(Class<T> toMock) {
		return createMock(toMock.getName(), toMock);
	}

	/**
	 * Create a default mock for the given Class, whether it is a class or an interface, using the given name as the mock name.
	 * 
	 * @param name Name for the mock
	 * @param toMock Class for which to create mock 
	 * @return Default mock for the Class
	 */
	public <T> T createMock(String name, Class<T> toMock) {

		if (toMock.isInterface()) {
			return interfaceMocks.createMock(safe(name), toMock);

		} else {
			return classMocks.createMock(safe(name), toMock);

		}
	}

	/**
	 * Create a nice mock for the given Field, whether it is a class or an interface, using the field name as the mock name.
	 * 
	 * @param f Field for which to create mock
	 * @return Nice mock for the Field
	 */
	public Object createNiceMock(Field f) {
		return createNiceMock(f.getName(), f.getType());
	}

	/**
	 * Create a nice mock for the given Class, whether it is a class or an interface, using the class name as the mock name.
	 * 
	 * @param toMock Class for which to create mock
	 * @return Nice mock for the Class
	 */
	public <T> T createNiceMock(Class<T> toMock) {
		return createNiceMock(toMock.getName(), toMock);
	}

	/**
	 * Create a nice mock for the given Class, whether it is a class or an interface, using the given name as the mock name.
	 * 
	 * @param name Name for the mock
	 * @param toMock Class for which to create mock 
	 * @return Nice mock for the Class
	 */
	public <T> T createNiceMock(String name, Class<T> toMock) {

		if (toMock.isInterface()) {
			return interfaceMocks.createNiceMock(safe(name), toMock);

		} else {
			return classMocks.createNiceMock(safe(name), toMock);

		}
	}

	/**
	 * Create a strict mock for the given Field, whether it is a class or an interface, using the field name as the mock name.
	 * 
	 * @param f Field for which to create mock
	 * @return Strict mock for the Field
	 */
	public Object createStrictMock(Field f) {
		return createStrictMock(f.getName(), f.getType());
	}

	/**
	 * Create a strict mock for the given Class, whether it is a class or an interface, using the class name as the mock name.
	 * 
	 * @param toMock Class for which to create mock
	 * @return Strict mock for the Class
	 */
	public <T> T createStrictMock(Class<T> toMock) {
		return createStrictMock(toMock.getName(), toMock);
	}

	/**
	 * Create a strict mock for the given Class, whether it is a class or an interface, using the given name as the mock name.
	 * 
	 * @param name Name for the mock
	 * @param toMock Class for which to create mock 
	 * @return Strict mock for the Class
	 */
	public <T> T createStrictMock(String name, Class<T> toMock) {

		if (toMock.isInterface()) {
			return interfaceMocks.createStrictMock(safe(name), toMock);

		} else {
			return classMocks.createStrictMock(safe(name), toMock);

		}
	}

	// EasyMock doesn't allow names that aren't valid java identifiers.
	private <T> String safe(String name) {
		return name.replaceAll("\\.", "_");
	}

	/**
	 * Add the given mock to be managed by this instance.
	 * 
	 * @param mock A Mock to manage
	 */
	public void registerMock(Object mock) {
		interfaceMocks.registerMock(mock);
	}

	/**
	 * Remove the given mock from management by this instance.
	 * 
	 * @param mock The Mock to stop managing.
	 */
	public void deregisterMock(Object mock) {
		interfaceMocks.deregisterMock(mock);
	}
}
