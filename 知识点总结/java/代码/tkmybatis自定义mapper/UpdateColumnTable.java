package cn.zswltech.ztzl.core.plugin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 配合 InsertIgnoreOnDupKeyUpdateMapper 使用 满足条件时才更新
 *
 * @author wangchuanhao
 * @date 2022/5/10 7:32 PM
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateColumnTable {

    /**
     * 类条件
     */
    private String condition;

    /**
     * 字段条件
     */
    private Map<String, String> fieldConditionMap;

}
