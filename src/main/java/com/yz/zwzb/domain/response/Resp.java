package com.yz.zwzb.domain.response;

import java.util.HashMap;

public class Resp
{

    public Resp()
    {
    }

    public Resp(String type){
        this.type = type;
    }
    String type;

    HashMap<String,Object> msg = new HashMap<>();

    public Resp fill(String key,Object value){
        msg.put(key,value);
        return this;
    }
    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public HashMap<String, Object> getMsg()
    {
        return msg;
    }

    public void setMsg(HashMap<String, Object> msg)
    {
        this.msg = msg;
    }
}
