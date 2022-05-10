package cn.zswltech.ztzl.core.plugin;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import tk.mybatis.mapper.mapperhelper.EntityHelper;

import javax.persistence.Entity;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @see EntityHelper
 * 配合 InsertIgnoreOnDupKeyUpdateMapper 使用 满足条件时才更新
 * 存储实体类的更新条件
 *
 * @author wangchuanhao
 * @date 2022/5/10 7:30 PM
 */
public class UpdateColumnHelper {

    private static final Map<Class<?>, UpdateColumnTable> tableMap = new ConcurrentHashMap<>();

    private static final UpdateColumnTable defaultTable = UpdateColumnTable
            .builder().condition("")
            .fieldConditionMap(new HashMap<>())
            .build();

    public static UpdateColumnTable getUpdateColumnTable(Class<?> tableClass) {
        if (tableMap.containsKey(tableClass)) {
            return tableMap.get(tableClass);
        }
        synchronized (tableClass) {
            if (tableMap.containsKey(tableClass)) {
                return tableMap.get(tableClass);
            }
            OnDupKeyUpdateIf classAnnotation = tableClass.getAnnotation(OnDupKeyUpdateIf.class);
            String classCondition = Optional.ofNullable(classAnnotation).map(OnDupKeyUpdateIf::condition).orElse("");
            Map<String, String> fieldConditionMap = new HashMap<>();
            collectFieldCondition(tableClass, fieldConditionMap);
            UpdateColumnTable updateColumnTable = defaultTable;
            if (StringUtils.isNotBlank(classCondition) || CollectionUtils.isNotEmpty(fieldConditionMap.entrySet())) {
                updateColumnTable = UpdateColumnTable.builder()
                        .condition(classCondition)
                        .fieldConditionMap(fieldConditionMap)
                        .build();
            }
            tableMap.put(tableClass, updateColumnTable);
            return updateColumnTable;
        }
    }

    private static void collectFieldCondition(Class<?> tableClass, Map<String, String> fieldConditionMap) {
        if (fieldConditionMap == null) {
            fieldConditionMap = new HashMap<>();
        }
        if (tableClass.equals(Object.class)) {
            return;
        }
        // 先找父类的字段，如有同名，子类覆盖父类
        Class<?> superClass = tableClass.getSuperclass();
        if (superClass != null
                && !superClass.equals(Object.class)
                && (superClass.isAnnotationPresent(Entity.class)
                || (!Map.class.isAssignableFrom(superClass)
                && !Collection.class.isAssignableFrom(superClass)))) {
            collectFieldCondition(tableClass.getSuperclass(), fieldConditionMap);
        }
        // 找本类的字段
        Field[] fields = tableClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            //排除静态字段
            if (!Modifier.isStatic(field.getModifiers())) {
                OnDupKeyUpdateIf fieldAnnotation = field.getAnnotation(OnDupKeyUpdateIf.class);
                if (fieldAnnotation != null) {
                    fieldConditionMap.put(field.getName(), fieldAnnotation.condition());
                }
            }
        }
    }

}
