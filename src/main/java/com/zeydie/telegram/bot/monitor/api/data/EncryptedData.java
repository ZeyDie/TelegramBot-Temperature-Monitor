package com.zeydie.telegram.bot.monitor.api.data;

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