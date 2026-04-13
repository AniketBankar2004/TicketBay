package com.aniket.TicketBay.exception;

import lombok.Builder;

import java.util.HashMap;
import java.util.Map;

@Builder
public class Error {
    Map<String,String> message;
}
