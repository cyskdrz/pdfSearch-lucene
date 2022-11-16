package com.company;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;

import static java.lang.System.out;


public class IndexSearch {
    //根据题目查询
    public static void IndexSearch_ForTitle(String keywords) throws Exception{
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
        QueryParser queryParser = new QueryParser(Version.LUCENE_36, "title",analyzer);
        Query query = queryParser.parse(keywords);

        Directory directory = FSDirectory.open(new File("D:\\oriTxt"));
        IndexSearcher indexSearcher = new IndexSearcher(IndexReader.open(directory));
        TopDocs topDocs = indexSearcher.search(query,10);
        out.println("总共匹配多少个：" + topDocs.totalHits);
        //获取查询结果集
        ScoreDoc[] hits = topDocs.scoreDocs;

        int pageSize=5;//每页显示记录数
        int curPage=2;//当前页
        //查询起始记录位置
        int begin = pageSize * (curPage - 1);
        //查询终止记录位置
        int end = Math.min(begin + pageSize, hits.length);
        //遍历结果集
        out.println("满足结果记录条数："+topDocs.totalHits);
        out.println("当前页数:"+curPage);
        for(int i=begin;i<end;i++) {
            int docID = hits[i].doc;
            Document document = indexSearcher.doc(docID);
            out.println("title:"+document.get("title"));
            out.println("\n");
        }

        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (int i = 0;i < scoreDocs.length;i++){
            int docID = scoreDocs[i].doc;
            Document document = indexSearcher.doc(docID);
            out.println("title:" +document.get("title"));

        }
        indexSearcher.close();
    }

    //返回字符串
    public static String IndexSearch_ForTitleReturnString(String keywords) throws Exception{
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
        QueryParser queryParser = new QueryParser(Version.LUCENE_36, "title",analyzer);
        Query query = queryParser.parse(keywords);

        Directory directory = FSDirectory.open(new File("D:\\oriTxt"));
        IndexSearcher indexSearcher = new IndexSearcher(IndexReader.open(directory));
        TopDocs topDocs = indexSearcher.search(query,10);
        out.println("总共匹配多少个：" + topDocs.totalHits);
        //获取查询结果集
        ScoreDoc[] hits = topDocs.scoreDocs;
        //字符串结果
        StringBuffer result = new StringBuffer();
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (int i = 0;i < scoreDocs.length;i++){
            int docID = scoreDocs[i].doc;
            Document document = indexSearcher.doc(docID);
            result.append("title:" + document.get("title"));

        }
        indexSearcher.close();
        return result.toString();
    }
    //根据作者查询
    public static void IndexSearch_ForAuthor(String keywords) throws Exception{
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
        QueryParser queryParser = new QueryParser(Version.LUCENE_36, "author",analyzer);
        Query query = queryParser.parse(keywords);

        Directory directory = FSDirectory.open(new File("D:\\oriTxt"));
        IndexSearcher indexSearcher = new IndexSearcher(IndexReader.open(directory));
        TopDocs topDocs = indexSearcher.search(query,10);
        out.println("满足结果记录条数："+topDocs.totalHits);

        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (int i = 0;i < scoreDocs.length;i++){
            int docID = scoreDocs[i].doc;
            Document document = indexSearcher.doc(docID);
            out.println("author:" +document.get("author"));

        }
        indexSearcher.close();
    }

    //根据所属查询
    public static void IndexSearch_ForAffiliation(String keywords) throws Exception{
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
        QueryParser queryParser = new QueryParser(Version.LUCENE_36, "affiliation",analyzer);
        Query query = queryParser.parse(keywords);

        Directory directory = FSDirectory.open(new File("D:\\oriTxt"));
        IndexSearcher indexSearcher = new IndexSearcher(IndexReader.open(directory));
        TopDocs topDocs = indexSearcher.search(query,10);
        out.println("满足结果记录条数："+topDocs.totalHits);

        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (int i = 0;i < scoreDocs.length;i++){
            int docID = scoreDocs[i].doc;
            Document document = indexSearcher.doc(docID);
            out.println("affiliation:" +document.get("affiliation"));

        }
        indexSearcher.close();
    }

    //根据正文查询
    public static void IndexSearch_ForFulltext(String keywords) throws Exception{
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
        QueryParser queryParser = new QueryParser(Version.LUCENE_36, "fulltext",analyzer);
        Query query = queryParser.parse(keywords);

        Directory directory = FSDirectory.open(new File("D:\\oriTxt"));
        IndexSearcher indexSearcher = new IndexSearcher(IndexReader.open(directory));
        TopDocs topDocs = indexSearcher.search(query,10);
        out.println("满足结果记录条数："+topDocs.totalHits);

        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (int i = 0;i < scoreDocs.length;i++){
            int docID = scoreDocs[i].doc;
            Document document = indexSearcher.doc(docID);
            out.println("fulltext:" +document.get("fulltext"));

        }
        indexSearcher.close();
    }

    //全域查询
    public static void IndexSearch_ForAll(String keyword) throws Exception{
        String[] fields = {"title","author","fulltext","address","affiliation","abstract","keyword","date"};
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
        MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_36,fields,analyzer);
        Query query = parser.parse(keyword);

        Directory directory = FSDirectory.open(new File("D:\\oriTxt"));
        IndexSearcher indexSearcher = new IndexSearcher(IndexReader.open(directory));
        TopDocs topDocs = indexSearcher.search(query,10);
        out.println("满足结果记录条数："+topDocs.totalHits);

        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (int i = 0;i < scoreDocs.length;i++){
            int docID = scoreDocs[i].doc;
            Document document = indexSearcher.doc(docID);
            out.println("title:" +document.get("title"));
            out.println("author:" +document.get("author"));
            out.println("keyword:" +document.get("keyword"));
            out.println("");

        }
        indexSearcher.close();


    }
    public static String [] IndexSearch_Type(String keywords,String type) throws Exception{
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
        QueryParser queryParser = new QueryParser(Version.LUCENE_36, type,analyzer);
        Query query = queryParser.parse(keywords);

        Directory directory = FSDirectory.open(new File("D:\\oriTxt"));
        IndexSearcher indexSearcher = new IndexSearcher(IndexReader.open(directory));
        TopDocs topDocs = indexSearcher.search(query,50);

        String [] result = new String[4];
        StringBuffer result_title = new StringBuffer();
        StringBuffer result_abstract = new StringBuffer();
        StringBuffer result_date = new StringBuffer();
        StringBuffer result_author = new StringBuffer();
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (int i = 0;i < scoreDocs.length;i++){
            int docID = scoreDocs[i].doc;
            Document document = indexSearcher.doc(docID);
            result_title.append(document.get("title")+"\n");
            result_abstract.append(limitWord(document.get("abstract"),30)+"\n");
            result_date.append(replaceText(document.get("date")) + "\n");
            result_author.append(document.get("author") + "\n");
        }
        result[0] = result_title.toString();
        result[1] = result_abstract.toString();
        result[2] = result_date.toString();
        result[3] = result_author.toString();
        indexSearcher.close();
        return result;
    }
    public static String limitWord(String str,int count){
        if (str == null || str.length() == 0) {
            return "...";
        }
        String [] word = str.split("\\s+");
        String result = "";
        if(count < word.length){
        for(int i = 0;i<count;i++){
            result += word[i]+" ";
        }
        return result + "...";
        }
        else return str + "...";
    }
    public static String replaceText(String str){
        if (str == null || str.length() == 0) {
            return "Unknown";
        }
        else return str;
    }


}


