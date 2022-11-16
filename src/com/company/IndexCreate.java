package com.company;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.lucene.document.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class IndexCreate {
    public static void main(String[] args) throws IOException {

        //索引存放位置
        Directory directory = FSDirectory.open(new File("D:\\oriTxt"));

        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);

        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36,analyzer);

        IndexWriter indexWriter = new IndexWriter(directory,config);

        indexWriter.deleteAll();

        //XML文件所在路径
        File file = new File("D:\\oriXMLs");
        File[] filelist = file.listFiles();

        //遍历XML文件并建立索引
        for(int tt = 0;tt < filelist.length;tt++) {
            Document doc = new Document();
            String title;
            File file1 = filelist[tt];

            try {
                SAXReader reader = new SAXReader();
                org.dom4j.Document document = reader.read(file1);
                Element root = document.getRootElement();


                //为title做索引(OK)
                title = root.element("teiHeader").element("fileDesc").element("titleStmt").elementTextTrim("title");
                String s = ":\\\\/.&*(()/*`~?<|{:。}>-,';][=-!#$%^&*+@\\";
                title = title.replaceAll("[\\pP\\p{Punct}]", "");

                doc.add(new Field("title",title,Field.Store.YES,Field.Index.ANALYZED_NO_NORMS));

                //为fulltext做索引(OK)
                List<Element> fulltext = root.element("text").element("body").elements("div");
                for (Element ft : fulltext) {
                    List<Element> ps = ft.elements();
                    for (Element ele : ps) {
                        doc.add(new Field("fulltext",ele.getText(),Field.Store.YES,Field.Index.ANALYZED_NO_NORMS));
                    }
                }

                //为keyword做索引(OK)
                if (root.element("teiHeader").element("profileDesc").element("textClass") != null) {
                    List<Element> keyword = root.element("teiHeader").element("profileDesc").element("textClass").element("keywords").elements();
                    for (Element kw : keyword) {
                        doc.add(new Field("keyword",kw.getText(),Field.Store.YES,Field.Index.ANALYZED_NO_NORMS));
                    }
                }

                //为date做索引(OK)
                if (root.element("teiHeader").element("fileDesc").element("sourceDesc").element("biblStruct").element("monogr").element("imprint")!= null) {
                    List<Element> date = root.element("teiHeader").element("fileDesc").element("sourceDesc").element("biblStruct").element("monogr").element("imprint").elements();
                    for (Element dt : date) {
                        doc.add(new Field("date", dt.getText(), Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
                    }
                }

                //为abstract做索引(OK)
                if (root.element("teiHeader").element("profileDesc").element("abstract")!=null) {
                    List<Element> abs = root.element("teiHeader").element("profileDesc").element("abstract").elements();
                    for (Element ab:abs){
                       doc.add(new Field("abstract", ab.getText(), Field.Store.YES, Field.Index.ANALYZED_NO_NORMS));
                    }
                }

                //为author做索引(OK)
                if (root.element("teiHeader").element("fileDesc").element("sourceDesc").element("biblStruct").element("analytic").element("author") != null) {
                    List<Element> author = root.element("teiHeader").element("fileDesc").element("sourceDesc").element("biblStruct").element("analytic").elements("author");
                    for (Element au:author){
                        if (au.element("persName") != null){
                            String name ="";
                            List<Element> author_name = au.element("persName").elements();
                            for (Element an:author_name){
                                name = name +" "+an.getText();
                            }
                            doc.add(new Field("author",name,Field.Store.YES,Field.Index.ANALYZED_NO_NORMS));
                        }
                    }
               }

                //为affiliation做索引(OK)
                if (root.element("teiHeader").element("fileDesc").element("sourceDesc").element("biblStruct").element("analytic").element("author") != null) {
                    List<Element> author = root.element("teiHeader").element("fileDesc").element("sourceDesc").element("biblStruct").element("analytic").elements("author");
                    for (Element au:author){
                        if (au.element("affiliation") != null){
                            List<Element> affiliation = au.elements("affiliation");
                            for (Element aff:affiliation) {
                                if (aff.element("orgName") != null)
                                    doc.add(new Field("affiliation",aff.element("orgName").getText(), Field.Store.YES, Field.Index.ANALYZED_NO_NORMS));
                            }
                        }
                    }
                }

                //为address做索引(OK)
                if (root.element("teiHeader").element("fileDesc").element("sourceDesc").element("biblStruct").element("analytic").element("author") != null) {
                    List<Element> author = root.element("teiHeader").element("fileDesc").element("sourceDesc").element("biblStruct").element("analytic").elements("author");
                    for (Element au : author) {
                        if (au.element("affiliation") != null) {
                            if (au.element("affiliation").element("address") != null) {
                                if (au.element("affiliation").element("address").elements() != null) {
                                    String Add = "";
                                    List<Element> address = au.element("affiliation").element("address").elements();
                                    for (Element add : address) {
                                        Add = Add+" "+add.getText();
                                    }
                                    doc.add(new Field("address",Add, Field.Store.YES, Field.Index.ANALYZED_NO_NORMS));
                                }
                            }

                        }
                    }
                }

                indexWriter.addDocument(doc);
            }

            catch (Exception e){
                e.printStackTrace();
            }

        }
        indexWriter.close();

    }
}
