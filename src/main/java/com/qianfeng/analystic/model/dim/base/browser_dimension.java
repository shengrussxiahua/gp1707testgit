package com.qianfeng.analystic.model.dim.base;

import com.qianfeng.common.GlobalConstants;
import org.apache.commons.lang.StringUtils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//browser_dimension实体类
public class browser_dimension extends BaseDimension {
    private int id;
    private String browser_name;//浏览器名称
    private String browser_version;//浏览器版本号

    public browser_dimension() {
    }

    public browser_dimension(String browser_name, String browser_version) {
        this.browser_name = browser_name;
        this.browser_version = browser_version;
    }

    public browser_dimension(int id, String browser_name, String browser_version) {
        this.id = id;
        this.browser_name = browser_name;
        this.browser_version = browser_version;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrowser_name() {
        return browser_name;
    }

    public void setBrowser_name(String browser_name) {
        this.browser_name = browser_name;
    }

    public String getBrowser_version() {
        return browser_version;
    }

    public void setBrowser_version(String browser_version) {
        this.browser_version = browser_version;
    }

    @Override
    public String toString() {
        return "browser_dimension{" +
                "id=" + id +
                ", browser_name='" + browser_name + '\'' +
                ", browser_version='" + browser_version + '\'' +
                '}';
    }

    public static List<browser_dimension> buildList(String browser_name, String browser_version){
        if(StringUtils.isEmpty(browser_name)){
            browser_name = browser_version = GlobalConstants.DEFAULT_VALUE;
        }
        if(StringUtils.isEmpty(browser_version)){
            browser_version = GlobalConstants.DEFAULT_VALUE;
        }
        List<browser_dimension> li = new ArrayList<>();
        li.add(new browser_dimension(browser_name,browser_version));
        li.add(new browser_dimension(browser_name,GlobalConstants.ALL_OF_VALUE));
        return li;
    }

    @Override
    public int compareTo(BaseDimension o) {
        browser_dimension other = (browser_dimension) o;
        if(this == other){
            return 0;
        }
        int tmp = this.id - other.id;
        if(tmp != 0){
            return tmp;
        }
        tmp = this.browser_name.compareTo(other.browser_name);
        if(tmp != 0){
            return tmp;
        }
        return this.browser_version.compareTo(other.browser_version);
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(this.id);
        dataOutput.writeUTF(this.browser_name);
        dataOutput.writeUTF(this.browser_version);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.id = dataInput.readInt();
        this.browser_name = dataInput.readUTF();
        this.browser_version = dataInput.readUTF();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        browser_dimension browser = (browser_dimension) o;

        if (id != browser.id) return false;
        if (browser_name != null ? !browser_name.equals(browser.browser_name) : browser.browser_name != null)
            return false;
        return browser_version != null ? browser_version.equals(browser.browser_version) : browser.browser_version == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (browser_name != null ? browser_name.hashCode() : 0);
        result = 31 * result + (browser_version != null ? browser_version.hashCode() : 0);
        return result;
    }
}
