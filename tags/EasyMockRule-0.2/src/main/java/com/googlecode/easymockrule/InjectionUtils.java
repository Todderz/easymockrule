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

	private InjectionUtils() {
		// Hide utility class constructor
	}

	/**
	 * Inject candidate into the first matching target with a field of same
	 * type, or matching name given by fieldName. Targets are scanned in order.
	 * First attempt to inject by name, then by type. Returns as soon as an
	 * injection succeeds.
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
	 * Inject candidate into target where target has a field matching the
	 * fieldName.
	 * 
	 * @param testSubject
	 * @param candidate
	 * @param fieldName
	 * @return
	 */
	public static boolean injectByName(Object testSubject, Object candidate, String fieldName) {

		try {
			Field testSubjectField = findField(testSubject.getClass(), fieldName);
			testSubjectField.setAccessible(true);
			testSubjectField.set(testSubject, candidate);

			return true;

		} catch (Exception e) {
			return false;
		}
	}

	// Work up through super classes looking for the named field.
	private static Field findField(Class<?> currentClass, String fieldName) throws Exception {

		while (isUserDefined(currentClass)) {

			try {
				Field f = currentClass.getDeclaredField(fieldName);

				if (f != null) {
					return f;
				}

			} catch (Exception e) {
				// Ignore
			} finally {
				currentClass = currentClass.getSuperclass();
			}
		}

		throw new Exception();
	}

	/**
	 * Is this given class a user defined class not a Java library class?
	 * Actually, we just check that this class is not Object.class, because
	 * that's generally going to be good enough and not difficult.
	 * 
	 * @param candidateClass
	 *            class to check
	 * @return true if currentClass is not Object.
	 */
	public static boolean isUserDefined(Class<?> candidateClass) {
		return candidateClass != Object.class;
	}

	// Inject candidate into target where target has a field matching the type
	// of candidate, working up through super classes until we find a match.
	private static boolean injectByType(Object startingPoint, Object candidate) {

		Class<?> currentClass = startingPoint.getClass();

		while (isUserDefined(currentClass)) {

			if (wireByType(startingPoint, candidate, currentClass)) {
				return true;
			}

			currentClass = currentClass.getSuperclass();
		}

		return false;
	}

	// Try to find a field in the given class with a type that allows the candidate to be set.
	private static boolean wireByType(Object startingPoint, Object candidate, Class<?> currentClass) {

		for (Field testSubjectField : currentClass.getDeclaredFields()) {

			Class<?> targetFieldType = testSubjectField.getType();
			Class<?> candidateType = candidate.getClass();

			if (targetFieldType.isAssignableFrom(candidateType)) {
				try {
					testSubjectField.setAccessible(true);
					testSubjectField.set(startingPoint, candidate);

					return true;

				} catch (Exception e) {
					continue;
				}
			}
		}

		return false;
	}
	
}
