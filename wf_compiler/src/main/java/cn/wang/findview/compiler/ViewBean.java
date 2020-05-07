package cn.wang.findview.compiler;

import javax.lang.model.element.Element;

/**
 * Created to :
 *
 * @author WANG
 * @date 2020/5/7
 */
public class ViewBean {


    private String fieldName;
    private Id id;

    public ViewBean(String fieldName, Id id) {
        this.fieldName = fieldName;
        this.id = id;
    }

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof ViewBean){
            return ((ViewBean) o).fieldName == fieldName;
        }
        return super.equals(o);
    }
}
