package com.lidashuang.jt.jtt808.config;

import java.util.ArrayList;
import java.util.List;

/**
 * JT808 配置文件信息
 * @author lidashuang
 * @version 1.0
 */
public final class Jtt808Config {

    /**
     * 配置文件模型
     */
    public static class Model {
        private int index;
        private int messageId;
        private String content;

        public Model() { }

        public Model(int index, int messageId, String content) {
            this.index = index;
            this.messageId = messageId;
            this.content = content;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getMessageId() {
            return messageId;
        }

        public void setMessageId(int messageId) {
            this.messageId = messageId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    /**
     * 配置文件列表
     */
    private final static List<Model> REFERENCE_TABLE = new ArrayList<>();

    static {
        // 初始化配置文件内容
        
    }

    /**
     * 获取配置文件列表
     * @return 配置文件列表
     */
    public static List<Model> getReferenceTableData() {
        return REFERENCE_TABLE;
    }

    /**
     * 查询配置文件数据
     * @param content 查询的内容
     * @return 配置文件列表
     */
    public static List<Model> getReferenceTableData(String content) {
        final List<Model> result = new ArrayList<>();
        for (final Model model : REFERENCE_TABLE) {
            if (model.getContent().contains(content)
                    || String.valueOf(model.getIndex()).contains(content)
                    || String.valueOf(model.getMessageId()).contains(content)) {
                result.add(model);
            }
        }
        return result;
    }

    /**
     * 删除配置文件数据
     * @param model 配置文件的对象
     */
    public synchronized static void delReferenceTableData(Model model) {
        REFERENCE_TABLE.remove(model);
    }

    /**
     * 添加配置文件数据
     * @param model 配置文件的对象
     */
    public synchronized static void setReferenceTableData(Model model) {
        REFERENCE_TABLE.add(model);
    }

}
