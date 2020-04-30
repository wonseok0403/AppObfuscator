package modifier.obfuscator

import com.github.javaparser.ast.expr.FieldAccessExpr
import com.github.javaparser.ast.expr.Name
import modifier.AbstractModifier
import util.cipher.CipherHelper.SHA256
import util.file.CompilationUtil.saveCompilationUnit
import util.file.FileUtil
import util.file.FileUtil.changeFileName
import util.file.XMLUtil
import util.syntax.*
import java.io.File

class Obfuscator private constructor() : AbstractModifier() {
    companion object {
        fun build(targetAppName: String): Obfuscator {
            return Obfuscator().apply { this.targetAppName = targetAppName }
        }
    }

    private class XMLObfuscator(targetAppName: String) : AbstractModifier() {
        val XMLFile by lazy { FileUtil.getManifestXML(mainFolder) }

        init {
            this.targetAppName = targetAppName
        }

        override fun run() {
            applyToActivity()
            applyToApplication()
        }

        private fun applyToActivity() {
            XMLUtil.obfuscateActivityName(XMLFile)
        }

        private fun applyToApplication() {
            XMLUtil.obfuscateApplicationName(XMLFile)
        }
    }

    private class DeclarationObfuscator(targetAppName: String) : AbstractModifier() {
        init {
            this.targetAppName = targetAppName
        }

        override fun run() {
            javaFiles.forEach {
                applyToVariable(it)
                applyToParameter(it)
                applyToMethod(it)
                applyToClass(it)
            }
        }

        private fun applyToClass(file: File) {
            val cu = parse(file)
            val util = ClassUtil(cu)
            val classNameList = mutableListOf<String>()
            val obfuscatedClassNameList = mutableListOf<String>()

            util.get().forEach {
                classNameList.add(it.name.toString())
            }

            classNameList.forEach { default ->
                SHA256(default)?.let {
                    obfuscatedClassNameList.add(it)
                    util.setName(default, it)
                }
            }

            saveCompilationUnit(file, cu)

            changeFileName(file, obfuscatedClassNameList[0])
        }

        private fun applyToVariable(file: File) {
            println("----$file----")
            val cu = parse(file)
            VariableDeclUtil(cu).also { util ->
                util.get().forEach {
                    val name = it.name.toString()
                    println(name)
                    with(SHA256(name), {
                        util.setName(name, this!!)
                    })
                }
            }

            saveCompilationUnit(file, cu)
        }

        private fun applyToParameter(file: File) {
            val cu = parse(file)
            ParameterUtil(cu).also { util ->
                util.get().forEach {
                    val name = it.name.toString()
                    if (name[0].isLowerCase()) {
                        println(name)
                        with(SHA256(name), {
                            util.setName(name, this!!)
                        })
                    }
                }
            }
            saveCompilationUnit(file, cu)
        }

        private fun applyToMethod(file: File) {
            val cu = parse(file)
            MethodDeclUtil(cu).also { util ->
                util.get().filter { it.annotations.size == 0 }.forEach {
                    val name = it.name.toString()
                    with(SHA256(name), {
                        util.setName(name, this!!)
                    })
                }
            }
            saveCompilationUnit(file, cu)
        }
    }

    private class CallingObfuscator(targetAppName: String, private val userDefCode: List<String>) : AbstractModifier() {
        init {
            this.targetAppName = targetAppName
        }

        override fun run() {
            javaFiles.forEach {
                applyToInstanceVariable(it)
                applyToVariable(it)
                applyToMethodCall(it)
            }
        }

        private fun applyToVariable(file: File) {
            val cu = parse(file)
            NameUtil(cu).also { util ->
                util.get().forEach {
                    val name = it.name.toString()
                    if (userDefCode.contains(name) || name[0].isLowerCase()) {
                        with(SHA256(name), {
                            util.setName(name, this!!)
                        })
                    }
                }
            }

            saveCompilationUnit(file, cu)
        }

        private fun applyToInstanceVariable(file: File){
            val cu = parse(file)
            FieldAccessUtil(cu).also{ util ->
                util.get().filter{"R" !in it.toString()}.forEach {
                    val name = it.name.toString()
                    with(SHA256(name), {
                        util.setName(name, this!!)
                    })
                }
            }

            saveCompilationUnit(file, cu)
        }

        private fun applyToMethodCall(file: File){
            val cu = parse(file)

            MethodCallUtil(cu).also{ util ->
                util.get().forEach {
                    val name = it.name.toString()
                    if (userDefCode.contains(name)) {
                        with(SHA256(name), {
                            util.setName(name, this!!)
                        })
                    }
                }
            }

            saveCompilationUnit(file, cu)
        }
    }

    private fun getUserDefCode(): List<String> {
        val userDefine = mutableListOf<String>()

        javaFiles.forEach {
            val cu = parse(it)
            ClassUtil(cu).get().forEach {
                userDefine.add(it.name.toString())
            }

            MethodDeclUtil(cu).get().filter { it.annotations.size == 0 }.forEach {
                userDefine.add(it.name.toString())
            }
        }
        return userDefine
    }

    override fun run() {
        val userDefCode = getUserDefCode()
        XMLObfuscator(targetAppName).run()
        CallingObfuscator(targetAppName, userDefCode).run()
        DeclarationObfuscator(targetAppName).run()
    }
}

/**
 * FieldAccess -> 클래스 인스턴스 변수 접근 난독화
 * Name -> 변수 사용 난독화
 * Parameter -> 파라미터 선언 난독화
 * Class -> 클래스 선언 난독화
 * VariableDecl -> 변수 선언 난독화
 * MethodDecl -> 메소드 선언 난독화
 * MethodCall -> 메소드 사용 난독화
 **/