package com.cxnet.common.utils.tree;

import com.cxnet.common.utils.StringUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Zone implements Serializable {

    private static final long serialVersionUID = -1528175948190672440L;
    private String id;
    private String code;
    private String name;
    private String pid;
    private String pCode;
    private String label;
    private String type;
    private List<Zone> children;
    private String lev;

    public String getPid() {
        return StringUtils.isEmpty(pid) ? "" : pid;
    }

    public String getPCode() {
        return StringUtils.isEmpty(pCode) ? "" : pCode;
    }

    public Zone(String id, String name, String pid) {
        this.id = id;
        this.name = name;
        this.pid = pid;
    }

    public Zone(String id, String code, String name, String pid, String pCode, String label, String type, List<Zone> children, String lev) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.pid = pid;
        this.pCode = pCode;
        this.label = label;
        this.type = type;
        this.children = children;
        this.lev = lev;
    }

    public void addChildren(Zone zone) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(zone);
    }
}