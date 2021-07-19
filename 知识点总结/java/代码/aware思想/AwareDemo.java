package com.xkcoding.log.aop.entity;

import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 从spring bean初始化时aware接口的实现机制中
 * 思考的一套不同vo同几个属性的填充方法 -- 定义设值接口
 */
public class AwareDemo {

    public static void main(String[] args) {
        Z1 z1 = new Z1();
        List<SetValueAware> z1List = Arrays.asList(z1);
        Z2 z2 = new Z2();
        List<SetValueAware> z2List = Arrays.asList(z2);
        Z3 z3 = new Z3();
        List<SetValueAware> z3List = Arrays.asList(z3);

        testFillValue(z1List);
        testFillValue(z2List);
        testFillValue(z3List);
    }

    public static void testFillValue(List<SetValueAware> setValueAwareBeanList) {
        //根据id去数据库查其他数据的过程
        List<Integer> idList = setValueAwareBeanList.stream().map(SetValueAware::getId).collect(Collectors.toList());
        List<String> dataList = idList
            .stream()
            .map(id -> UUID.randomUUID().toString().replaceAll("-",""))
            .collect(Collectors.toList());

        //映射
        Map<Integer, String> dataMap = new HashMap<>();
        for (int i = 0; i < idList.size(); i++) {
            dataMap.put(idList.get(i), dataList.get(i));
        }

        //根据type设置值
        for (SetValueAware setValueAwareBean : setValueAwareBeanList) {
            String data = dataMap.get(setValueAwareBean.getId());
            data = data + setValueAwareBean.getType();
            setValueAwareBean.setNeedFillValue(data);
        }
    }

}

@Data
class Z1 implements SetValueAware {
    private String z1PrivateValue;


    private Integer id;
    private Integer type;
    private String needFillValue;
}

@Data
class Z2 implements SetValueAware {
    private String z2PrivateValue;

    private Integer id;
    private Integer type;
    private String needFillValue;
}

@Data
class Z3 implements SetValueAware {
    private String z3PrivateValue;

    private Integer id;
    private Integer type;
    private String needFillValue;
}

interface SetValueAware {

    Integer getId();

    Integer getType();

    void setNeedFillValue(String needFillValue);
}

