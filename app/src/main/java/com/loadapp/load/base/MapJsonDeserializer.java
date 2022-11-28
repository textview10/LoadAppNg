package com.loadapp.load.base;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.LinkedTreeMap;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

public class MapJsonDeserializer implements JsonDeserializer<LinkedTreeMap<String, Object>> {

    @Override
    public LinkedTreeMap<String, Object> deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        LinkedTreeMap<String, Object> treeMap = new LinkedTreeMap<>();
        JsonObject jsonObject = json.getAsJsonObject();
        Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
        for (Map.Entry<String, JsonElement> entry : entrySet) {
            Object ot = entry.getValue();
            if(ot instanceof JsonPrimitive){
                JsonPrimitive ot1 = (JsonPrimitive) ot;
                if (ot1.isNumber()){
                    double dbNum = ot1.getAsDouble();
                    long lngNum = (long) dbNum;

                    // 数字超过long的最大值，返回浮点类型
                    if (dbNum > Long.MAX_VALUE) {
                        treeMap.put(entry.getKey(), dbNum);
                    }
                    // 判断数字是否为整数值
                    if (dbNum == lngNum) {
                        treeMap.put(entry.getKey(), lngNum);
                    } else {
                        treeMap.put(entry.getKey(),dbNum);
                    }
                }else {
                    treeMap.put(entry.getKey(),ot1);
                }
            }else{
                treeMap.put(entry.getKey(), ot);
            }
        }
        return treeMap;
    }
}
