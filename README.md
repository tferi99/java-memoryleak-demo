*# java-memoryleak-demo
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
 
***Configuration in IntelliJ Idea:***  
- **app** - only Heap min/max specified; default (parallel) GC

  - **Main class:** org.ftoth.javamemoryleakdemo.JavaMemLeakDemoApp
  
  - **JVM params:** -Xms64m -Xmx1000m
    
- **app-GCverb-G1** - G1 GC; base GC log

  - **Main class:** org.ftoth.javamemoryleakdemo.JavaMemLeakDemoApp
  - **JVM params:** -Xms64m -Xmx1000m -XX:NewSize=16m -XX:MaxNewSize=32m -XX:+UseG1GC -verbose:gc -XX:+PrintStringTableStatistics  -XX:MaxTenuringThreshold=9
  
- **app-GCverbDetail-G1** - G1 GC; detailed GC log

  - **Main class:** org.ftoth.javamemoryleakdemo.JavaMemLeakDemoApp
  - **JVM params:** -Xms64m -Xmx1000m -XX:+UseG1GC -verbose:gc -XX:+PrintGCDetails
  
- **DummyMain** - for printing JVM parameters 

  - **Main class:** org.ftoth.javamemoryleakdemo.DummyMain
  - **JVM params:** -Xms64m -Xmx1000m -XX:NewSize=16m -XX:MaxNewSize=32m -XX:+UseG1GC -XX:+PrintFlagsFinal -XX:+UnlockDiagnosticVMOptions -XX:MaxTenuringThreshold=9
  