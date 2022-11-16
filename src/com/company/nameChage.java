package com.company;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import java.io.File;

public class nameChage{
        public static void main(String[] args){
            File file = new File("D:\\oriXMLs");
            File[] filelist = file.listFiles();

            for(int tt = 0;tt < filelist.length;tt++) {
                File file1 = filelist[tt];
                String filename = file1.getAbsolutePath();
                if(filename.indexOf(".")>=0)
                {
                    String name = filename.substring(filename.lastIndexOf("\\")+1,filename.indexOf("."));
                    filename = "D:\\oriPDFs\\" + name + ".pdf";
                }
                File fileName = new File(filename);
                String title;
                String s = ":\\\\/.&*(()/*`~?<|{:。}>-,';][=-!#$%^&*+@\\";

            try {
                SAXReader reader = new SAXReader();
                org.dom4j.Document document = reader.read(file1);
                Element root = document.getRootElement();
                title = root.element("teiHeader").element("fileDesc").element("titleStmt").elementTextTrim("title");
                title = title.replaceAll("[\\pP\\p{Punct}]", "");
                fileName.renameTo(new File("D:\\oriPDFs\\" + title + ".pdf"));
            }
            catch (Exception e){
                e.printStackTrace();
            }
            }
        }
    }