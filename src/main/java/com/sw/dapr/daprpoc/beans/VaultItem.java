package com.sw.dapr.daprpoc.beans;

public class VaultItem
{
    private String vault;
    private String key;
    private String password;

    public String getVault ()
    {
        return vault;
    }

    public void setVault (String vault)
    {
        this.vault = vault;
    }

    public String getKey ()
    {
        return key;
    }

    public void setKey (String key)
    {
        this.key = key;
    }

    public String getPassword ()
    {
        return password;
    }

    public void setPassword (String password)
    {
        this.password = password;
    }

}