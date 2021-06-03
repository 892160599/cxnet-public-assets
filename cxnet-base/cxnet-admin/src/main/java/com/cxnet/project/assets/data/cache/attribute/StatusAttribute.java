package com.cxnet.project.assets.data.cache.attribute;

import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Element;
import net.sf.ehcache.search.attribute.AttributeExtractor;

/**
 * 消息记录缓存 状态 属性
 */
@Slf4j
public class StatusAttribute implements AttributeExtractor {
    @Override
    public Object attributeFor(Element element, String attributeName) {
        if (!"status".equals(attributeName)) {
            throw new AssertionError(attributeName);
        }
        return element.getObjectValue();
    }
}
