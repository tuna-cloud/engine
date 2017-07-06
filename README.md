# Protocol-Engine
ProtocolEngine is a binary protocol decoder or encoder, its work just like struct in C language,while the ProtocolEngine also
provide much more advanced features.

## Installation
To use ProtocolEngine you just need to include the
[com-github-ioprotocol-x.x.x.jar](https://github.com/ioprotocol/engine) file in the classpath.

If you are using Maven just add the following dependency to your pom.xml:

```xml
<dependency>
    <groupId>com.github.ioprotocol</groupId>
    <artifactId>com-github-ioprotocol</artifactId>
    <version>1.0.2</version>
</dependency>
```
## How to use it ?
```java
ProtocolEngine engin = new ProtocolEngine();
JavaBean bean = engine.decode(byte[] buf, JavaBean.class);
// or
byte[] buf = engine.encode(bean);
```

## Simple Example
Now we use a simple binary protocol begin our travel.<br>
The binary protocol like this:<br>
```java
+--------+---------+--------+----------------+
| Header | Version | Length |   Content      |
| 0x2882 |   0x01  | 0x000B | "HELLO, WORLD" |
+--------+---------+--------+----------------+
```
First we need define a java class like this:<br>
```java

import com.github.io.protocol.annotation.AsciiString;
import com.github.io.protocol.annotation.ByteOrder;
import com.github.io.protocol.annotation.Number;

public class ProtocolTest {
    @Number(width = 16, order = ByteOrder.BigEndian)
    private int header;
    @Number(width = 8)
    private int version;
    @Number(width = 16, order = ByteOrder.BigEndian)
    private int contentLength;
    @AsciiString(length = "getContentLength")
    private String asccString;

    public int getHeader() {
        return header;
    }

    public void setHeader(int header) {
        this.header = header;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public String getAsccString() {
        return asccString;
    }

    public void setAsccString(String asccString) {
        this.asccString = asccString;
    }
}
```
then we can decode or encode the protocol like this:
```java
@Test
public void test() throws Exception {
	ProtocolEngine engine = new ProtocolEngine();

	ProtocolTest test = new ProtocolTest();
	test.setHeader(0x2882);
	test.setVersion(1);
	test.setContentLength(12);
	test.setAsccString("HELLO, WORLD");

	// we encode the ProtocolTest object to byte buffer.
	byte[] buf = engine.encode(test);
	System.out.println(HexStringUtil.toHexString(buf));
	// print msg:
	// 288201000C48454C4C4F2C20574F524C44

	// ok, now we decode the ProtocolTest object from byte[] array.
	ProtocolTest testDecode = engine.decode(buf, ProtocolTest.class);

	System.out.println(testDecode.toString());
	// print msg:
	// ProtocolTest{header=10370, version=1, contentLength=12, asccString='HELLO, WORLD'}
	// wow... is it work?
}
```
For more usage examples check the tests.
Please check the documention. There are lots of cool things you should know, including information about connection pooling.
## Documention
- [More docs](https://github.com/ioprotocol/engine/blob/master/documention/howtouse.md)
- [Changelog](https://github.com/ioprotocol/engine/blob/master/documention/changelog.md)
- [Important note](https://github.com/ioprotocol/engine/blob/master/documention/important.md)

## Submit BUG
While you find bug or have good idea, welcome email to me.

## Author info
Email:xushy@yahoo.com<br>
QQ:845158228
