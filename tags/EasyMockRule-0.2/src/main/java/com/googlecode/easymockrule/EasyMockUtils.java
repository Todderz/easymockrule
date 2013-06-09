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
import java.lang.reflect.Proxy;

import org.apache.commons.lang.StringUtils;
import org.easymock.internal.MocksControl;
import org.easymock.internal.MocksControl.MockType;
import org.easymock.internal.ObjectMethodsFilter;

/**
 * Utility methods for working with EasyMock.
 * 
 * @author Alistair Todd <ringracer@gmail.com>
 */
public class EasyMockUtils {

	private EasyMockUtils() {
		// Hide utility class constructor
	}

	/**
	 * Get the control owning the supplied mock. Will blow up if the object
	 * passed is not a mock.
	 * 
	 * @param mock
	 * @return
	 */
	public static MocksControl getMockControl(Object mock) {
		return getInvocationHandler(mock).getDelegate().getControl();
	}

	private static ObjectMethodsFilter getInvocationHandler(Object mock) {
		return (ObjectMethodsFilter) Proxy.getInvocationHandler(mock);
	}

	/**
	 * Get the mock control "type" field value or empty string on error.
	 * 
	 * @param mock
	 * @return
	 */
	public static String getMockType(Object mock) {

		try {
			MocksControl mockControl = getMockControl(mock);

			Field mockTypeField = mockControl.getClass().getDeclaredField("type");
			mockTypeField.setAccessible(true);

			MockType mockType = (MockType) mockTypeField.get(mockControl);

			return mockType.toString();

		} catch (Exception e) {
			return StringUtils.EMPTY;
		}
	}
}
