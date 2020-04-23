package obfuscator

import cipher.CipherHelper.SHA256
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.Parameter
import com.github.javaparser.ast.expr.*
import util.file.CompilationUtil.saveCompilationUnit
import util.file.FileUtil
import util.file.FileUtil.changeFileName
import util.file.XMLUtil
import util.syntax.ClassUtil
import util.syntax.NameUtil
import util.syntax.ParameterUtil
import util.syntax.VariableUtil
import java.io.File

class Obfuscator private constructor() : AbstractObfuscator() {
    companion object {
        fun build(targetAppName: String): Obfuscator {
            return Obfuscator().apply { this.targetAppName = targetAppName }
        }
    }

    private class DeclarationObfuscator(targetAppName: String) : AbstractObfuscator() {
        init {
            this.targetAppName = targetAppName
        }

        override fun run() {
            applyToXML()
            javaFiles.forEach {
                applyToVariable(it)
                //applyToMethod(it)
                applyToClass(it)
            }
        }

        private fun applyToXML(){
            XMLUtil.obfuscateManifest(FileUtil.getManifestXML(folder))
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
            val cu = parse(file)
            VariableUtil(cu).also { util ->
                util.get().forEach {
                    val name = it.name.toString()
                    println(name)
                    with(SHA256(name), {
                        util.setName(name, this!!)
                    })
                }
            }
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
            NameUtil(cu).also { util ->
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
        }
    }

    private class CallingObfuscator(private val cu: CompilationUnit) : AbstractObfuscator() {
        override fun run() {
            applyToVariable()
            applyToMethod()
        }

        private fun applyToVariable() {
//            val util = AssignUtil(cu)
//            util.get().forEach{
//                val name = it.target.toString()
//                println(name)
//                with(SHA256(name),{
//                    //util.setName(name, this!!)
//                })
//            }
        }

        private fun applyToMethod() {

        }
    }

    override fun run() {
        DeclarationObfuscator(targetAppName).run()
    }
}

