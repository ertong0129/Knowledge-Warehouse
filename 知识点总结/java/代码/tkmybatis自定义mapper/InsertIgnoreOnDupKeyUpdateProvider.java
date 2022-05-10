package cn.zswltech.ztzl.core.plugin;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.MapperException;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;
import tk.mybatis.mapper.util.StringUtil;

import java.util.Optional;
import java.util.Set;

/**
 * 插入时记录存在就更新 支持注解配置更新条件, 通过xxx = IF(xxx, x, s)方式实现
 *
 * @author wangchuanhao
 * @date 2022/5/10 5:41 PM
 */
@Slf4j
public class InsertIgnoreOnDupKeyUpdateProvider extends MapperTemplate {

    public InsertIgnoreOnDupKeyUpdateProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     * 语法
     * <pre>
     *      <bind name="countryname_bind" value='@java.util.UUID@randomUUID().toString().replace("-", "")'/>
     *      INSERT IGNORE INTO country_u(id,countryname,countrycode) VALUES
     *      <trim prefix="(" suffix=")" suffixOverrides=",">
     *          <if test="id != null">#{id,javaType=java.lang.Integer},</if>
     *          <if test="id == null">#{id,javaType=java.lang.Integer},</if>
     *          <if test="countryname != null">#{countryname,javaType=java.lang.String},</if>
     *          <if test="countryname == null">#{countryname_bind,javaType=java.lang.String},</if>
     *          <if test="countrycode != null">#{countrycode,javaType=java.lang.String},</if>
     *          <if test="countrycode == null">#{countrycode,javaType=java.lang.String},</if>
     *      </trim>
     *      ON DUPLICATE KEY UPDATE
     *      <trim prefix="" suffix="" suffixOverrides=",">
     *          <!-- 满足条件才更新字段值 -->
     *          <if test="countryname != null">countryname=IF(condition,#{countryname,javaType=java.lang.String},countryname),</if>
     *          <if test="countrycode == null">countrycode=IF(#{countrycode,javaType=java.lang.String},countrycode),</if>
     *      </trim>
     *   </pre>
     * @param ms
     * @return
     */
    public String insertOrUpdateSelective(MappedStatement ms) {
        log.info("进入insert ignore mybatis方法");
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        //获取全部列
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        //Identity列只能有一个
        Boolean hasIdentityKey = false;
        //先处理cache或bind节点
        for (EntityColumn column : columnList) {
            if (!column.isInsertable()) {
                continue;
            }
            if (StringUtil.isNotEmpty(column.getSequenceName())) {
                //sql.append(column.getColumn() + ",");
            } else if (column.isIdentity()) {
                //这种情况下,如果原先的字段有值,需要先缓存起来,否则就一定会使用自动增长
                //这是一个bind节点
                sql.append(SqlHelper.getBindCache(column));
                //如果是Identity列，就需要插入selectKey
                //如果已经存在Identity列，抛出异常
                if (hasIdentityKey) {
                    //jdbc类型只需要添加一次
                    if (column.getGenerator() != null && column.getGenerator().equals("JDBC")) {
                        continue;
                    }
                    throw new MapperException(ms.getId() + "对应的实体类" + entityClass.getCanonicalName() + "中包含多个MySql的自动增长列,最多只能有一个!");
                }
                //插入selectKey
                newSelectKeyMappedStatement(ms, column);
                hasIdentityKey = true;
            } else if (column.isUuid()) {
                //uuid的情况，直接插入bind节点
                sql.append(SqlHelper.getBindValue(column, getUUID()));
            }
        }
        sql.append(insertIgnoreIntoTable(entityClass, tableName(entityClass)));
        sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        for (EntityColumn column : columnList) {
            if (!column.isInsertable()) {
                continue;
            }
            if (StringUtil.isNotEmpty(column.getSequenceName()) || column.isIdentity() || column.isUuid()) {
                sql.append(column.getColumn() + ",");
            } else {
                sql.append(SqlHelper.getIfNotNull(column, column.getColumn() + ",", isNotEmpty()));
            }
        }
        sql.append("</trim>");
        sql.append("<trim prefix=\"VALUES(\" suffix=\")\" suffixOverrides=\",\">");
        for (EntityColumn column : columnList) {
            if (!column.isInsertable()) {
                continue;
            }
            //优先使用传入的属性值,当原属性property!=null时，用原属性
            //自增的情况下,如果默认有值,就会备份到property_cache中,所以这里需要先判断备份的值是否存在
            if (column.isIdentity()) {
                sql.append(SqlHelper.getIfCacheNotNull(column, column.getColumnHolder(null, "_cache", ",")));
            } else {
                //其他情况值仍然存在原property中
                sql.append(SqlHelper.getIfNotNull(column, column.getColumnHolder(null, null, ","), isNotEmpty()));
            }
            //当属性为null时，如果存在主键策略，会自动获取值，如果不存在，则使用null
            //序列的情况
            if (StringUtil.isNotEmpty(column.getSequenceName())) {
                sql.append(SqlHelper.getIfIsNull(column, getSeqNextVal(column) + " ,", isNotEmpty()));
            } else if (column.isIdentity()) {
                sql.append(SqlHelper.getIfCacheIsNull(column, column.getColumnHolder() + ","));
            } else if (column.isUuid()) {
                sql.append(SqlHelper.getIfIsNull(column, column.getColumnHolder(null, "_bind", ","), isNotEmpty()));
            }
        }
        sql.append("</trim>");
        sql.append("ON DUPLICATE KEY UPDATE ");
        sql.append(updateSetColumnsWithIf(entityClass, null));
        log.info(sql.toString());
        return sql.toString();
    }


    /**
     * 改造SqlHelper方法，使之符合自己的逻辑
     * @see
     * @param entityClass
     * @param defaultTableName
     * @return
     */
    private static String insertIgnoreIntoTable(Class<?> entityClass, String defaultTableName) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT IGNORE INTO ");
        sql.append(SqlHelper.getDynamicTableName(entityClass, defaultTableName));
        sql.append(" ");
        return sql.toString();
    }

    /**
     * 改造SqlHelper方法，使之符合自己的逻辑
     * @see SqlHelper#updateSetColumns(java.lang.Class, java.lang.String, boolean, boolean)
     * @param entityClass
     * @param entityName
     * @return
     */
    public static String updateSetColumnsWithIf(Class<?> entityClass, String entityName) {
        StringBuilder sql = new StringBuilder();
        sql.append("<trim prefix=\"\" suffix=\"\" suffixOverrides=\",\">");
        //获取全部列
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        //当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
        for (EntityColumn column : columnList) {
            if (!column.isId() && column.isUpdatable()) {
                sql.append(SqlHelper.getIfNotNull(entityName, column, getColumnEqualsHolderWrapIfCondition(entityClass, column, entityName) + ",", false));
            }
        }
        sql.append("</trim>");
        return sql.toString();
    }

    public static String getColumnEqualsHolderWrapIfCondition(Class<?> entityClass, EntityColumn column, String entityName) {
        UpdateColumnTable updateColumnTable = UpdateColumnHelper.getUpdateColumnTable(entityClass);
        // 先获取字段上的条件 如果为空再取类上的条件
        String condition = Optional.ofNullable(updateColumnTable.getFieldConditionMap())
                .map(m -> m.get(column.getProperty()))
                .orElse(updateColumnTable.getCondition());
        if (StringUtils.isNotBlank(condition)) {
            // 需要wrap IF(conditon, #{xxx}, xxx)
            return String.format("%s = IF(%s,%s,%s)", column.getColumn(), condition, column.getColumnHolder(entityName), column.getColumn());
        }
        // 条件为空，直接更新
        return column.getColumnEqualsHolder(entityName);
    }

}
