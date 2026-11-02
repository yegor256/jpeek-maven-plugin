/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 Yegor Bugayenko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.jpeek.plugin;

import com.yegor256.farea.Farea;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests for {@link JpeekMojo}.
 *
 * @since 1.0
 */
final class JpeekMojoTest {

    @Test
    void generatesIndexReport(@TempDir final Path dir) throws IOException {
        new Farea(dir).together(
            f -> {
                f.clean();
                f.files().file("src/main/java/org/example/Hello.java").write(
                    String.join(
                        "\n",
                        "package org.example;",
                        "public class Hello {",
                        "    public void hi() {",
                        "        System.out.println(\"ok\");",
                        "    }",
                        "}"
                    ).getBytes(StandardCharsets.UTF_8)
                );
                f.build().plugins().appendItself().execution()
                    .goals("analyze").configuration().set("cohesionRate", 0);
                f.exec("verify");
                MatcherAssert.assertThat(
                    "The analyze goal must generate an index report",
                    f.files().file("target/jpeek/index.html").exists(),
                    Matchers.is(true)
                );
            }
        );
    }
}
