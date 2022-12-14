/*
 * Copyright 2017-2018 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.micronaut.inject.visitor;

import io.micronaut.core.annotation.Experimental;
import io.micronaut.core.convert.value.MutableConvertibleValues;
import io.micronaut.inject.ast.ClassElement;
import io.micronaut.inject.ast.Element;
import io.micronaut.inject.writer.GeneratedFile;

import java.util.Optional;

/**
 * Provides a way for {@link TypeElementVisitor} classes to log messages during compilation and fail compilation.
 *
 * @author James Kleeh
 * @author Graeme Rocher
 * @since 1.0
 */
public interface VisitorContext extends MutableConvertibleValues<Object> {

    /**
     * Allows printing informational messages.
     *
     * @param message The message
     * @param element The element
     */
    void info(String message, Element element);

    /**
     * Allows printing informational messages.
     *
     * @param message The message
     */
    void info(String message);

    /**
     * Allows failing compilation for a given element with the given message.
     *
     * @param message The message
     * @param element The element
     */
    void fail(String message, Element element);

    /**
     * Allows printing a warning for the given message and element.
     *
     * @param message The message
     * @param element The element
     */
    void warn(String message, Element element);

    /**
     * Visit a file within the META-INF directory.
     *
     * @param path The path to the file
     * @return An optional file it was possible to create it
     */
    @Experimental
    Optional<GeneratedFile> visitMetaInfFile(String path);


    /**
     * Visit a file that will be located within the generated source directory.
     *
     * @param path The path to the file
     * @return An optional file it was possible to create it
     */
    @Experimental
    Optional<GeneratedFile> visitGeneratedFile(String path);

    /**
     * This method will lookup another class element by name. If it cannot be found an empty optional will be returned.
     *
     * @param name The name
     * @return The class element
     */
    default Optional<ClassElement> getClassElement(String name) {
        return Optional.empty();
    }

    /**
     * This method will lookup another class element by name. If it cannot be found an empty optional will be returned.
     *
     * @param type The name
     * @return The class element
     */
    default Optional<ClassElement> getClassElement(Class<?> type) {
        if (type != null) {
            return getClassElement(type.getName());
        }
        return Optional.empty();
    }

}
