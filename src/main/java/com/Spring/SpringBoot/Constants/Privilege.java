package com.Spring.SpringBoot.Constants;

public enum Privilege {
    RESET_ANY_USER_PASSWORD(1l,"RESET_ANY_USER_PASSWORD"),
    ACCESS_ADMIN_PANEL(2l,"ACCESS_ADMIN_PANEL");

    private Long auth_id;
    private String auth_String;

    private Privilege(Long auth_id, String auth_String){
        this.auth_id = auth_id;
        this.auth_String = auth_String; 
    }

    public Long getAuthId(){
        return auth_id;
    }
    public String getAuthString(){
        return auth_String;
    }
    
}