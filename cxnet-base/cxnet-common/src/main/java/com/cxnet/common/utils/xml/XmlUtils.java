package com.cxnet.common.utils.xml;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.List;

public class XmlUtils {


    public static String getColumns(String json, String... cols) {
        int len = cols.length;
        String[][] keyWord = new String[len][];
        for (int i = 0; i < len; i++) {
            keyWord[i] = cols[i].split("\\.");
        }
        StringBuffer colVal = new StringBuffer();
        for (int i = 0; i < len; i++) {
            JSONObject jsonObject = JSON.parseObject(json);
            for (int j = 0; j < keyWord[i].length - 1; j++) {
                jsonObject = (JSONObject) jsonObject.get(keyWord[i][j]);
            }
            if (colVal.toString().length() < 1) {
                colVal.append(jsonObject.get(keyWord[i][keyWord[i].length - 1]).toString());
            } else {
                colVal.append("," + jsonObject.get(keyWord[i][keyWord[i].length - 1]).toString());
            }
            jsonObject = null;
        }
        return colVal.toString();
    }

    /**
     * {
     * "result":{ "msg":"",
     * "list":[
     * {"user":"123","pass":"111"}
     * {"user":"456","pass":"222"}
     * ]
     * }
     * }
     * <p>
     * 对于上面的json，，arrayPath为“result.list”,cols可以为user,pass
     * 最终返回的是user对应的值，或pass的值，cols对应的值
     *
     * @param json
     * @param arrayPath JSONArray的路径
     * @param cols      JSONArray中对象的一些成员
     * @return JSONArray中对象的一些成员的value值
     */
    public static String getArray(String json, String arrayPath, String... cols) {
        int len = cols.length;
        String[] keyWord = arrayPath.split("\\.");
        StringBuffer colVals = new StringBuffer();
        JSONObject jsonObject = JSON.parseObject(json);
        JSONArray jsonArray = null;
        for (int i = 0; i < keyWord.length - 1; i++) {
            jsonObject = (JSONObject) jsonObject.get(keyWord[i]);
        }
        jsonArray = (JSONArray) jsonObject.getJSONArray(keyWord[keyWord.length - 1]);//得到对应的JSONArray对象
        for (int j = 0; j < jsonArray.size(); j++) {
            JSONObject jj = JSON.parseObject(jsonArray.get(j).toString());
            for (int i = 0; i < cols.length; i++) {
                if (0 == i)
                    colVals.append(jj.get(cols[i]).toString());
                else
                    colVals.append("," + jj.get(cols[i]).toString());
            }
        }
        return colVals.toString();
    }


    /**
     * xml转json
     *
     * @param element
     * @param json
     */
    public static void dom4jJson(Element element, JSONObject json) {
        //如果是属性
        for (Object o : element.attributes()) {
            Attribute attr = (Attribute) o;
            if (!isEmpty(attr.getValue())) {
                json.put("@" + attr.getName(), attr.getValue());
            }
        }
        List<Element> chdEl = element.elements();
        if (chdEl.isEmpty() && !isEmpty(element.getText())) {//如果没有子元素,只有一个值
            json.put(element.getName(), element.getText());
        }

        for (Element e : chdEl) {//有子元素
            if (!e.elements().isEmpty()) {//子元素也有子元素
                JSONObject chdjson = new JSONObject();
                dom4jJson(e, chdjson);
                Object o = json.get(e.getName());
                if (o != null) {
                    JSONArray jsona = null;
                    if (o instanceof JSONObject) {//如果此元素已存在,则转为jsonArray
                        JSONObject jsono = (JSONObject) o;
                        json.remove(e.getName());
                        jsona = new JSONArray();
                        jsona.add(jsono);
                        jsona.add(chdjson);
                    }
                    if (o instanceof JSONArray) {
                        jsona = (JSONArray) o;
                        jsona.add(chdjson);
                    }
                    json.put(e.getName(), jsona);
                } else {
                    if (!chdjson.isEmpty()) {
                        json.put(e.getName(), chdjson);
                    }
                }


            } else {//子元素没有子元素
                for (Object o : element.attributes()) {
                    Attribute attr = (Attribute) o;
                    if (!isEmpty(attr.getValue())) {
                        json.put("@" + attr.getName(), attr.getValue());
                    }
                }
                if (!e.getText().isEmpty()) {
                    json.put(e.getName(), e.getText());
                }
            }
        }
    }

    public static boolean isEmpty(String str) {

        if (str == null || str.trim().isEmpty() || "null".equals(str)) {
            return true;
        }
        return false;
    }
}
