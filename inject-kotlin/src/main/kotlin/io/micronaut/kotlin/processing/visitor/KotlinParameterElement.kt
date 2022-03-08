package io.micronaut.kotlin.processing.visitor

import com.google.devtools.ksp.symbol.KSValueParameter
import io.micronaut.core.annotation.AnnotationMetadata
import io.micronaut.inject.ast.ClassElement
import io.micronaut.inject.ast.ParameterElement

class KotlinParameterElement(
    private val genericClassElement: ClassElement,
    private val classElement: ClassElement,
    private val parameter: KSValueParameter,
    annotationMetadata: AnnotationMetadata,
    visitorContext: KotlinVisitorContext
) : AbstractKotlinElement<KSValueParameter>(parameter, annotationMetadata, visitorContext), ParameterElement {

    override fun getName(): String {
        return parameter.name!!.asString()
    }

    override fun getType(): ClassElement = classElement

    override fun getGenericType(): ClassElement = genericClassElement

    override fun getArrayDimensions(): Int = classElement.arrayDimensions
}
