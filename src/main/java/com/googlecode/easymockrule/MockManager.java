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
 * Allows creation of mocks for classes or interfaces, and operations on the collection of created
 * mocks such as verifyAll, replayAll, for EasyMock 252.
 * 
 * @author Alistair Todd <ringracer@gmail.com>
 */
public class MockManager {

    private EasyMockSupport interfaceMocks = new EasyMockSupport();
    private org.easymock.classextension.EasyMockSupport classMocks = new org.easymock.classextension.EasyMockSupport();

    public void replayAll() {
        interfaceMocks.replayAll();
        classMocks.replayAll();
    }

    public void verifyAll() {

        // This is simply to avoid requiring simple test methods that don't use the mocks to have to
        // call replayAll. A test that does use the mocks will still fail if it forgets to call
        // replayAll before exercising the mocks.

        try {
            interfaceMocks.verifyAll();
        } catch (IllegalStateException e) {
            interfaceMocks.replayAll();
            interfaceMocks.verifyAll();
        }

        try {
            classMocks.verifyAll();
        } catch (IllegalStateException e) {
            classMocks.replayAll();
            classMocks.verifyAll();
        }
    }

    public Object createMock(Field f) throws Exception {

        if (f.getType().isInterface()) {
            return interfaceMocks.createMock(f.getName(), f.getType());
        } else {
            return classMocks.createMock(f.getName(), f.getType());
        }
    }

    public Object createNiceMock(Field f) throws Exception {

        if (f.getType().isInterface()) {
            return interfaceMocks.createNiceMock(f.getName(), f.getType());
        } else {
            return classMocks.createNiceMock(f.getName(), f.getType());
        }
    }

    public Object createStrictMock(Field f) throws Exception {

        if (f.getType().isInterface()) {
            return interfaceMocks.createStrictMock(f.getName(), f.getType());
        } else {
            return classMocks.createStrictMock(f.getName(), f.getType());
        }
    }
}
