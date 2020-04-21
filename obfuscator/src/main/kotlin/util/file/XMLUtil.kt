package util.file

import cipher.CipherHelper
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

object XMLUtil {
    fun obfuscateManifest(xml: File) {
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
}