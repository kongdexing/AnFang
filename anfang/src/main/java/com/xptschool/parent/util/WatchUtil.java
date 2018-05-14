package com.xptschool.parent.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WatchUtil {

    //1 => ‘爸爸’, 2 => ‘妈妈’, 3 => ‘爷爷’, 4 => ‘奶奶’, 5 => ‘外公’, 6 => ‘外婆’,7=>’哥哥’,8=>’姐姐’,9=>’叔叔’,10=>’舅舅’,11=>’阿姨’,12=>’婶婶’, 0 => ‘其它’

    public static List<WatchRelation> getRelationList() {
        List<WatchRelation> relations = new ArrayList<>();
        relations.add(new WatchRelation("1", "爸爸"));
        relations.add(new WatchRelation("2", "妈妈"));
        relations.add(new WatchRelation("3", "爷爷"));
        relations.add(new WatchRelation("4", "奶奶"));
        relations.add(new WatchRelation("5", "外公"));
        relations.add(new WatchRelation("6", "外婆"));
        relations.add(new WatchRelation("7", "哥哥"));
        relations.add(new WatchRelation("8", "姐姐"));
        relations.add(new WatchRelation("9", "叔叔"));
        relations.add(new WatchRelation("10", "舅舅"));
        relations.add(new WatchRelation("11", "阿姨"));
        relations.add(new WatchRelation("12", "婶婶"));
        relations.add(new WatchRelation("0", "其它"));
        return relations;
    }

    public static String getRelationByKey(String key) {
        List<WatchRelation> relations = getRelationList();
        for (int i = 0; i < relations.size(); i++) {
            WatchRelation relation = relations.get(i);
            if (relation.getKey().equals(key)) {
                return relation.getValue();
            }
        }
        return "其它";
    }

    public static class WatchRelation {
        private String key;
        private String value;

        public WatchRelation(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

}
