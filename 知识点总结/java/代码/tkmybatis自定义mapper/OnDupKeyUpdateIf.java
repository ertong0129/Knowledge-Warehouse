package cn.zswltech.ztzl.core.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 配合 InsertIgnoreOnDupKeyUpdateMapper 使用 满足条件时才更新
 * INSERT IGNORE INTO table ON DUMPLICATE KEY UPDATE name = IF(condition, #{name}, name)
 * @author wangchuanhao
 * @date 2022/5/10 5:41 PM
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OnDupKeyUpdateIf {

    String condition();

}
