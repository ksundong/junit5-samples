//usr/bin/env jshell --show-version --execution local "$0" "$@"; exit $?

/open BUILDING

//
// download main and test dependencies
//
String platformVersion = "1.1.0"
String jupiterVersion = "5.1.0"
String vintageVersion = "5.1.0"
//get("lib", "org.junit.platform", "junit-platform-commons", platformVersion)
//get("lib", "org.junit.platform", "junit-platform-console", platformVersion)
//get("lib", "org.junit.platform", "junit-platform-engine", platformVersion)
//get("lib", "org.junit.platform", "junit-platform-launcher", platformVersion)
//get("lib", "org.junit.jupiter", "junit-jupiter-api", jupiterVersion)
//get("lib", "org.junit.jupiter", "junit-jupiter-engine", jupiterVersion)
get("lib", "org.junit.vintage", "junit-vintage-engine", vintageVersion)
get("lib", "junit", "junit", "4.12")
get("lib", "org.hamcrest", "hamcrest-core", "1.3")
get("lib", "org.apiguardian", "apiguardian-api", "1.0.0")
get("lib", "org.opentest4j", "opentest4j", "1.0.0")
get("lib", "net.jqwik", "jqwik", "0.8.5")

String repo = "https://jitpack.io/com/github/junit-team/junit5"
String version = "experiments~parallel-execution-r5.1.0-M2-g1730fe7-29"
URI jitpack(String artifact) { return URI.create(String.join("/", repo, artifact, version, artifact + "-" + version + ".jar" )); }
get("lib", jitpack("junit-jupiter-api"))
get("lib", jitpack("junit-jupiter-engine"))
get("lib", jitpack("junit-platform-console"))
get("lib", jitpack("junit-platform-commons"))
get("lib", jitpack("junit-platform-engine"))
get("lib", jitpack("junit-platform-launcher"))

// create target directories
Files.createDirectories(Paths.get("bin/main-jars"))

//
// compile and package tool module
//
run("javac", "-d", "bin/main", "--module-source-path", "src/main", "--module", "com.example.tool")
run("jar", "--create", "--file", "bin/main-jars/com.example.tool.jar", "-C", "bin/main/com.example.tool", ".")

//
// compile and package application module
//
run("javac", "-d", "bin/main", "--module-source-path", "src/main", "--module", "com.example.application")
run("jar", "--create", "--file", "bin/main-jars/com.example.application.jar", "--main-class", "com.example.application.Main", "-C", "bin/main/com.example.application", ".")
run("jar", "--describe-module", "--file", "bin/main-jars/com.example.application.jar")

//
// compile and package "ice.cream" module
//
run("javac", "-d", "bin/main", "--module-path", "lib", "--module-source-path", "src/main", "--module", "ice.cream")
run("jar", "--create", "--file", "bin/main-jars/ice.cream.jar", "--module-version", "47.11", "-C", "bin/main/ice.cream", ".")
run("jar", "--describe-module", "--file", "bin/main-jars/ice.cream.jar")

//
// run application
//
exe("java", "--module-path", "bin/main-jars", "--module", "com.example.application", "3", "4")

/exit
