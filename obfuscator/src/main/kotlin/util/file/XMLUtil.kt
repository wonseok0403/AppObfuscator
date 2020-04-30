package util.file

import org.w3c.dom.Attr
import org.w3c.dom.NamedNodeMap
import org.w3c.dom.Node
import util.cipher.CipherHelper
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult


object XMLUtil {
    fun obfuscateActivityName(xml: File) {
        val documentBuilder = DocumentBuilderFactory.newDefaultInstance().newDocumentBuilder()
        val document = documentBuilder.parse(xml)
        val activityList = document.getElementsByTagName("activity")

        for(i in 0 until activityList.length){
            val activity = activityList.item(i)
            var nameTag = activity.attributes.getNamedItem("android:name")
            nameTag.textContent = CipherHelper.SHA256(nameTag.textContent)
        }

        val transformer = TransformerFactory.newInstance().newTransformer()
        val domSource = DOMSource(document)
        val streamResult = StreamResult(File(xml.absolutePath));
        transformer.transform(domSource, streamResult);
    }

    fun obfuscateApplicationName(xml: File) {
        injectApplicationName(xml)

        val documentBuilder = DocumentBuilderFactory.newDefaultInstance().newDocumentBuilder()
        val document = documentBuilder.parse(xml)
        val activityList = document.getElementsByTagName("application")

        for(i in 0 until activityList.length){
            val activity = activityList.item(i)
            var nameTag = activity.attributes.getNamedItem("android:name")
            nameTag.textContent = CipherHelper.SHA256(nameTag.textContent)
        }

        val transformer = TransformerFactory.newInstance().newTransformer()
        val domSource = DOMSource(document)
        val streamResult = StreamResult(File(xml.absolutePath));
        transformer.transform(domSource, streamResult);
    }

    fun injectApplicationName(xml: File) {
        val documentBuilder = DocumentBuilderFactory.newDefaultInstance().newDocumentBuilder()
        val document = documentBuilder.parse(xml)
        val activityList = document.getElementsByTagName("application")

        for(i in 0 until activityList.length){
            val activity = activityList.item(i)
            var nameTag = activity.attributes.getNamedItem("android:name")
            if(nameTag == null){
                val attr: Attr = document.createAttribute("android:name")
                attr.nodeValue = "MyApplication"
                activity.attributes.setNamedItem(attr)
            }else{
                nameTag.textContent = "MyApplication"
            }
        }

        val transformer = TransformerFactory.newInstance().newTransformer()
        val domSource = DOMSource(document)
        val streamResult = StreamResult(File(xml.absolutePath));
        transformer.transform(domSource, streamResult);
    }

    fun getPackageName(xml: File) : String?{
        val documentBuilder = DocumentBuilderFactory.newDefaultInstance().newDocumentBuilder()
        val document = documentBuilder.parse(xml)
        val manifestList = document.getElementsByTagName("manifest")

        var packageName : String? = null
        for(i in 0 until manifestList.length){
            val activity = manifestList.item(i)
            packageName = activity.attributes.getNamedItem("package").nodeValue.toString()
        }
        return packageName
    }
}