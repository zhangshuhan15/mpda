package com.dahuaboke.mpda.agent.tools;


import com.dahuaboke.mpda.agent.exception.MpdaIllegalArgumentException;
import com.dahuaboke.mpda.tools.product.ProductToolHandler;
import com.dahuaboke.mpda.tools.product.dto.NetValReq;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.BiFunction;

/**
 * auth: dahua
 * time: 2025/8/22 10:53
 */
public abstract class AbstractBaseTool<I> implements BiFunction<I, ToolContext, ToolResult> {

    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    @Lazy
    protected ProductToolHandler productToolHandler;

    @Override
    public ToolResult apply(I input, ToolContext toolContext) {
        return execute(objectMapper.convertValue(input, getInputType()));
    }

    public String getName() {
        String simpleName = getClass().getSimpleName();
        return Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
    }

    abstract public String getDescription();

    abstract public String getParameters();

    abstract public Class<I> getInputType();

    abstract public ToolResult execute(I input);

    protected String getJsonSchema(Class<I> clz, String... required) {
        try {
            JsonSchemaGenerator schemaGenerator = new JsonSchemaGenerator(objectMapper);
            JsonSchema schema = schemaGenerator.generateSchema(clz);
            JsonNode schemaNode = objectMapper.valueToTree(schema);
            if (required != null && required.length > 0) {
                ArrayNode requiredArray = objectMapper.createArrayNode();
                for (String s : required) {
                    requiredArray.add(s);
                }
                ((ObjectNode) schemaNode).set("required", requiredArray);
            }
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(schemaNode);
        } catch (JsonProcessingException e) {
            throw new MpdaIllegalArgumentException(e);
        }
    }

    protected String getMaxNetval(String productNo) {
        return productToolHandler.maxNetval(new NetValReq(productNo));
    }

    protected String getYearRita(String productNo) {
        return productToolHandler.yearRita(new NetValReq(productNo));
    }

    protected String getYearRita(String productNo, LocalDateTime begTime, LocalDateTime endTime) {
        DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");
        return productToolHandler.yearRita(new NetValReq(productNo, begTime.format(yyyyMMdd), endTime.format(yyyyMMdd)));
    }
}
