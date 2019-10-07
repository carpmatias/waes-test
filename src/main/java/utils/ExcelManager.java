package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelManager {		
		
		Workbook excelFileToRead;
		XSSFWorkbook excelFileToWrite;
		FileInputStream fs;
		FileOutputStream fo;
		File f;		
		String path;
		
		public ExcelManager(String path) throws InvalidFormatException, IOException
		{
			this.path = path;
			this.f = new File(path);		
		}

		/**
		 * Returns specifid excel sheet.
		 *
		 * @param sheetName specific sheet to return
		 *
		 * @author Matías Cárdenas
		 */
		public Sheet getExcelSheet(String sheetName) throws InvalidFormatException, IOException {
			this.fs = new FileInputStream(this.f);	
			this.excelFileToRead = WorkbookFactory.create(this.fs);
			Sheet sheet = excelFileToRead.getSheet(sheetName);
			this.fs.close();
			this.fs = null;
			return sheet;
		}


	/**
	 * Returns one excel row from the document
	 *
	 * @param sheetToRead specific sheet to read
	 * @param rowNumber
	 * @param columnFrom
	 * @param columnTo
	 *
	 * @author Matías Cárdenas
	 */
		public ArrayList<String> readExcelRow(String sheetToRead, int rowNumber, int columnFrom, int columnTo) throws InvalidFormatException, IOException
		{
			ArrayList<String> row = new ArrayList();
			
			this.fs = new FileInputStream(this.f);
			this.excelFileToRead = WorkbookFactory.create(this.fs);
			
			Sheet readingSheet = excelFileToRead.getSheet(sheetToRead);
			Row readingRow = readingSheet.getRow(rowNumber);
			
			int columnNumber = columnFrom;
			
			while(columnNumber <= columnTo) {				
				Cell readingCell = readingRow.getCell(columnNumber);
				if(readingCell != null)
					row.add(readingCell.toString());
				else
					row.add("");
				columnNumber++;
			}
			
			this.fs.close();
			this.fs = null;
				
			return row;			
		}
}
