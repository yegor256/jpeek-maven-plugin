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

import java.io.File;
import java.io.IOException;

import com.jcabi.xml.*;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.jpeek.App;

/**
 * {@link org.apache.maven.plugin.AbstractMojo} implementation for jPeek.
 *
 * @since 0.1
 * @todo #3:30min Add some tests for this Mojo using AbstractMojoTestCase
 *  from maven-plugin-testing-harness. A good resource for examples is
 *  maven-checkstyle-plugin. It has been verified that it works from the
 *  command line (see README).
 * @todo #3:30min Add support for analyzing classes in the test directory.
 *  This should output an analysis most certainly in a different directory
 *  from the main classes.
 */
@Mojo(name = "analyze", defaultPhase = LifecyclePhase.VERIFY)
public final class JpeekMojo extends AbstractMojo {

    /**
     * Specifies the path where to find classes analyzed by jPeek.
     * @checkstyle MemberNameCheck (3 lines)
     */
    @Parameter(property = "jpeek.input", defaultValue = "${project.build.outputDirectory}")
    private File inputDirectory;

    /**
     * Specifies the path to save the jPeek output.
     * @checkstyle MemberNameCheck (3 lines)
     */
    @Parameter(property = "jpeek.output", defaultValue = "${project.build.directory}/jpeek/")
    private File outputDirectory;

    /**
     * Specifies expected cohesion ration of a project
     */
    @Parameter(property = "jpeek.cohesionRate", defaultValue = "8.0")
    private double cohesionRate;

    /**
     * Skip analyze.
     */
    @Parameter(property = "jpeek.skip", defaultValue = "false")
    private boolean skip;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (!this.skip) {
            try {
                new App(
                    this.inputDirectory.toPath(),
                    this.outputDirectory.toPath()
                ).analyze();

                final XML index = new XMLDocument(
                    new File(String.format("%s\\%s", outputDirectory, "index.xml"))
                );

                final double score = Double.parseDouble(
                    index.xpath("/index/@score").get(0)
                );

                if(score < cohesionRate)
                    throw new MojoFailureException(String.format("Project cohesion rate is less than %.2f", cohesionRate));

            } catch (final IOException ex) {
                throw new MojoExecutionException("Couldn't analyze", ex);
            }
        }
    }
}
