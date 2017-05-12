package vehiclesurvey.utils;

import org.junit.*;
import org.junit.rules.ExpectedException;
import vehiclesurvey.Models.FactItem;
import vehiclesurvey.Models.SurveyFormat;
import vehiclesurvey.constants.AppConstants;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Abby on 6/05/2017.
 */
public class FileUtilsTest {

    private FileUtils fileUtils;
    private String dummyFile;
    private String validFile;
    private String regex;
    @Before
    public void setUp() throws Exception {
        fileUtils = new FileUtils();
        dummyFile = "src/test/resources/data.txt";
        validFile = "src/test/resources/validInputFile.txt";
        regex = AppConstants.INPUT_VALIDATION_REGEX;
    }

    @After
    public void tearDown() throws Exception {
        fileUtils = null;
    }

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldReadTheFileFromGivenPath() throws Exception {
        Reader reader = fileUtils.fileReader(dummyFile);
        Assert.assertEquals(true, new BufferedReader(reader).lines().count() >=0l);
    }

    @Test
    public void shouldThrowExceptionWhileReadingGivenFilePathIfInvalidPath() throws Exception {
        try {
            Reader reader = fileUtils.fileReader("invalid-path");
            Assert.assertTrue(false);
        }
        catch(Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void shouldReadTheFileFromResource() throws Exception {
        Reader reader = fileUtils.resourceReader(AppConstants.DEFAULT_INPUT);
        Assert.assertEquals(true, new BufferedReader(reader).lines().count() >=0);
    }

    @Test
    public void shouldThrowExceptionForInvalidResourceFile() throws Exception {
        try {
            Reader reader = fileUtils.resourceReader("invalid-resource");
            Assert.assertTrue(false);
        }
        catch(Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void shouldReturnValidatedInputFromInputStream() throws Exception {
        String input = "A638379\nB638382\nA638520\nB638523";
        StringReader reader = new StringReader(input);
        List<String> expectedList = new ArrayList<>();
        expectedList = Arrays.asList(input.split("\n"));
        Assert.assertArrayEquals(expectedList.toArray(),fileUtils.validateInputStream(reader, regex).toArray());
    }

    @Test
    public void shouldConvertLowerCaseInputStreamToUpperCase() throws Exception {
        String input = "a638379\nb638382\nA638520\nB638523";
        StringReader reader = new StringReader(input);
        List<String> expectedList = Arrays.stream(input.split("\n"))
                .map(String::toUpperCase)
                .collect(Collectors.toList());
        Assert.assertArrayEquals(expectedList.toArray(),fileUtils.validateInputStream(reader, regex).toArray());
    }

    @Test
    public void shouldThrowExceptionIfInputStreamNotValid() throws Exception {
        String input1 = "A638379\nD638382\nA638520\nB638523";
        String input2 = "A638379\nA638382\n638520\nB638523";
        String input3 = "A638379\nc38523";
        String input4 = "";
        String input5 = "A638379\nA638382\n638520\nB638523S";

        try {
            fileUtils.validateInputStream(new StringReader(input1), regex);
            Assert.assertTrue(false);
        } catch (Exception e) {
            Assert.assertTrue(true);
        }

        try {
            fileUtils.validateInputStream(new StringReader(input2), regex);
            Assert.assertTrue(false);
        } catch (Exception e) {
            Assert.assertTrue(true);
        }

        try {
            fileUtils.validateInputStream(new StringReader(input3), regex);
            Assert.assertTrue(false);
        } catch (Exception e) {
            Assert.assertTrue(true);
        }

        try {
            fileUtils.validateInputStream(new StringReader(input4), regex);
            Assert.assertTrue(false);
        } catch (Exception e) {
            Assert.assertTrue(true);
        }

        try {
            fileUtils.validateInputStream(new StringReader(input5), regex);
            Assert.assertTrue(false);
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void shouldThrowExceptionWhenFormattingEmptyFacts() throws Exception {
        try {
            String result = fileUtils.convertFactToFileFormat(new LinkedHashMap<>(),
              new SurveyFormat("Count Distribution", "Minutes Session",
                "Direction", "Start Time", "End Time"));
            Assert.assertEquals("", result);
        }
        catch(Exception e){
            Assert.assertTrue(true);
        }
    }

    private Map<Integer, List<FactItem>> createSurveyFactStub() throws Exception {
        Map<Integer, List<FactItem>> stub = new LinkedHashMap<>();
        List<FactItem> factItems = new ArrayList<>();
        factItems.add(new FactItem(10d, new Date(1), new Date(10)));
        factItems.add(new FactItem(10d, new Date(12), new Date(15)));
        stub.put(10, factItems);
        stub.put(20, factItems);
        stub.put(30, factItems);

        return stub;
    }

    @Test
    public void shouldWriteTheSurveyDataToString() throws Exception {
        String result = fileUtils.convertFactToFileFormat(createSurveyFactStub(),
          new SurveyFormat("Count Distribution", "Minutes Session",
            "North", "Start Time", "End Time"));

        String expected = "Count Distribution going North:: \n" +
          "\t10\tMinutes Session\n" +
            "\t\t00:00:00 - 00:00:00::\t10.0\n" +
            "\t\t00:00:00 - 00:00:00::\t10.0\n" +
          "\t20\tMinutes Session\n" +
            "\t\t00:00:00 - 00:00:00::\t10.0\n" +
            "\t\t00:00:00 - 00:00:00::\t10.0\n" +
          "\t30\tMinutes Session\n" +
            "\t\t00:00:00 - 00:00:00::\t10.0\n" +
            "\t\t00:00:00 - 00:00:00::\t10.0\n";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void shouldCreateTextFilePathFromFileName() throws Exception{
        String result = fileUtils.getFilePath("test");
        Assert.assertEquals("out/survey/test.txt", result);
    }

    @Test
    public void shouldWriteStringDataToFile() {
        try {
            fileUtils.writeTextFile("test.txt", "test data");
        }
        catch(Exception e) {
            Assert.fail();
        }
    }

}