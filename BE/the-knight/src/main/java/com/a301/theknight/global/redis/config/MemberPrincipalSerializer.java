package com.a301.theknight.global.redis.config;

import com.a301.theknight.domain.auth.model.MemberPrincipal;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;

import java.io.IOException;

public class MemberPrincipalSerializer extends JsonDeserializer<MemberPrincipal> {

    @Override
    public MemberPrincipal deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        System.out.println("\n MemberPrinciple Deserialize Start");
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode jsonNode = mapper.readTree(jp);

        MemberPrincipal memberPrincipal =  new MemberPrincipal(
                readJsonNode(jsonNode, "memberId").asLong(),
                readJsonNode(jsonNode, "email").asText());
        System.out.println(" MemberPrinciple Deserialize END\n");
        return memberPrincipal;
    }

    private JsonNode readJsonNode(JsonNode jsonNode, String field) {
        System.out.println(" JsonNode field: " + field);
        return jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance();
    }
}