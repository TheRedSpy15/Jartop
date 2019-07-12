/**
 * Scaffolding file used to store all the setups needed to run 
 * tests automatically generated by EvoSuite
 * Sat Apr 07 06:12:52 GMT 2018
 */


import org.evosuite.runtime.annotation.EvoSuiteClassExclude;
import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.After;
import org.junit.AfterClass;
import org.evosuite.runtime.sandbox.Sandbox;
import org.evosuite.runtime.sandbox.Sandbox.SandboxMode;

@EvoSuiteClassExclude
public class PasswordManager_ESTest_scaffolding {

  @org.junit.Rule 
  public org.evosuite.runtime.vnet.NonFunctionalRequirementRule nfr = new org.evosuite.runtime.vnet.NonFunctionalRequirementRule();

  private static final java.util.Properties defaultProperties = (java.util.Properties) java.lang.System.getProperties().clone(); 

  private org.evosuite.runtime.thread.ThreadStopper threadStopper =  new org.evosuite.runtime.thread.ThreadStopper (org.evosuite.runtime.thread.KillSwitchHandler.getInstance(), 3000);


  @BeforeClass 
  public static void initEvoSuiteFramework() { 
    org.evosuite.runtime.RuntimeSettings.className = "PasswordManager"; 
    org.evosuite.runtime.GuiSupport.initialize(); 
    org.evosuite.runtime.RuntimeSettings.maxNumberOfThreads = 100; 
    org.evosuite.runtime.RuntimeSettings.maxNumberOfIterationsPerLoop = 10000; 
    org.evosuite.runtime.RuntimeSettings.mockSystemIn = true; 
    org.evosuite.runtime.RuntimeSettings.sandboxMode = org.evosuite.runtime.sandbox.Sandbox.SandboxMode.RECOMMENDED; 
    org.evosuite.runtime.sandbox.Sandbox.initializeSecurityManagerForSUT(); 
    org.evosuite.runtime.classhandling.JDKClassResetter.init();
    setSystemProperties();
    initializeClasses();
    org.evosuite.runtime.Runtime.getInstance().resetRuntime(); 
  } 

  @AfterClass 
  public static void clearEvoSuiteFramework(){ 
    Sandbox.resetDefaultSecurityManager(); 
    java.lang.System.setProperties((java.util.Properties) defaultProperties.clone()); 
  } 

  @Before 
  public void initTestCase(){ 
    threadStopper.storeCurrentThreads();
    threadStopper.startRecordingTime();
    org.evosuite.runtime.jvm.ShutdownHookHandler.getInstance().initHandler(); 
    org.evosuite.runtime.sandbox.Sandbox.goingToExecuteSUTCode(); 
    setSystemProperties(); 
    org.evosuite.runtime.GuiSupport.setHeadless(); 
    org.evosuite.runtime.Runtime.getInstance().resetRuntime(); 
    org.evosuite.runtime.agent.InstrumentingAgent.activate(); 
  } 

  @After 
  public void doneWithTestCase(){ 
    threadStopper.killAndJoinClientThreads();
    org.evosuite.runtime.jvm.ShutdownHookHandler.getInstance().safeExecuteAddedHooks(); 
    org.evosuite.runtime.classhandling.JDKClassResetter.reset(); 
    resetClasses(); 
    org.evosuite.runtime.sandbox.Sandbox.doneWithExecutingSUTCode(); 
    org.evosuite.runtime.agent.InstrumentingAgent.deactivate(); 
    org.evosuite.runtime.GuiSupport.restoreHeadlessMode(); 
  } 

  public static void setSystemProperties() {
 
    java.lang.System.setProperties((java.util.Properties) defaultProperties.clone()); 
    java.lang.System.setProperty("file.encoding", "Cp1252"); 
    java.lang.System.setProperty("java.awt.headless", "true"); 
    java.lang.System.setProperty("java.io.tmpdir", "C:\\Users\\Hunter\\AppData\\Local\\Temp\\"); 
    java.lang.System.setProperty("user.country", "US"); 
    java.lang.System.setProperty("user.dir", "C:\\Users\\Hunter\\IdeaProjects\\Jartop\\src\\main"); 
    java.lang.System.setProperty("user.home", "C:\\Users\\Hunter"); 
    java.lang.System.setProperty("user.language", "en"); 
    java.lang.System.setProperty("user.name", "Hunter"); 
    java.lang.System.setProperty("user.timezone", "America/New_York"); 
  }

  private static void initializeClasses() {
    org.evosuite.runtime.classhandling.ClassStateSupport.initializeClasses(PasswordManager_ESTest_scaffolding.class.getClassLoader() ,
      "com.google.common.hash.Hashing$ChecksumType$1",
      "com.google.common.hash.HashCode$LongHashCode",
      "com.google.common.hash.Hashing$ChecksumType",
      "User",
      "com.google.common.base.Supplier",
      "com.google.common.hash.Hashing",
      "com.google.common.hash.AbstractCompositeHashFunction",
      "com.google.common.hash.AbstractHashFunction",
      "com.google.errorprone.annotations.Immutable",
      "com.google.common.hash.PrimitiveSink",
      "com.google.common.hash.Hasher",
      "Core",
      "com.google.common.base.Charsets",
      "com.google.common.hash.AbstractByteHasher",
      "com.google.common.hash.Hashing$LinearCongruentialGenerator",
      "com.google.common.hash.HashCode",
      "UserAccountSecurity",
      "PasswordManager",
      "com.google.common.hash.HashCode$BytesHashCode",
      "com.google.common.hash.HashCode$IntHashCode",
      "com.google.common.hash.Hashing$ConcatenatedHashFunction",
      "com.google.common.hash.Funnel",
      "com.google.common.hash.ChecksumHashFunction$ChecksumHasher",
      "com.google.common.hash.AbstractHasher",
      "Desktop",
      "com.google.common.hash.ImmutableSupplier",
      "com.google.common.base.Preconditions",
      "com.google.common.hash.HashFunction",
      "com.google.common.hash.ChecksumHashFunction",
      "com.google.common.hash.Hashing$ChecksumType$2"
    );
  } 

  private static void resetClasses() {
    org.evosuite.runtime.classhandling.ClassResetter.getInstance().setClassLoader(PasswordManager_ESTest_scaffolding.class.getClassLoader()); 

    org.evosuite.runtime.classhandling.ClassStateSupport.resetClasses(
      "PasswordManager",
      "Desktop",
      "com.google.common.hash.AbstractHashFunction",
      "com.google.common.hash.ChecksumHashFunction",
      "com.google.common.base.Preconditions",
      "com.google.common.hash.Hashing$ChecksumType",
      "com.google.common.base.Charsets",
      "com.google.common.hash.Hashing",
      "com.google.common.hash.AbstractHasher",
      "com.google.common.hash.AbstractByteHasher",
      "com.google.common.hash.ChecksumHashFunction$ChecksumHasher",
      "com.google.common.hash.HashCode",
      "com.google.common.hash.HashCode$IntHashCode",
      "User",
      "UserAccountSecurity",
      "Core"
    );
  }
}
