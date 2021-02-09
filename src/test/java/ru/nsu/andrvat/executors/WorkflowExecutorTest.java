package ru.nsu.andrvat.executors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

class WorkflowExecutorTest {
    private static final Map<String, Integer> blockIdsByBlockNames = new LinkedHashMap<>() {{
        put("dump", 1);
        put("grep", 2);
        put("readfile", 3);
        put("replace", 4);
        put("sort", 5);
        put("writefile", 6);
    }};

    private static final ArrayList<String> specifiedWorkflowConfigSettings = new ArrayList<>(List.of(
            "desc",
            "1 = dump dumpText.txt",
            "2 = grep test",
            "3 = readfile inputText.txt",
            "4 = replace test winter!",
            "5 = sort",
            "6 = writefile output.txt",
            "csed"));

    private final ArrayList<String> specifiedOperatingText = new ArrayList<>(List.of(
            "This is the first line of specified operating text",
            "Aaaabb, test grep",
            "Bbbbaa, testing grep",
            "Ccccab, test GREEEP!",
            "This is the last line of specified operating text and test at all ..."));

    private final WorkflowExecutor workflowExecutor = new WorkflowExecutor();

    private String buildCorrectWorkflowConfigSettingsWithExecuteSequence(ArrayList<String> configs, String blockName) {
        StringBuilder inOneStringSpecifiedWorkflowConfigSettings = new StringBuilder();
        for (String currentConfigLine : configs) {
            inOneStringSpecifiedWorkflowConfigSettings.append(currentConfigLine);
            inOneStringSpecifiedWorkflowConfigSettings.append("\n");
        }
        return inOneStringSpecifiedWorkflowConfigSettings.toString() +
                blockIdsByBlockNames.get("readfile") + " -> " +
                blockIdsByBlockNames.get(blockName);
    }

    public ArrayList<String> cloneFrom(ArrayList<String> list) {
        return (ArrayList<String>) list.clone();
    }

    @Test
    public void testSortFunction() {
        ArrayList<String> sortedSpecifiedOperatingText = specifiedOperatingText;
        Collections.sort(sortedSpecifiedOperatingText);

        workflowExecutor.parametrizedRun(new ByteArrayInputStream(
                buildCorrectWorkflowConfigSettingsWithExecuteSequence
                        (specifiedWorkflowConfigSettings, "sort")
                        .getBytes(StandardCharsets.UTF_8)));
        Assertions.assertEquals(sortedSpecifiedOperatingText, workflowExecutor.getContextOperatingText());
    }

    @Test
    public void testGrepFunction() {
        ArrayList<String> sortedSpecifiedOperatingText = specifiedOperatingText;
        sortedSpecifiedOperatingText.removeIf(currentLine -> !currentLine.contains("test"));

        workflowExecutor.parametrizedRun(new ByteArrayInputStream(
                buildCorrectWorkflowConfigSettingsWithExecuteSequence
                        (specifiedWorkflowConfigSettings, "grep")
                        .getBytes(StandardCharsets.UTF_8)));
        Assertions.assertEquals(sortedSpecifiedOperatingText, workflowExecutor.getContextOperatingText());
    }

    @Test
    public void testReadfileFunction() {
        workflowExecutor.parametrizedRun(new ByteArrayInputStream(
                buildCorrectWorkflowConfigSettingsWithExecuteSequence
                        (specifiedWorkflowConfigSettings, "readfile")
                        .getBytes(StandardCharsets.UTF_8)));
        Assertions.assertEquals(specifiedOperatingText, workflowExecutor.getContextOperatingText());
    }

    @Test
    public void testReplaceFunction() {
        ArrayList<String> forReplaceSpecifiedOperatingText = new ArrayList<>();
        for (String textLine : specifiedOperatingText) {
            forReplaceSpecifiedOperatingText.add(textLine.replaceAll("test", "winter!"));
        }

        workflowExecutor.parametrizedRun(new ByteArrayInputStream(
                buildCorrectWorkflowConfigSettingsWithExecuteSequence
                        (specifiedWorkflowConfigSettings, "replace")
                        .getBytes(StandardCharsets.UTF_8)));
        Assertions.assertEquals(forReplaceSpecifiedOperatingText, workflowExecutor.getContextOperatingText());
    }

    @Test
    public void testDumpFunctionForExceededArgumentsNumber() {
        ArrayList<String> brokenWorkflowConfigSettings = cloneFrom(specifiedWorkflowConfigSettings);
        brokenWorkflowConfigSettings.set(blockIdsByBlockNames.get("dump"), "2 = grep test hahah");

        Assertions.assertThrows(
                RuntimeException.class, () -> {
                    workflowExecutor.parametrizedRun(new ByteArrayInputStream(
                            buildCorrectWorkflowConfigSettingsWithExecuteSequence
                                    (brokenWorkflowConfigSettings, "dump")
                                    .getBytes(StandardCharsets.UTF_8)));
                });

    }

    @Test
    public void testReadfileFunctionForIncorrectSignatureConfigLine() {
        ArrayList<String> brokenWorkflowConfigSettings = cloneFrom(specifiedWorkflowConfigSettings);
        brokenWorkflowConfigSettings.set(blockIdsByBlockNames.get("readfile"), "3 ~ readfile inputText.txt");

        Assertions.assertThrows(
                RuntimeException.class, () -> {
                    workflowExecutor.parametrizedRun(new ByteArrayInputStream(
                            buildCorrectWorkflowConfigSettingsWithExecuteSequence
                                    (brokenWorkflowConfigSettings, "readfile")
                                    .getBytes(StandardCharsets.UTF_8)));
                });

    }
}