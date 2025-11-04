package org.jpeek.plugin;

import com.yegor256.farea.Farea;
import com.yegor256.farea.RequisiteMatcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

class JpeekAnalyzeMojoTest {

    @Test
    void testJpeekAnalyzeGoal(@TempDir Path dir) throws Exception {
        new Farea(dir).together(f -> {
            f.files()
                    .file("pom.xml")
                    .write(readResource("pom.xml"));
            f.files()
                    .file("src/main/java/org/example/Hello.java")
                    .write(readResource("Hello.java"));

            f.exec("verify");

            MatcherAssert.assertThat(
                    "analyze goal must completed successfully",
                    f.files().log(),
                    RequisiteMatcher.SUCCESS
            );
            Path report = dir.resolve("target/jpeek/index.html");
            MatcherAssert.assertThat(
                    "report must be created",
                    Files.exists(report),
                    Matchers.is(true)
            );
        });
    }

    private byte[] readResource(String name) {
        try {
            return Files.readAllBytes(
                    Paths.get(
                            Objects.requireNonNull(
                                    JpeekAnalyzeMojoTest.class
                                            .getClassLoader()
                                            .getResource(name)
                            ).toURI()
                    )
            );
        } catch (Exception e) {
            throw new IllegalStateException("Can't read resource: " + name, e);
        }
    }
}
