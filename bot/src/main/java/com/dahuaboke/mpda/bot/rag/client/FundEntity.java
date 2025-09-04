package com.dahuaboke.mpda.bot.rag.client;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Desc: 批量插入新核心接口数据库 映射对象
 * @Author：zhh
 * @Date：2025/9/04 17:32
 */
public class FundEntity {

    /**
     * 文本内容
     */
    private String id;

    /**
     * 文本内容
     */
    private String text;

    /**
     * 文本嵌入向量
     */
    private float[] embedding;

    /**
     * 页码
     */
    private int pageNumber;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 关键字
     */
    private List<String> excerptKeyWords;

    /**
     * 文件名关键字
     */
    private List<String> fileNameKeyWords;

    /**
     * 分数
     */
    private BigDecimal score;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float[] getEmbedding() {
        return embedding;
    }

    public void setEmbedding(float[] embedding) {
        this.embedding = embedding;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<String> getExcerptKeyWords() {
        return excerptKeyWords;
    }

    public void setExcerptKeyWords(List<String> excerptKeyWords) {
        this.excerptKeyWords = excerptKeyWords;
    }

    public List<String> getFileNameKeyWords() {
        return fileNameKeyWords;
    }

    public void setFileNameKeyWords(List<String> fileNameKeyWords) {
        this.fileNameKeyWords = fileNameKeyWords;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }
}
