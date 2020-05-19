## How to use?

Add the plugin execution to your `pom.xml`:

```xml
    <plugin>
        <groupId>org.jpeek</groupId>
        <artifactId>jpeek-maven-plugin</artifactId>
        <version>1.0-SNAPSHOT</version>
        <executions>
            <execution>
                <goals>
                    <!-- Bound by default to verify phase -->
                    <goal>analyze</goal>
                </goals>
            </execution>
        </executions>
        <configuration>
            <!-- Those are the default values -->
            <inputDirectory>${project.build.outputDirectory}</inputDirectory>
            <outputDirectory>${project.build.directory}/jpeek/</outputDirectory>
        </configuration>
    </plugin>
```

Or run it from the command-line:

```
mvn org.jpeek:jpeek-maven-plugin:1.0-SNAPSHOT:analyze
```
