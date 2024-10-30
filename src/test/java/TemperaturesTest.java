import com.zeydie.telegram.bot.monitor.TemperatureMonitorBot;
import com.zeydie.telegram.bot.monitor.api.modules.computer.IComputer;
import com.zeydie.telegram.bot.monitor.api.v1.data.ComputerData;
import com.zeydie.telegram.bot.monitor.api.v1.data.TemperatureData;
import com.zeydie.telegrambot.configs.ConfigStore;
import com.zeydie.telegrambot.configs.data.BotConfig;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;

import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TemperaturesTest {
    private static BotConfig.TestConfig testConfig;

    private static IComputer computer;
    private static final ComputerData computerData = new ComputerData(
            "test",
            new TemperatureData(),
            new TemperatureData(),
            0
    );

    @BeforeAll
    public static void init() {
        TemperatureMonitorBot.main(List.of("-p", "3666").toArray(new String[]{}));

        computer = TemperatureMonitorBot.getInstance().getComputerModule();

        testConfig = ConfigStore.getBotConfig().getTestConfig();
    }

    @SneakyThrows
    @Test
    @Order(0)
    public void addComputerData() {
        computer.addComputerData(computerData);

        Assertions.assertNotNull(computer.getComputerData(computerData));
    }

    @SneakyThrows
    @Test
    @Order(1)
    public void removeComputerData() {
        computer.removeComputerData(computerData);

        Assertions.assertNull(computer.getComputerData(computerData));
    }
}