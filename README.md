# java-memoryleak-demo
It simulates a few of memory leak scenarios:
- Allocate random data into a collection and don't release it
- Open streams and don't close them 

## Memory allocation
You can simulate memory consumption in 1 MB units.
- Memory is allocated in 1024 * 1KB units as random Strings
- Strings are added to a static collection

## Configuration
- specify memory with these JVM parameters: **-Xms64m -Xmx100m**
- activate GC logging with this JVM parameters: **-verbose:gc**
  