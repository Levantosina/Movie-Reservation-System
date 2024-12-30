package com.movie.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;
import com.movie.common.TotalSeatsDTO;

import java.io.IOException;

/**
 * @author DMITRII LEVKIN on 30/12/2024
 * @project Movie-Reservation-System
 */
public class TotalSeatsDTODeserializer extends JsonDeserializer<TotalSeatsDTO> {
    @Override
    public TotalSeatsDTO deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        IntNode node = p.readValueAsTree();
        return new TotalSeatsDTO(node.intValue());
    }
}