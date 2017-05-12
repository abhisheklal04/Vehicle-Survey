package vehiclesurvey;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import vehiclesurvey.constants.AnalyserTypes;
import vehiclesurvey.constants.AppConstants;
import vehiclesurvey.services.SurveyService;
import vehiclesurvey.utils.FileUtils;
import vehiclesurvey.utils.VehicleParser;

import java.io.Reader;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


/**
 * Created by Abby on 5/05/2017.
 */
public class AppTest {

    App app;

    String[] args;

    FileUtils fileUtils;

    Reader reader;

    String dummyFile;

    VehicleParser vehicleParser;

    SurveyService surveyService;

    @Before
    public void setUp() throws Exception {
        fileUtils = mock(FileUtils.class);
        vehicleParser = mock(VehicleParser.class);
        reader = mock(Reader.class);
        surveyService = mock(SurveyService.class);
        app = new App(fileUtils, vehicleParser, surveyService);
        dummyFile = "src/test/resources/data.txt";
        args = new String[]{dummyFile};

    }

    @After
    public void tearDown() throws Exception {
        app = null;
        args = null;
    }

    @Test
    public void shouldReadFileFromCommandArgs() throws Exception {
        when(fileUtils.fileReader(args[0])).thenReturn(reader);
        app.processArgs(args);
        verify(fileUtils).fileReader(Mockito.anyString());
        verify(fileUtils, never()).resourceReader(Mockito.anyString());
    }

    @Test
    public void shouldReadDefaultFileWhenNoProcessArgumentPassed() throws Exception {
        when(fileUtils.resourceReader(AppConstants.DEFAULT_INPUT)).thenReturn(reader);
        app.processArgs(new String[]{});
        verify(fileUtils, never()).fileReader(any());
        verify(fileUtils).resourceReader(any());
    }

    @Test
    public void shouldValidateTheFileData() throws Exception {
        when(fileUtils.fileReader(any())).thenReturn(reader);
        when(fileUtils.validateInputStream(reader, AppConstants.INPUT_VALIDATION_REGEX)).thenReturn(any());
        app.processArgs(args);
        verify(fileUtils).validateInputStream(any(), any());
    }

    @Test
    public void shouldConvertVehicleReadingsToVehicleList() throws Exception {
        when(fileUtils.fileReader(eq(args[0]))).thenReturn(reader);
        when(fileUtils.validateInputStream(reader, AppConstants.INPUT_VALIDATION_REGEX)).thenReturn(any());
        app.processArgs(args);
        verify(vehicleParser).setVehicleReadings(any());
        verify(vehicleParser).generateVehicleData();
    }

    @Test
    public void shouldNotGenerateVehicleDataWhenInvalidFilePath() throws Exception {
        doThrow(mock(Exception.class)).when(fileUtils).fileReader(any());
        app.processArgs(args);
        verify(vehicleParser, never()).setVehicleReadings(any());
        verify(vehicleParser, never()).generateVehicleData();
    }

    @Test
    public void shouldFeedVehicleAndIntervalListToSurveyService() throws Exception{
        when(fileUtils.fileReader(any())).thenReturn(reader);
        app.processArgs(args);
        verify(surveyService).setDataList(any());
        verify(surveyService).setTimeIntervals(any());
    }

    @Test
    public void shouldGetSurveyDaysAfterParsingVehicleList() throws Exception{
        when(fileUtils.fileReader(any())).thenReturn(reader);
        app.processArgs(args);
        InOrder inOrder = inOrder(vehicleParser,surveyService);
        inOrder.verify(vehicleParser).generateVehicleData();
        inOrder.verify(vehicleParser).getDay();
        inOrder.verify(surveyService).setDays(anyInt());
    }

    @Test
    public void shouldAnalyzeAfterSetting_Data_Days_TimeIntervals() throws Exception{
        when(fileUtils.fileReader(any())).thenReturn(reader);
        app.processArgs(args);
        InOrder inOrder = inOrder(surveyService);
        inOrder.verify(surveyService).setDataList(any());
        inOrder.verify(surveyService).setTimeIntervals(any());
        inOrder.verify(surveyService).setDays(any());
        inOrder.verify(surveyService,atLeast(3)).analyseData(any(),any());
    }

    @Test
    public void shouldCreateSurveysForCountAndSpeed() throws Exception{
        when(fileUtils.fileReader(any())).thenReturn(reader);
        app.processArgs(args);
        verify(surveyService,atLeastOnce()).analyseData(eq(AnalyserTypes.COUNT),any());
        verify(surveyService,atLeastOnce()).analyseData(eq(AnalyserTypes.SPEED),any());
    }

    @Test
    public void shouldConvertAnalysisToStringFormat() throws Exception {
        when(fileUtils.fileReader(args[0])).thenReturn(reader);
        when(fileUtils.getFilePath(any())).thenReturn(anyString());
        app.processArgs(args);
        verify(fileUtils, atLeast(3)).convertFactToFileFormat(any(), any());
    }

    @Test
    public void shouldWriteTheAnalysisToTextFile() throws Exception {
        when(fileUtils.fileReader(args[0])).thenReturn(reader);
        when(fileUtils.getFilePath(any())).thenReturn(anyString());
        app.processArgs(args);
        verify(fileUtils, atLeast(3)).getFilePath(any());
        verify(fileUtils, atLeast(3)).writeTextFile(any(), any());
    }

    @Test
    public void shouldCreateSurveysForDistance() throws Exception{
        when(fileUtils.fileReader(any())).thenReturn(reader);
        app.processArgs(args);
        verify(surveyService,atLeastOnce()).analyseData(eq(AnalyserTypes.DISTANCE),any());
    }
}