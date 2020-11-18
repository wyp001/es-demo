package com.wyp.ik.config;

import org.apache.lucene.analysis.util.ResourceLoader;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
import org.wltea.analyzer.dic.Dictionary;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TestIK {
    private static Dictionary singleton;
    private static ResourceLoader loader;
    static List<String> dictList = new ArrayList<String>();
    
    
    
    public static void main(String[] args) throws IOException {
        for (int i = 0; i < 4; i++) {
            test(i);
        }
        
    }
    
    public static void test(int num) throws IOException{
        String content = "曾做过周朝“守藏室之官”（管理藏书的官员），是中国古代伟大的思想家、哲学家、文学家和史学家，"
                + "被道教尊为教祖，世界文化名人。老子思想主张“无为”，《老子》以“道”解释宇宙万物的演变，"
                + "“道”为客观自然规律，同时又具有“独立不改，周行而不殆”的永恒意义。《老子》书中包括大量"
                + "朴素辩证法观点，如以为一切事物均具有正反两面，并能由对立而转化，是为“反者道之动”，“正复为奇，"
                + "善复为妖”，“祸兮福之所倚，福兮祸之所伏”。又以为世间事物均为“有”与“无”之统一，“有、无相生”，"
                + "而“无”为基础，“天下万物生于有，有生于无”。他关于民众的格言有：“天之道，损有余而补不足，人之道"
                + "则不然，损不足以奉有余”；“民之饥，以其上食税之多”；“民之轻死，以其上求生之厚”；“民不畏死，奈何以"
                + "死惧之”。他的哲学思想和由他创立的道家学派，不但对中国古代思想文化的发展作出了重要贡献，而且对"
                + "中国2000多年来思想文化的发展产生了深远的影响。关于他的身份，还有人认为他是老莱子，也是楚国人，"
                + "跟孔子同时，曾著书十五篇宣传道家之用。";
        StringReader input = new StringReader(content.trim());
        MyConfiguration mycfg = new MyConfiguration();
        mycfg.setUseSmart(true);  //true　用智能分词　，false细粒度
        
        if (singleton == null) {
            singleton = Dictionary.initial(mycfg);
        }
        
        String dict = null;
        if(num==0){
            dict = "org/wltea/analyzer/dic/main2012.dic";//分词jar包自带的词典
            System.out.println("---------------------------------------------------------");
            System.out.println("加载扩展词典："+dict);
            mycfg.setMainDictionary(dict);//动态设置自定义的词库
            
            //在使用新词典时，清除其他词典（刷新）
            if(dictList.size()>0){
                singleton.disableWords(dictList);
            }
            
            //dictList:通过我们指定的词典文件获取到的词组list
            dictList = getWordList(dict);
            singleton.addWords(dictList);
        }else if(num==1){
            dict = "test/dict1.dic";
            System.out.println("---------------------------------------------------------");
            System.out.println("加载扩展词典："+dict);
            mycfg.setMainDictionary(dict);//动态设置自定义的词库

            //在使用新词典时，清除其他词典（刷新）
            if(dictList.size()>0){
                singleton.disableWords(dictList);
            }
            
            dictList = getWordList(dict);
            singleton.addWords(dictList);
        }else if(num==2){
            dict = "test/dict2.dic";
            System.out.println("---------------------------------------------------------");
            System.out.println("加载扩展词典："+dict);
            mycfg.setMainDictionary(dict);//动态设置自定义的词库
            
            //在使用新词典时，清除其他词典（刷新）
            if(dictList.size()>0){
                singleton.disableWords(dictList);
            }
            
            dictList = getWordList(dict);
            singleton.addWords(dictList);
        }else if(num==3){
            dict = "test/dict3.dic";
            System.out.println("加载扩展词典："+dict);
            mycfg.setMainDictionary(dict);//动态设置自定义的词库
            
            //在使用新词典时，清除其他词典（刷新）
            if(dictList.size()>0){
                singleton.disableWords(dictList);
            }
            
            dictList = getWordList(dict);
            singleton.addWords(dictList);
        }
        

        IKSegmenter ikSeg = new IKSegmenter(input,mycfg);
        
        Lexeme lexeme=null;  
        while((lexeme=ikSeg.next())!=null){
            String keys = lexeme.getLexemeText();
            
            if(keys.length()>1){
                System.out.println(keys);
            }
        }
        input.close();
    }
    
    /**
     * 读取字典
     * @param dict
     * @return
     */
    public static List<String> getWordList(String dict) {
        List<String> list = new ArrayList<>();
        InputStream is = null;
        try {
            is = TestIK.class.getClass().getResourceAsStream("/"+dict);
            BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"), 1024);
            String theWord = null;
            do {
                theWord = br.readLine();
                if (theWord != null && !"".equals(theWord.trim())) {
                    list.add(theWord);
                }
            } while (theWord != null);
        } catch (IOException ioe) {
            System.err.println("字典文件读取失败:"+dict);
            ioe.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return list;
    }
    
}