package com.dahuaboke.mpda.client.vector;

import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.converter.AbstractFilterExpressionConverter;

import java.util.HashMap;
import java.util.Map;

/**
 * @Desc: 将spring ai的过滤表达式转化成map,目前只支持eq 和 and
 * @Author：zhh
 * @Date：2025/9/15 10:25
 */
public class EqAndFilterToMapConverter extends AbstractFilterExpressionConverter {

    private final Map<String, Object> resultMap;
    private String currentKey;

    public EqAndFilterToMapConverter() {
        this.resultMap = new HashMap<>();
    }

    /**
     * 获取转换后的结果Map
     */
    public Map<String, Object> getResultMap() {
        return resultMap;
    }

    /**
     * 清空当前状态（用于多次使用同一个转换器实例）
     */
    public void reset() {
        resultMap.clear();
        currentKey = null;
    }

    /**
     * 转换表达式并返回Map
     */
    public Map<String, Object> convertToMap(Filter.Expression expression) {
        reset();
        // 使用父类的convertOperand方法开始转换过程
        convertOperand(expression, new StringBuilder());
        return resultMap;
    }

    /**
     * 实现父类的convertExpression方法
     * 注意：此方法返回String，但我们主要使用convertToMap方法
     */
    @Override
    public String convertExpression(Filter.Expression expression) {
        return convertToMap(expression).toString();
    }

    /**
     * 处理表达式 - 核心方法
     * 只处理EQ和AND表达式
     */
    @Override
    protected void doExpression(Filter.Expression expression, StringBuilder context) {
        switch (expression.type()) {
            case EQ:
                // 处理相等表达式：key == value
                processEqualityExpression(expression);
                break;
            case AND:
                // 处理AND表达式：递归处理左右操作数
                processAndExpression(expression, context);
                break;
            default:
                // 忽略其他类型的表达式
                break;
        }
    }

    /**
     * 处理相等表达式
     */
    private void processEqualityExpression(Filter.Expression expression) {
        // 处理左侧（应该是Key）
        if (expression.left() instanceof Filter.Key) {
            Filter.Key key = (Filter.Key) expression.left();
            currentKey = key.key();
        }

        // 处理右侧（应该是Value）
        if (expression.right() instanceof Filter.Value) {
            Filter.Value value = (Filter.Value) expression.right();
            if (currentKey != null) {
                resultMap.put(currentKey, value.value());
                currentKey = null;
            }
        }
    }

    /**
     * 处理AND表达式
     */
    private void processAndExpression(Filter.Expression expression, StringBuilder context) {
        // 递归处理左侧表达式
        convertOperand(expression.left(), context);
        // 递归处理右侧表达式
        convertOperand(expression.right(), context);
    }

    /**
     * 处理Key
     * 对于简单的EQ表达式，这个方法是必需的
     */
    @Override
    protected void doKey(Filter.Key filterKey, StringBuilder context) {
        currentKey = filterKey.key();
    }

    /**
     * 处理Value
     * 对于简单的EQ表达式，这个方法是必需的
     */
    @Override
    protected void doValue(Filter.Value filterValue, StringBuilder context) {
        if (currentKey != null) {
            resultMap.put(currentKey, filterValue.value());
            currentKey = null;
        }
    }

    /**
     * 处理NOT表达式 - 忽略
     */
    @Override
    protected void doNot(Filter.Expression expression, StringBuilder context) {

    }


}
