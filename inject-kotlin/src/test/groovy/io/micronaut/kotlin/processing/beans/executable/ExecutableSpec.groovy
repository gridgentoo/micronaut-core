/*
 * Copyright 2017-2019 original authors
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
package io.micronaut.kotlin.processing.beans.executable

import io.micronaut.context.ApplicationContext
import io.micronaut.context.DefaultApplicationContext
import io.micronaut.core.annotation.AnnotationUtil
import io.micronaut.inject.BeanDefinition
import io.micronaut.inject.ExecutableMethod
import io.micronaut.inject.ExecutionHandle
import io.micronaut.inject.MethodExecutionHandle
import spock.lang.Specification

import static io.micronaut.kotlin.processing.KotlinCompiler.*

class ExecutableSpec extends Specification {

    void "test executable compile spec"() {
        given:"A bean that defines no explicit scope"
        when:
        BeanDefinition beanDefinition = buildBeanDefinition('test.MyBean', '''
package test

import io.micronaut.context.annotation.Executable

@Executable
class MyBean {

    fun methodOne(@jakarta.inject.Named("foo") one: String): String {
        return "good"
    }
    
    fun methodTwo(one: String, two: String): String {
        return "good"
    }
    
    fun methodZero(): String {
        return "good"
    }
}


''')
        then:"the default scope is singleton"
        beanDefinition.executableMethods.size() == 3
        beanDefinition.executableMethods[0].methodName == 'methodOne'
        beanDefinition.executableMethods[0].getArguments()[0].getAnnotationMetadata().stringValue(AnnotationUtil.NAMED).get() == 'foo'
    }

    void "test executable metadata"() {
        given:
        ApplicationContext applicationContext = new DefaultApplicationContext("test").start()

        when:
        Optional<MethodExecutionHandle> method = applicationContext.findExecutionHandle(BookController, "show", Long)
        ExecutableMethod executableMethod = applicationContext.findBeanDefinition(BookController).get().findMethod("show", Long).get()

        then:
        method.isPresent()

        when:
        MethodExecutionHandle executionHandle = method.get()

        then:
        executionHandle.returnType.type == String
        executionHandle.invoke(1L) == "1 - The Stand"

        when:
        executionHandle.invoke("bad")

        then:
        def e = thrown(IllegalArgumentException)
        e.message == 'Invalid type [java.lang.String] for argument [Long id] of method: String show(Long id)'

    }

    void "test executable responses"() {
        given:
        ApplicationContext applicationContext = new DefaultApplicationContext("test").start()

        expect:
        applicationContext.findExecutionHandle(BookController, methodName, argTypes as Class[]).isPresent()
        ExecutionHandle method = applicationContext.findExecutionHandle(BookController, methodName, argTypes as Class[]).get()
        method.invoke(args as Object[]) == result


        where:
        methodName            | argTypes         | args             | result
        "show"                | [Long]           | [1L]             | "1 - The Stand"
        "showArray"           | [Long[].class]   | [[1L] as Long[]] | "1 - The Stand"
        "showPrimitive"       | [long.class]     | [1L as long]     | "1 - The Stand"
        "showPrimitiveArray"  | [long[].class]   | [[1L] as long[]] | "1 - The Stand"
        "showVoidReturn"      | [Iterable.class] | [['test']]       | null
        "showPrimitiveReturn" | [int[].class]    | [[1] as int[]]   | 1
    }
}
