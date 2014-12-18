# Java to JSON Schema v.4 mapper

The goal of this project is to provide comprehensive, feature-complete and well-tested mapper from Java classes to JSON schema format.

Sample usage:

```java
class Demo {
	private boolean mindBlowing;

	public boolean isMindBlowing() {
		return mindBlowing;
	}

}

JSONObject schema = new SchemaMapper().toJsonSchema4(Demo.class);
```

`schema` holds following JSON structure:

```javascript
{
    "$schema": "http://json-schema.org/draft-04/schema#",
    "additionalProperties": false,
    "type": "object",
    "properties": {
    	"mindBlowing": {"type": "boolean"}
    },
    "required": ["mindBlowing"]
}
```
# Features

# Usage

Single instance of `SchemaMapper` class can be re-used multiple times to map types to JSON schema. The method `SchemaMapper.toJsonSchema4()` with its overloads transforms given type into `JSONObject`.

## dependencies

The JSON schema returned by method `toJsonSchema4()` contains type definition of the method call argument. If type properties cannot be mapped directly to JSON schema types (primitive types, String) their types are replaced by external references. Example:

```java
class Demo {
	private Exception exception;
	private Thread thread;
	private String text;

	public Exception getException() {
		return exception;
	}

	public Thread getThread() {
		return thread;
	}
	
	public String getText() {
		return text;
	}
}

SchemaMapper mapper = new SchemaMapper();
JSONObject schema = mapper.toJsonSchema4(Demo.class);
```
`schema` holds following JSON structure:

```javascript
{
    "$schema": "http://json-schema.org/draft-04/schema#",
    "additionalProperties": false,
    "type": "object",
    "properties": {
        "exception": {"$ref": "Exception"},
        "string": {"type": "string"},
        "thread": {"$ref": "Thread"}
    }
}
```

Call to `SchemaMapper.getDependencies()` returns `Iterator<Type>` over set of referenced types. In the presented example the Iterator would iterate over `java.lang.Exception` and `java.lang.Thread`. The returned types can be afterwards used as arguments to consecutive calls to `SchemaMapper.toJsonSchema4()`.

Note: The values returned by the `Iterator` are always unique.

# Producing complete schema in one call

It is possible to produce complete JSON schema for given type in one call to overloaded `SchemaMapper.toJsonSchema4(Class<?> clazz, boolean includeDependencies)` with `includeDependencies = true`. Example:

```java
class Dependency<T> {
	private T name;

	public T getName() {
		return name;
	}
}

class Demo {
	private Dependency<String> stringDependency;

	public Dependency<String> getStringDependency() {
		return stringDependency;
	}
}

final JSONObject schema = new SchemaMapper().toJsonSchema4(Demo.class, true);
```
`schema` holds follwing JSON schema structure:
```javascript
{
    "additionalProperties": false,
    "type": "object",
    "definitions": {"Dependency<String>": {
        "additionalProperties": false,
        "type": "object",
        "properties": {"name": {"type": "string"}}
    }},
    "properties": {"stringDependency": {"$ref": "#/definitions/Dependency<String>"}}
}
```

# Configuration
## reference name prefix

In order to set reference type prefix, like `"exception": {"$ref": "prefix_Exception"}`, one can pass subclassed implementation of `DefaultReferenceNameProvider` to the `SchemaMapper.setReferenceNameProvider()` method. Example:

```java
mapper.setReferenceNameProvider(new DefaultReferenceNameProvider() {
	@Override
	protected String getPrefix() {
		return "prefix_";
	}
});
```

## property discovery mode

By default schema mapper discovers type properties by looking up for fields with getters. This behaviour can be overridden in order to discover properties by getters alone. Discovery by getters is especially useful when generating JSON schema for interfaces.

```java
interface Demo {
	int getNumber();
}

final SchemaMapper mapper = new SchemaMapper();
mapper.setPropertyDiscoveryMode(PropertyDiscoveryMode.GETTER);
final JSONObject schema = mapper.toJsonSchema4(Demo.class);
```

produces:

```javascript
{
    "$schema": "http://json-schema.org/draft-04/schema#",
    "additionalProperties": false,
    "type": "object",
    "properties": {"number": {"$ref": "int"}},
    "required": ["number"]
}
```

## date-time format

By default schema mapper produces following schema for `java.util.Date` and `java.util.Calendar` types:

```javascript
{
    "type": "string",
	"format": "date-time"
}
```

The type format can be overridden by passing the desired date format String to the `SchemaMapper.setDateTimeFormat()` method. For example `SchemaMapper.setDateTimeFormat("date")` would produce `"format": "date"`.

## strict/relaxed mode

There are two modes of schema mapper operation:

* strict (default)
* relaxed

In strict mode mapper will throw `MappingException` each time it encounters type that for any reason cannot be mapped. Relax mode will gracefully accept the type and output schema with `"type": "any"`. To set schema mapper to relaxed mode, call `SchemaMapper.setRelaxedMode(true)`.

## annotations denoting required (not nullable) fields

JSON schema produced by schema mapper contains `"required":[...]` array property with a list of parameters that are obligaroty. By default the list contains fields of primitive type, that is types that cannot take `null` value. It is possible to make schema mapper aware of the other types that also should be treated as mandatory and that should be added to the list. Simply annotate the fields with any annotation, like for example JSR-303 `javax.validation.constraints.NotNull` and let schema mapper react to the annnotation by passing it's type to `SchemaMapper.setRequiredFieldAnnotations()`

## type descriptions

To include type description in produced JSON schema's `"description": ...` property, register implementation of `pl.zientarski.DescriptionProvider` with call to `SchemaMapper.setDescriptionProvider()`. If no `pl.zientarski.DescriptionProvider` implementation is provided, the produced schema will not include type `"description": ...`. Example implementation producing description with Java simple type name looks following:

```java
mapper.setDescriptionProvider(new DescriptionProvider(){
	@Override
	public String process(Type type){
		return type.toString();
	}
});
```

The sample output for the registered `pl.zientarski.DescriptionProvider` would look following:

```javascript
{
    "$schema": "http://json-schema.org/draft-04/schema#",
    "description": "class pl.zientarski.DocumentationTest$6Demo",
    "additionalProperties": false,
    "type": "object",
    "properties": {"field": {"$ref": "int"}},
    "required": ["field"]
}
```

# Custom type handlers

In rare situations user may want to completely override the mapping of particular type to JSON schema. This could be done by implementing custom `TypeHandler` and registering it in schema mapper by calling `SchemaMapper.addTypeHandler()` method. The `TypeHandler` implementation should provide two methods:

* `boolean accepts(Type type)` should return `true` only if the type handler is designed to override default mapping of the given type
* `JSONObject process(TypeDescription, MapperContext)` should return JSON schema for a given type. Example:

```java
class DeathStar {}

final SchemaMapper mapper = new SchemaMapper();
mapper.addTypeHandler(new TypeHandler() {
	@Override
	public boolean accepts(Type type) {
		return type.equals(DeathStar.class);
	}

	@Override
	public JSONObject process(TypeDescription typeDescription, MapperContext mapperContext) {
		final JSONObject result = new JSONObject();
		result.put("construction", "completed");
		return result;
	}
});

final JSONObject schema = mapper.toJsonSchema4(DeathStar.class);
```

schema is equal to

```javascript
{"construction": "completed"}
```
# Maturity

The project is well tested with battery of unit tests (TODO test report, TODO coverage), and is used in production environments.

# Using

The project can be used with Maven/Gradle by referencing following repository:

```xml
TODO
name:
url:
```

and following project dependency

```xml
TODO
group:
name:
varsion:
```

# License
TODO

# Known issues

---

# Version history
## 0.0.1
* Initial version