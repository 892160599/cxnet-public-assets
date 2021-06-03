package com.cxnet.util;

import com.cxnet.common.constant.Convert;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.project.system.dict.service.SysDictDataServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Map;

@Component
public class ConvertUtil {

    @Autowired(required = false)
    private SysDictDataServiceI sysDictDataServiceI;

    public void convert(Object obj, Field[] fields) {
        Map<String, String> sysDictDataMap = null;
        try {
            for (Field field : fields) {
                if (null == field) {
                    continue;
                }
                field.setAccessible(true);
                Convert annotation = field.getAnnotation(Convert.class);
                if (null != annotation) {
                    if (annotation.dictValue()) {
                        Class<?> aClass = obj.getClass();
                        Field dictField = aClass.getDeclaredField(field.getName() + "Dict");
                        if (null != dictField) {
                            dictField.setAccessible(true);
                            dictField.set(obj, field.get(obj));
                        }
                    }
                    String key = annotation.key();
                    if (StringUtils.isNotEmpty(key)) {
                        sysDictDataMap = sysDictDataServiceI.selectDictDataMapByType(key);
                        String fie = sysDictDataMap.get(field.get(obj));
                        if (StringUtils.isNotBlank(fie)) {
                            field.set(obj, fie);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
