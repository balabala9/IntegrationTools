package org.util.reflect;

import java.util.List;

/**
 * Created by li on 17-6-19.
 */
public class FunBean {
    private String funName;
    private String paraTypeNum;
    private List<Class<?>> parameterTypeList;

    public String getFunName() {
        return funName;
    }

    public void setFunName(String funName) {
        this.funName = funName;
    }

    public String getParaTypeNum() {
        return paraTypeNum;
    }

    public void setParaTypeNum(String paraTypeNum) {
        this.paraTypeNum = paraTypeNum;
    }

    public List<Class<?>> getParameterTypeList() {
        return parameterTypeList;
    }

    public void setParameterTypeList(List<Class<?>> parameterTypeList) {
        this.parameterTypeList = parameterTypeList;
    }
}
