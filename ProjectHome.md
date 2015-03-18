**Annotations for EasyMock.**

Looking for EasyMock annotations?

Want to use a simple annotation to establish a mock using EasyMock, and not have to do any manual coding to create mocks, inject them into test subjects, and remember to verify them?

EasyMockRule is a JUnit Rule for annotation-driven automatic creation, injection, management and verification of EasyMock mock objects.

Instead of creating mocks, injecting them into the test subject, and managing calls to replay and verify, you can now simply use annotations like:
```
@NiceMock
private SomeType aMockedThing;
```
and
```
@TestSubject
private SomeOtherType thingThatUsesTheMock;
```
and then the mocks will be **automatically created, injected and verified** for each test, leaving your code free of mocking related clutter and allowing the reader to focus on the purpose of your test.

Supports EasyMock 2.5.2 and allows mocking of classes and interfaces without having to use the classextension package.

Annotation support for EasyMock makes your test code faster to write, easier to maintain, and much clearer to read.

The EasyMock annotation immediately communicates that the field is a Mock, and the autowiring (injection by name or type) into the test subject, along with automatic creation and verification, means that simply labeling something with the annotation for Mock, NiceMock, or StrictMock, causes it to behave the way a mock should.

Just like @Test is all you have to do to make a method behave as a test and do all the things test methods should do, without any further wiring or engineering, now @Mock is all you need to make something do what mocks should do.

Annotations for EasyMock. Add the jar to your pom / classpath, one line of code for the JUnit Test Rule, and a few simple annotations. Job done.


**Greatly improved version 0.2:**

  * Better injection capabilities
  * Create your own mocks without annotations but still have them managed by EasyMockRule
  * Clean and fluent api for using Hamcrest matchers in EasyMock expectation setters


