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

import static net.sf.cglib.proxy.Enhancer.isEnhanced;

import org.easymock.EasyMockSupport;
import org.easymock.internal.MocksControl;

/**
 * Extends EasyMockSupport to allow registration of already created mock.
 * 
 * @author Alistair Todd <ringracer@gmail.com>
 */
public class ExternalMockSupport extends EasyMockSupport {

	/**
	 * Register a mock to be managed by this instance for verifyAll etc.
	 * 
	 * @param mock
	 */
	public void registerMock(Object mock) {
		controls.add(getMockControl(mock));
	}

	/**
	 * Remove a mock from the list being managed.
	 * 
	 * @param mock
	 */
	public void deregisterMock(Object mock) {
		controls.remove(getMockControl(mock));
	}

	/**
	 * Get the IMocksControl instance that owns the supplied mock. Throws
	 * exception if the supplied object is not in fact a mock.
	 * 
	 * @param mock
	 * @return
	 */
	public MocksControl getMockControl(Object mock) {

		if (isEnhanced(mock.getClass())) {
			// TODO [atodd] Work out how to get the control for a CGLib class
			// proxy
			throw new IllegalArgumentException("Can't register a class mock. Only supports registering interface mocks");
		}

		return EasyMockUtils.getMockControl(mock);
	}

}
