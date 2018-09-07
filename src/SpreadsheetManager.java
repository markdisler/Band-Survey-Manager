import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class SpreadsheetManager {

	public static String[][] getTableFromSpreadsheet(String path) {

		try {
			Workbook workbook = Workbook.getWorkbook(new File(path));
			Sheet sheet = workbook.getSheet(0);
			String[][] sheetData = getSheetData(sheet);
			workbook.close();  
			return sheetData;
		} catch (BiffException | IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	private static String[][] getSheetData(Sheet s) {
		int blankRowCount = 0;

		ArrayList<ArrayList>list = new ArrayList<ArrayList>();
		for (int i = 0; i < s.getRows(); i++) {
			ArrayList<String>cols = new ArrayList<String>();
			boolean blankRow = true;
			for (int j = 0; j < s.getColumns(); j++) {
				Cell cell = s.getCell(j, i);
				String content = cell.getContents();
				cols.add(content);
				if (!content.equals("")) {
					blankRow = false;			
				}
			}
			list.add(cols);	

			if (blankRow) {
				blankRowCount++;	
			} else if (!blankRow && blankRowCount != 0) {
				blankRowCount = 0;
			}

			if (blankRowCount >= 20) {
				for (int a = 0; a < blankRowCount-3; a++) {
					list.remove(list.size()-1);
				}
				i = s.getRows();
			}
		}

		String [][] allData = new String [list.size()][list.get(0).size()];

		for (int i = 0; i < allData.length; i++) {
			ArrayList<String>sublist = list.get(i);
			String [] a = new String [list.get(i).size()];
			for (int j = 0; j < a.length; j++) {
				a[j] = sublist.get(j);			
			}
			allData[i] = a;
		}

		return allData;
	}

}
