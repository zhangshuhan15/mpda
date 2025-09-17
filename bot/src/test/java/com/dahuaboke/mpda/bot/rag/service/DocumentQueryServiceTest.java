package com.dahuaboke.mpda.bot.rag.service;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @Desc:
 * @Author：zhh
 * @Date：2025/9/15 16:43
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class DocumentQueryServiceTest {

    @Autowired
    private DocumentQueryService documentQueryService;

    @Test
    public void testProcessProduct(){
        documentQueryService.processProducts(Map.of("001982","富国收益宝交易型货币B"));
    }



}
