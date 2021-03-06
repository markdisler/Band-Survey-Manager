import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

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

	public static void createExcelFile(ArrayList<BusList> busLists) {
		
		try {
			String fileName = "/Users/Mark/Desktop/buslist.xls";

			WorkbookSettings ws = new WorkbookSettings();
			ws.setLocale(new Locale("en", "EN"));
			WritableWorkbook workbook = Workbook.createWorkbook(new File(fileName), ws);

			for (BusList list : busLists) {
				WritableSheet s = workbook.createSheet(list.getInstrument().replaceAll("/", " & "), 0);
				System.out.println(list.getInstrument());
				writeDataSheet(s, list);
			}
			
			workbook.write();
			workbook.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		}

	}

	private static void writeDataSheet(WritableSheet sheet, BusList list) {
		try {
			
			
			Map<String, Integer> numPersonnel = new HashMap<String, Integer>();
			
			WritableFont wf = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
			WritableCellFormat cf = new WritableCellFormat(wf);
			sheet.setColumnView(0,  25);

			for (int i = 0; i < list.personnel.size(); i++) {
				Person p = list.personnel.get(i);
				
				String name = p.getFullName();
				String instr = p.getInstrument();
				
				Label l = new Label(0, i, name, cf);
				sheet.addCell(l);
				
				Label l2 = new Label(1, i, instr, cf);
				sheet.addCell(l2);
				
				if (numPersonnel.get(instr) == null) {
					numPersonnel.put(instr, 1);
				} else {
					numPersonnel.put(instr, numPersonnel.get(instr) + 1);
				}
			}
			
			int idx = list.personnel.size() + 2;
			int total = 0;
			for (String k : numPersonnel.keySet()) {
				Label lbl = new Label(0, idx, numPersonnel.get(k) + " " + k, cf);
				sheet.addCell(lbl);
				idx += 1;
				
				total += numPersonnel.get(k);
			}
			
			Label lbl = new Label(0, idx, "Total: " + total, cf);
			sheet.addCell(lbl);
			
		} catch (WriteException e) {
			e.printStackTrace();
		}
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
