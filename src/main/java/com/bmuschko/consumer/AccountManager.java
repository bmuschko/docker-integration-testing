package com.bmuschko.consumer;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.math.BigDecimal;

public interface AccountManager {

    BigDecimal credit(Long accountId, BigDecimal amount) throws IOException, ParseException;
}