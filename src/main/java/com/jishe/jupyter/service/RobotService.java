package com.jishe.jupyter.service;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.jishe.jupyter.component.JsonSimple;
import com.jishe.jupyter.entity.*;
import com.jishe.jupyter.global;
import com.jishe.jupyter.repository.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.jishe.jupyter.component.JsonSimple;
import com.jishe.jupyter.entity.Node;
import com.jishe.jupyter.repository.NodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.neo4j.jdbc.Driver;
import java.sql.*;
/**
 * @program: jupyter
 * @description: 排行榜服务层，主要提供排行榜的服务生产。
 * @author: kfzjw008(Junwei Zhang)
 * @create: 2020-01-16 15:05
 **/
@Service
@Transactional
public class RobotService {
    @Autowired
    private RankList_AllQuestion_Repoistory rankList_allQuestion_repoistory;
    @Autowired
    private RankList_CurrentQuestion_Repoistory RankList_CurrentQuestion_Repoistory;
    @Autowired
    private RankList_integral_Repoistory RankList_integral_Repoistory;
    @Autowired
    private ranklist_tz_Repository ranklist_tz_Repository;
    @Autowired
    NodeRepository NodeRepository;

    /**
     * 输入请求问题
     *
     * @param text
     * @return 问题中的名词
     * @throws IOException
     */
    public List<String> entityRecognize(String text) throws IOException {
        //String text = "太阳到地球的距离";
        String url = "http://comdo.hanlp.com/hanlp/v1/ner/chineseName";
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(url);

        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("text", text));
        httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        httppost.addHeader("token", "f6e4bad5fe904e5ca5ad5007cff6c6e61607435341148token");

        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            // System.out.println(EntityUtils.toString(entity)); Entity => String
            JSONObject jsonObject = JSONUtil.parseObj(EntityUtils.toString(entity));

            List<String> wordList = jsonObject.getJSONArray("data").stream().filter(word ->
                    ((JSONObject) word).get("nature").toString().equals("n")) //
                    .map(word ->
                            ((JSONObject) word).get("word").toString())
                    .collect(Collectors.toList());
            return wordList;
        }
        return null;
    }

    /**
     * 如果词典包含则为实体
     * @param words
     * @return
     */
    public List<String> getRelationShip(List<String> words){
        return words.stream().filter(word -> !global.wordList.contains(word)).collect(Collectors.toList());
    }

    /**
     * 如果词典不包含则为关系
     * @param words
     * @return
     */
    public List<String> getEntity(List<String> words){
        return words.stream().filter(word -> global.wordList.contains(word)).collect(Collectors.toList());
    }

    /**
     * * @name: 总练习数量排行
     *
     * @description: 微信用户登录服务模块，该方法用于实现用户登录。
     * @author: kfzjw008(Junwei Zhang)
     * @create: 2020-01-16 22:35
     **/


    //只查实体，获得相关信息√
    public String shiti(String word) throws SQLException, ClassNotFoundException {
        List<Node> f2 = NodeRepository.findALLGX(word);
        List<String> f3 = NodeRepository.findALLGX2(word);
        if(f2.size()==0){
            return "这个知识的属性我目前还没有储备嗷~不过我会继续努力的！";
        }
        String result="智能小星帮你找到了"+word+"相关的资料，都列在下边了嗷~~\n";
        try{
            for(int i=0;i<f2.size();i++){
                int j=i-1;
              //  if(j<0){j=f2.size()-1;}
                System.out.println("f2="+f2.size()+"f3="+f3.size());
              //   System.out.println(f2.get(i).getName()+"\\n ");
              //  System.out.println(f2.get(0).getName());
              //  System.out.println(f2.get(1).getName());
              //  System.out.println(f2.get(2).getName());
              //  System.out.println(f3.get(0));
              //  System.out.println(f3.get(1));
              //  System.out.println(f3.get(2));
                System.out.println("i="+i);
                System.out.println("j="+j);
                result=result+" "+f3.get(i)+" : "+f2.get(i).getName()+"\n ";
            }
        }
   catch (Exception e){
       //  throw  e;
       this.shitiguanxishiti(word,"中文名",word);
       List<Node> f22 = NodeRepository.findALLGX(word);
       List<String> f33 = NodeRepository.findALLGX2(word);
       String result2="智能小星帮你找到了"+word+"相关的资料，都列在下边了嗷~~\n";
       for(int i=0;i<f22.size();i++){
           int j=i-1;
         //  if(j<0){j=f22.size()-1;}
          // System.out.println("f2="+f2.size()+"f3="+f3.size());
           //   System.out.println(f2.get(i).getName()+"\\n ");
           //  System.out.println(f2.get(0).getName());
           //  System.out.println(f2.get(1).getName());
           //  System.out.println(f2.get(2).getName());
           //  System.out.println(f3.get(0));
           //  System.out.println(f3.get(1));
           //  System.out.println(f3.get(2));
           System.out.println("i="+i);
           System.out.println("j="+j);
           result2=result2+" "+f33.get(i)+" : "+f22.get(i).getName()+"\n ";
       }
       return result2;
     //  this.shiti(word);
   }
        return result;
        //System.out.println(result);
       // System.out.println(JsonSimple.toJson(f2));
        //System.out.println("共查出来节点有："+f2.size()+"个");
    }
    //通过实体、关系，查实体
    public String shitiguanxi(String shiti,String guanxi) throws SQLException, ClassNotFoundException {

        String result="";
        String word1=shiti;
        String word2=guanxi;
        Class.forName("org.neo4j.jdbc.Driver");
        Connection con = DriverManager
                .getConnection("jdbc:neo4j:http://ucas-sunflower.design:7474/","neo4j","orbit-frank-fish-major-amanda-4467");  //创建连接

        String query = "Match (n:Node{name: '"+word1+"'})-[:"+word2+"]-(names:Node) return properties(names).name";

        PreparedStatement stmt = null;       //采用预编译，和关系数据库不一样的是,参数需要使用{1},{2},而不是?
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(query);
            //stmt.setInt(1, 14);
            rs = stmt.executeQuery();
            System.out.println(rs.getRow());
            while (rs.next()) {
                System.out.println("a: " + rs.getString("properties(names).name"));
                result= rs.getString("properties(names).name");
            }
            //  result= rs.getString("properties(names).name");
           // System.out.println(word1+"的"+word2+"是"+result);
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != rs) {
                rs.close();
            }
            if (null != stmt) {
                stmt.close();
            }
        }


        return word1+"的"+word2+"是"+result;
    }
    //通过实体、实体，查关系
    public String shitiguanxishiti(String shiti,String guanxi,String shiti2) throws ClassNotFoundException, SQLException {
        System.out.println("tiaozhuan!");
        String result="";
        String word1=shiti;
        String word2=guanxi;
        Class.forName("org.neo4j.jdbc.Driver");
        Connection con = DriverManager
                .getConnection("jdbc:neo4j:http://ucas-sunflower.design:7474/","neo4j","orbit-frank-fish-major-amanda-4467");  //创建连接

        String query = "match(x:Node{name:'"+shiti+"'}) return x.name";
        String query2 = "match(x:Node{name:'"+shiti2+"'}) return x.name";
        String query3 = "create (x:Node{name:'"+shiti+"'})";
        String query4 = "create (x:Node{name:'"+shiti2+"'})";
        String query5 ="match (x:Node{name:'"+shiti+"'}),(y:Node{name:'"+shiti2+"'}) create (x)-[jx:"+guanxi+"]->(y)";

        PreparedStatement stmt = null;       //采用预编译，和关系数据库不一样的是,参数需要使用{1},{2},而不是?
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(query);
            //stmt.setInt(1, 14);
            rs = stmt.executeQuery();
            System.out.println(rs.getRow());
            int biaoshi2=0;
            while (rs.next()) {
                biaoshi2=1;
                System.out.println("a: " + rs.getString("x.name"));
                result= rs.getString("x.name");

            }
            if(biaoshi2==0){
                stmt = con.prepareStatement(query3);
                //stmt.setInt(1, 14);
                stmt.executeUpdate();


            }
            stmt = con.prepareStatement(query2);
            //stmt.setInt(1, 14);
            rs = stmt.executeQuery();
            int biaoshi=0;
            System.out.println(rs.getRow());
            while (rs.next()) {
                biaoshi=1;
                System.out.println("a: " + rs.getString("x.name"));
                result= rs.getString("x.name");

            }
            if(biaoshi==0){
                stmt = con.prepareStatement(query4);
                //stmt.setInt(1, 14);
                stmt.executeUpdate();


            }
            stmt = con.prepareStatement(query5);
            //stmt.setInt(1, 14);
            stmt.executeUpdate();
            //  result= rs.getString("properties(names).name");
            // System.out.println(word1+"的"+word2+"是"+result);
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != rs) {
                rs.close();
            }
            if (null != stmt) {
                stmt.close();
            }
        }


        return "我学会了！"+this.shitiguanxi(shiti,guanxi);
    }
    //只查关系，获得相关实体
    public String guanxi(String word){
        String result="";
        return result;
    }
}
