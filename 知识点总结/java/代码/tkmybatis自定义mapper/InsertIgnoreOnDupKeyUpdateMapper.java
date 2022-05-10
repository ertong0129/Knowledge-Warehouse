package cn.zswltech.ztzl.core.plugin;

import org.apache.ibatis.annotations.InsertProvider;
import tk.mybatis.mapper.provider.base.BaseInsertProvider;

/**
 * 通用Mapper接口,插入时记录存在就更新 支持注解配置更新条件, 通过xxx = IF(xxx, x, s)方式实现
 *
 * @author wangchuanhao
 * @date 2022/5/10 5:35 PM
 */
public interface InsertIgnoreOnDupKeyUpdateMapper<T> {

    @InsertProvider(type = InsertIgnoreOnDupKeyUpdateProvider.class, method = "dynamicSQL")
    int insertOrUpdateSelective(T record);

}
