package util

import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.VariableDeclarator

class VariableUtil(private var cu :CompilationUnit) {
    fun getVariable(): List<VariableDeclarator> {
        val list = mutableListOf<VariableDeclarator>()
        for (type in cu.types) {
            for (iField in type.fields) {
                for (value in iField.variables) {
                    list.add(value)
                }
            }
        }
        return list
    }

    fun modifyValue(varName: String, changeValue: String) {
        looper(varName) {it.setInitializer(changeValue)}
    }

    private fun looper(varName: String, block:((VariableDeclarator)->Unit)){
        for (type in cu.types) {
            for (iField in type.fields) {
                for (value in iField.variables) {
                    if (value.name.identifier == varName) {
                        block(value)
                    }
                }
            }
        }
    }

}