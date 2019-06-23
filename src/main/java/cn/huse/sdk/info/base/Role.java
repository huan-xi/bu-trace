package cn.huse.sdk.info.base;

/**
 * @author: huanxi
 * @date: 2019-06-19 19:50
 */
public class Role {
    private String roleName;
    private String[] authorities;

    public Role(String roleName, String[] all) {
        this.roleName = roleName;
        this.authorities = all;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String[] getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String[] authorities) {
        this.authorities = authorities;
    }
}
