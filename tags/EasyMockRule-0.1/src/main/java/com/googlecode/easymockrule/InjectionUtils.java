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
import java.util.List;

/**
 * Utility code for auto-wired injection.
 * 
 * @author Alistair Todd <ringracer@gmail.com>
 */
public class InjectionUtils {

    /**
     * Inject candidate into the first matching target with a field of same type, or matching name
     * given by fieldName. Targets are scanned in order. First attempt to inject by name, then by
     * type. Returns as soon as an injection succeeds.
     * 
     * @param targets
     * @param candidate
     * @param fieldName
     * @throws Exception
     */
    public static void inject(List<Object> targets, Object candidate, String fieldName) throws Exception {

        for (Object target : targets) {

            if (injectByName(target, candidate, fieldName)) {
                return;
            }

            if (injectByType(target, candidate)) {
                return;
            }
        }
    }

    /**
     * Inject candidate into target where target has a field matching the type of candidate.
     * 
     * @param target
     * @param candidate
     * @return
     */
    public static boolean injectByType(Object target, Object candidate) {

        for (Field testSubjectField : target.getClass().getDeclaredFields()) {

            Class<?> targetFieldType = testSubjectField.getType();
            Class<?> candidateType = candidate.getClass();

            if (targetFieldType.isAssignableFrom(candidateType)) {
                try {
                    testSubjectField.setAccessible(true);
                    testSubjectField.set(target, candidate);
                    return true;
                } catch (Exception e) {
                    continue;
                }
            }
        }

        return false;
    }

    /**
     * Inject candidate into target where target has a field matching the fieldName.
     * 
     * @param testSubject
     * @param candidate
     * @param fieldName
     * @return
     */
    public static boolean injectByName(Object testSubject, Object candidate, String fieldName) {

        try {
            Field testSubjectField = testSubject.getClass().getDeclaredField(fieldName);
            testSubjectField.setAccessible(true);
            testSubjectField.set(testSubject, candidate);

            return true;

        } catch (Exception e) {
            return false;
        }
    }
}
