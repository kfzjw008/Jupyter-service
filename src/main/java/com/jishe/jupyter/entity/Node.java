package com.jishe.jupyter.entity;

/**
 * @program: jupyter
 * @description: neo4j实体
 * @author: kfzjw008(Junwei Zhang)
 * @create: 2020-12-14 15:03
 **/
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

import javax.persistence.Entity;

@Getter
@Setter
@NodeEntity(label = "Node")//对应修改结点标签
public class Node {

    @Id
    @GeneratedValue
    private Long id;
    @Property
    private String name;   //这一部分需要对应修改节点属性
    public Node() {

    }//较新版本都需要构造无参数函数

    public Node(String title) {
        this.name = name;//传参
    }


}
