package com.jgkj.parentscycle.bean;

/**
 * Created by chen on 16/8/25.
 */
public class ClassesAndTeachersListItemInfo {
    boolean isQuery = false;
    String classadviser;
    String classid;
    String classname;
    String createtime;
    String schoolid;
    String updatetime;
    String tmpinfoid;
    boolean isSelected = false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isQuery() {
        return isQuery;
    }

    public void setQuery(boolean query) {
        isQuery = query;
    }

    public String getTmpinfoid() {
        return tmpinfoid;
    }

    public void setTmpinfoid(String tmpinfoid) {
        this.tmpinfoid = tmpinfoid;
    }

    public String getClassadviser() {
        return classadviser;
    }

    public void setClassadviser(String classadviser) {
        this.classadviser = classadviser;
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getSchoolid() {
        return schoolid;
    }

    public void setSchoolid(String schoolid) {
        this.schoolid = schoolid;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }
}
