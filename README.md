# Getting started
Make sure gradle is installed.
To run it simply try
`gradle jfxRun`

# Gradle tasks
gradle jfxJar - Create executable JavaFX-jar
gradle jfxNative - Create native JavaFX-bundle (will run jfxJar first)
gradle jfxRun - Create the JavaFX-jar and runs it like you would do using java -jar my-project-jfx.jar, adjustable using runJavaParameter/runAppParameter-parameter
gradle jfxGenerateKeyStore - Create a Java keystore
gradle jfxListBundlers - List all possible bundlers available on this system, use '--info' parameter for detailed information