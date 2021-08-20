package com.lennox.tests;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.*;
import com.lennox.webpages.CompressorPage;
import com.lennox.webpages.HomePage;
import com.lennox.webpages.LoginPage;

import io.github.bonigarcia.wdm.WebDriverManager;
public class SearchProductTest {
 
  	WebDriver driver;
  	ExtentReports extentReport;
  	ExtentHtmlReporter htmlReporter;
  	ExtentTest testcase;
  	
  	@BeforeSuite
  	public void initialize() {
  		extentReport = new ExtentReports();
  	  htmlReporter = new ExtentHtmlReporter("ExtentReport.html");
  	  extentReport.attachReporter(htmlReporter);
  	}
  	@BeforeMethod
  	  public void setUp() {
           	  System.out.println("Start test");
        	 WebDriverManager.chromedriver().setup();
        	  driver = new ChromeDriver();  
        	  
  	  }
  	
  	@DataProvider(name ="excel-data")
  	public Object[][] excelDP() throws IOException{
        	//We are creating an object from the excel sheet data by calling a method that reads data from the excel stored locally in our system
  			String filepath = System.getProperty("user.dir") + "\\src\\test\\resources\\testcase.xlsx";
        	Object[][] arrObj = getExcelData(filepath,"Sheet1");
        	return arrObj;
  	}
  	//This method handles the excel - opens it and reads the data from the respective cells using a for-loop & returns it in the form of a string array
  	public String[][] getExcelData(String fileName, String sheetName){
        	
        	String[][] data = null;   	
  	  	try
  	  	{
  	   	FileInputStream fis = new FileInputStream(fileName);
  	   	XSSFWorkbook wb = new XSSFWorkbook(fis);
  	   	XSSFSheet sh = wb.getSheet(sheetName);
  	   	XSSFRow row = sh.getRow(0);
  	   	int noOfRows = sh.getPhysicalNumberOfRows();
  	   	int noOfCols = row.getLastCellNum();
  	   	System.out.println(noOfRows);
  	   	System.out.println(noOfCols);
  	   	Cell cell;
  	   	data = new String[noOfRows-1][noOfCols];
  	   	
  	   	for(int i =1; i<noOfRows;i++){
  		     for(int j=0;j<noOfCols;j++){
  		    	   row = sh.getRow(i);
  		    	   cell= row.getCell(j);
  		    	   data[i-1][j] = cell.getStringCellValue();
  	   	 	   }
  	   	}
  	  	}
  	  	catch (Exception e) {
  	     	   System.out.println("The exception is: " +e.getMessage());
           	}
        	return data;
  	}
  	@Test(dataProvider ="excel-data")
  	//public void search(String TestCaseName, String TestCaseDescription, String emailId, String password, String linkName, String pageNavigation, String pageDescription, String catalog, String yourPrice, String model){
  	public void searchProduct(String execution, String testId, String TestCaseName, String TestCaseDescription, String email, String password, String linkName, String pageNavigation, String pageDescription, String catalog, String yourPrice, String model) throws InterruptedException, IOException{
  		testcase= extentReport.createTest(testId, TestCaseName);
  		try {
  		if(execution.equalsIgnoreCase("Y"))
  		{
  		testcase.log(Status.INFO,"Execution is set to Y and hence the execution begins...");
		HomePage home = new HomePage(driver);
		Thread.sleep(30);
		home.clickOnLoginButton();
		LoginPage login = new LoginPage(driver);
		login.login(email, password);
		
		String navigationMenu[] = pageNavigation.split(":::");
		home.clickOnPageNavigation(linkName, navigationMenu[0], navigationMenu[1]);
		CompressorPage compressor = new CompressorPage(driver);
		String description = compressor.getDescription();
		if(description.equals(pageDescription))
			testcase.log(Status.INFO, "Description of the page matches with the expected description "+pageDescription);
		else
		{
			testcase.log(Status.ERROR, "Description of the page does not match with the expected description");
			testcase.log(Status.INFO, "Actual Description: "+description +"; Expected Description: "+pageDescription);
		}
		
		List<WebElement> pages = compressor.getPages();
		Map<String, String> productDesc = null;
		do {
			List<WebElement> products = compressor.getProductData(catalog);
			if (products.size() > 0) {
				productDesc = compressor.getProductDescription(catalog);
				testcase.log(Status.INFO, "Found the product with Catalog#: "+catalog);
				break;
			}
			compressor.scrollToEnd();
			try {
				compressor.goToNextPage();
			} catch (NoSuchElementException e) {
				testcase.log(Status.INFO, "No Product found with Catalog#: "+catalog);
				break;
				
			}
		} while (true);
		if (productDesc != null) {
			testcase.log(Status.INFO,"Product Description is "+productDesc);
			if((yourPrice.equals(productDesc.get("YourPrice"))) && (model.equals(productDesc.get("Model"))))
			{
			testcase.log(Status.PASS,"Your Price and Model are matching with the expected data");
			testcase.log(Status.INFO,"Your Price: "+yourPrice+"; Model: "+ model);
			}
			else
			{
				testcase.log(Status.FAIL, "Your Price and Model are matching with the expected data");
				testcase.log(Status.INFO, "Actual Price: "+ productDesc.get("YourPrice")+"; Expected Price: "+yourPrice);
				testcase.log(Status.INFO, "Actual Model: "+ productDesc.get("Model") +"; Expected Model: "+model);
			}
		}
	}
  		else
  			testcase.log(Status.INFO, "Execution is set to N and no execution has taken place");
  		
  	}
  		catch(Exception e)
  		{
  			TakesScreenshot screenshot = (TakesScreenshot) driver;
  			File sourceFile = screenshot.getScreenshotAs(OutputType.FILE);
  			File destinationFile = new File("error.png");
  			FileHandler.copy(sourceFile, destinationFile);
  			testcase.addScreenCaptureFromPath("error.png");
  		}
  	}
  	@AfterMethod
  	public void burnDown(){
        	driver.quit();
  	} 	
  	@AfterSuite
  	public void tearDown() {
  		extentReport.flush();
  	}
}