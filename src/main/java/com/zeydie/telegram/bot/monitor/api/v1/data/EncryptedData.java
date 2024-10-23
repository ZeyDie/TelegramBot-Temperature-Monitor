package com.zeydie.telegram.bot.monitor.api.v1.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public final class EncryptedData {
    private String name;
    private long chatId;
}