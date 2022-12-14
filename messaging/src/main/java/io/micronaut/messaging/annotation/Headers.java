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

package io.micronaut.messaging.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This lets you declare several headers for a client class and have them always included.
 * Example usage:
 *
 * <pre><code>
 *{@literal @}Headers({
 *     {@literal @}Header(name="Content-type",value="application/octet-stream"),
 *     {@literal @}Header(name="Content-length",value="2048")
 * })
 * </code></pre>
 *
 * @author graemerocher
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Headers {

    /***
     * This allows you to have multiple @Header's set at the class level with Java and Groovy.
     * Example usage:
     * <pre><code>
     *{@literal @}Headers({
     *     {@literal @}Header(name="Content-type",value="application/octet-stream"),
     *     {@literal @}Header(name="Content-length",value="2048")
     * })
     * </code></pre>
     * @return The headers
     */
    Header[] value() default {};
}
