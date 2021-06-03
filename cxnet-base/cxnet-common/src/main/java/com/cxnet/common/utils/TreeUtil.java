package com.cxnet.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Iterator;
import java.util.stream.Collectors;

/**
 * TreeUtil树结构工具类
 *
 * @author Chanyin
 */
public class TreeUtil {


    /**
     * list转树集合
     *
     * @param treeNodes 树节点
     * @param id        ID
     * @param pid       父ID
     */
    public JSONArray toTree(JSONArray treeNodes, String id, String pid) {
        JSONArray returnList = new JSONArray();
        for (Iterator<Object> iterator = treeNodes.iterator(); iterator.hasNext(); ) {
            JSONObject next = (JSONObject) iterator.next();
            if (next.containsKey(pid) && "0".equals(next.getString(pid))) {
                recursionFn(treeNodes, next, id, pid);
                returnList.add(next);
            }
        }
        if (returnList.isEmpty()) {
            returnList = treeNodes;
        }
        return returnList;
    }

    /**
     * 递归树节点
     *
     * @param treeNodes 树节点
     * @param next      下一节点
     * @param id        ID
     * @param pid       父ID
     */
    private void recursionFn(JSONArray treeNodes, JSONObject next, String id, String pid) {
        JSONArray childList = getChildList(treeNodes, next, id, pid);
        next.put("children", childList);
        for (int i = 0; i < childList.size(); i++) {
            JSONObject job = childList.getJSONObject(i);
            if (getChildList(treeNodes, job, id, pid).size() != 0) {
                Iterator<Object> it = childList.iterator();
                while (it.hasNext()) {
                    recursionFn(treeNodes, (JSONObject) it.next(), id, pid);
                }
            }
        }
    }

    /**
     * 获取树子集合
     *
     * @param treeNodes 树节点
     * @param next      下一节点
     * @param id        ID
     * @param pid       父ID
     */
    private JSONArray getChildList(JSONArray treeNodes, JSONObject next, String id, String pid) {
        JSONArray tList = new JSONArray();
        Iterator<Object> it = treeNodes.iterator();
        while (it.hasNext()) {
            JSONObject n = (JSONObject) it.next();
            if (n.containsKey(pid) && next.containsKey(id) && (StringUtils.isEmpty(n.getString(pid)) ? "0" : n.getString(pid)).equals(next.getString(id)) && !(StringUtils.isEmpty(n.getString(pid)) ? "0" : n.getString(pid)).equals(n.getString(id))) {
                tList.add(n);
            }

        }
        return tList;
    }

    /**
     * 递归树节点转下拉树集合
     *
     * @param jsonArray 树节点
     * @param id        ID
     * @param pid       父ID
     * @param label     标签名
     */
    public JSONArray toTreeSelect(JSONArray jsonArray, String icon, String id, String pid, String label, String isDataScope) {
        return jsonArray.stream().map(obj -> {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("icon", ((JSONObject) obj).get(icon));
            jsonObj.put("id", ((JSONObject) obj).get(id));
            jsonObj.put("pid", ((JSONObject) obj).get(pid));
            jsonObj.put("label", ((JSONObject) obj).get(label));
            jsonObj.put("isDataScope", ((JSONObject) obj).get(isDataScope));
            JSONArray children = (JSONArray) ((JSONObject) obj).get("children");
            if (children.size() != 0) {
                jsonObj.put("children", toTreeSelect(children, icon, id, pid, label, isDataScope));
            }
            return jsonObj;
        }).collect(Collectors.toCollection(JSONArray::new));
    }

    /**
     * 递归树节点转下拉树集合
     *
     * @param jsonArray 树节点
     * @param id        ID
     * @param pid       父ID
     * @param label     标签名
     */
    public JSONArray toTreeMob(JSONArray jsonArray, String id, String pid, String label, String isDataScope) {
        return jsonArray.stream().map(obj -> {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("id", ((JSONObject) obj).get(id));
            jsonObj.put("pid", ((JSONObject) obj).get(pid));
            jsonObj.put("label", ((JSONObject) obj).get(label));
            jsonObj.put("isDataScope", ((JSONObject) obj).get(isDataScope));
            JSONArray children = null != ((JSONObject) obj).get("children") ? (JSONArray) ((JSONObject) obj).get("children") : null;
            if (null != children && children.size() != 0) {
                jsonObj.put("children", toTreeMob(children, id, pid, label, isDataScope));
            }
            return jsonObj;
        }).collect(Collectors.toCollection(JSONArray::new));
    }

}

