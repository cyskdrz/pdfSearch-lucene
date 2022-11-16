package com.company;

public class SearchTest {
    public static  void main(String[] args) throws Exception {
        //测试搜索
        IndexSearch is = new IndexSearch();
        System.out.println("\n"+"code : 0"+"\n");
        String [] result = is.IndexSearch_Type("learning","title");
        String [] title = result[0].split("\n");
        String [] fullText = result[1].split("\n");
        String [] date = result[2].split("\n");
        String [] author = result[3].split("\n");
        for (int i = 0;i< result.length;i++){
            System.out.println("title : "+title[i]);
            System.out.println("text : "+fullText[i]);
            System.out.println("author : "+author[i]);
            System.out.println("date :"+date[i]);
        }

        }

    public static String limitWord(String str,int count){
        String [] word = str.split("\\s+");
        String result = "";
        for(int i = 0;i<count;i++){
            result += word[i]+" ";
        }
        return result + "...";
    }
    }

