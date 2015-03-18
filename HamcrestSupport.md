# Hamcrest Matchers in EasyMock Expectation Setters #

When defining expectation setters in EasyMock, you can use Hamcrest matchers as argument matchers by adapting a given Hamcrest matcher into an EasyMock matcher.

EasyMockRule provides a convenience method to make it very easy to do this, and to leave your test code very clean.

Use the "EasyMockRule.with(Matcher matcher)" method. This is a static method so use a static import to leave your code fluent and clean.

Eg, use a standard Hamcrest matcher as an EasyMock argument matcher like so:

```
    expect(something.doStuff(with(hasProperty("name", equalTo(value)))).andStubReturn(result);
```

Or, use your own custom matcher, like this:

```
    expect(something.doStuff(with(hasWhatImLookingFor()))).andStubReturn(result);
 
    ...
 
    private Matcher<SomeThing> hasWhatImLookingFor() {
        return your-custom-matcher...
    }
```

This is a remarkably powerful way to handle complicated matching in expectation setters to achieve tests that are precisely as specific as they need to be, not over-specific (and therefore brittle) not under-specific (and therefore flimsy), whilst maintaining fluency and readability.