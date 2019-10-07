package testutils;

import utils.ExcelManager;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

public class TestCasesData {

    ExcelManager em;

    public TestCasesData() throws IOException, InvalidFormatException {
        em = new ExcelManager(System.getProperty("user.dir") + "\\src\\main\\java\\data\\waesdata.xlsx");
    }

    public Object[][] getTestData(String testName) throws IOException, InvalidFormatException {
        Sheet testSheet = em.getExcelSheet(testName);

        Iterator<Row> rows = testSheet.iterator();
        Row firstRow = rows.next();
        Object[][] testData = new Object[testSheet.getPhysicalNumberOfRows()-1][firstRow.getPhysicalNumberOfCells()];

        int i = 0;
        while(rows.hasNext()) {
            Row row = rows.next();

            JSONObject payload = new JSONObject();
            Iterator<Cell> cells = row.iterator();
            int cellNumber = 0;
            while (cells.hasNext()) {
                Cell currentCell = cells.next();
                testData[i][cellNumber] = currentCell.getStringCellValue();
                cellNumber++;
            }
            i++;
        }

        return testData;
    }
}
