package cn.huse.sdk.info.base;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限
 * 角色对应种权限
 * @author: huanxi
 * @date: 2019-06-19 19:43
 */
public class BaseAuthority {
    private List<Role> roles;
    private String[] all;
    public BaseAuthority() {
        this.all = new String[]{
                "sourceInfo"  //添加信息权限
        };
        this.roles=new ArrayList<Role>();
        this.roles.add(new Role("admin",this.all));
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String[] getAll() {
        return all;
    }

    public void setAll(String[] all) {
        this.all = all;
    }
}
