package com.jishe.jupyter.repository;

/**
 * @program: jupyter
 * @description: neo4j
 * @author: kfzjw008(Junwei Zhang)
 * @create: 2020-12-14 15:06
 **/
import com.jishe.jupyter.entity.Node;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.jishe.jupyter.global;

public interface NodeRepository extends Neo4jRepository<Node,Long> {

    //Query标签后面的内容根据自己Neo4j数据库定义查询
//Neo4j里面也有一些固定的查询不用用到query标签，如findall之类，自己去了解一下
    @Query(value = "MATCH (n:Node) RETURN n LIMIT 25")
    List<Node>  findByName();
    @Query(value = "MATCH (n:Node{name:{word}})-[r]->(m:Node) RETURN m ")//ORDER BY id(n)
    List<Node>  findALLGX(@Param("word")String word);//输入一个实体，输出该实体所有关系
    @Query(value = "MATCH (n:Node{name:{word}})-[r]->(m:Node) RETURN type(r) ")
    List<String>  findALLGX2(@Param("word")String word);//输入一个实体，输出该实体所有关系
    //MATCH (n:Node{name:"银河系"})-[r]->(m:Node) RETURN n,m,r

    //@Query(value = "MATCH (n:Node{name:{word1}})-([a: "+lx+" ])-(names:Node) return properties(names).name")
   // String  findshitiguanxi(@Param("word1")String word1);//输入一个实体，输出该实体所有关系
    //Match (n:Node{name: '银河系'})-[:类型]-(names:Node) return properties(names).name
}
