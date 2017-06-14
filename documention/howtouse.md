# ProtocolEngine
ProtocolEngine support five type of columns, Number,Decimal,Element,DateTime and AsciiString, Also ProtocolEngine
support  combination and nesting with these base types.This documention is consists of the following parts:
* Number
* Decimal
* AsciiString
* Element
* DateTime
## Number
The number annotation can work on bit,byte,short,int,long and the java wrapped types(Byte,Short,Integer,Long),Now let's begin with simple example to understand what this annotation can do.
* Bit or Bits
```java
+------------------------------------+--------------------------+-------------+-------------------+
|                Byte1               |              Byte2       |     Byte3   |         Byte4     |
| Bit0 | Bit1~Bit2 | Bit3~Bit5 | Bit6~Bit9 | Bit10~Bit14 | Bit15~Bit20 | Bit21~Bit27 | Bit28~Bit31|
| Col1 |   Col2    |   Col3    |  Col4     |    Col5     |    Col6     |   Col7      |  Col8      |
+------+-----------+-----------+-----------+-------------+-------------+-------------+------------+
```
