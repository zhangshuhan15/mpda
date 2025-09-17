package com.dahuaboke.mpda.client.vector;

import org.junit.jupiter.api.Test;
import org.springframework.ai.vectorstore.filter.Filter;

import java.util.Map;

/**
 * @Desc:
 * @Author：zhh
 * @Date：2025/9/15 10:36
 */

public class EqAndFilterToMapConverterTest {

    @Test
    public void testEq() {
        Filter.Expression expression = new Filter.Expression(
                Filter.ExpressionType.EQ, new Filter.Key("excerpt_keywords"), new Filter.Value("净值为xxx")
        );

        // 使用转换器
        EqAndFilterToMapConverter converter = new EqAndFilterToMapConverter();
        Map<String, Object> result = converter.convertToMap(expression);

        System.out.println("转换结果: " + result);
    }

    @Test
    public void testEq02() {
        Filter.Expression expression = new Filter.Expression(
                Filter.ExpressionType.EQ, new Filter.Key("file_name"), new Filter.Value("易方达天天货币A")
        );

        // 使用转换器
        EqAndFilterToMapConverter converter = new EqAndFilterToMapConverter();
        Map<String, Object> result = converter.convertToMap(expression);

        System.out.println("转换结果: " + result);
    }


    @Test
    public void testEqAnd() {
        Filter.Expression expression = new Filter.Expression(
                Filter.ExpressionType.AND,
                new Filter.Expression(
                        Filter.ExpressionType.EQ, new Filter.Key("file_name"), new Filter.Value("易方达天天货币A")
                ),
                new Filter.Expression(
                        Filter.ExpressionType.EQ, new Filter.Key("excerpt_keywords"), new Filter.Value("基金经理")
                )
        );

        // 使用转换器
        EqAndFilterToMapConverter converter = new EqAndFilterToMapConverter();
        Map<String, Object> result = converter.convertToMap(expression);

        System.out.println("转换结果: " + result);
    }

    @Test
    public void testEqAnd02() {
        Filter.Expression expression = new Filter.Expression(
                Filter.ExpressionType.AND,
                new Filter.Expression(
                        Filter.ExpressionType.EQ, new Filter.Key("file_name"), new Filter.Value("易方达天天货币A")
                ),
                new Filter.Expression(
                        Filter.ExpressionType.EQ, new Filter.Key("page_number"), new Filter.Value(1)
                )
        );

        // 使用转换器
        EqAndFilterToMapConverter converter = new EqAndFilterToMapConverter();
        Map<String, Object> result = converter.convertToMap(expression);

        System.out.println("转换结果: " + result);
    }


}
