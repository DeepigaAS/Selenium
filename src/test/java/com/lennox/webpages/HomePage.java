package com.lennox.webpages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

public class HomePage {
   private WebDriver driver;

   //Page URL
   private static String PAGE_URL="https://www.liidaveqa.com";

   //Locators

   
   @FindBy(xpath = "//a[contains(text(),'Sign In')]")
   private WebElement loginButton;

   @FindBy(xpath = "//div[@id='login-modal']/div/child::a[contains(text(),'close')]")
   private WebElement loginModalCloseButton;

   //@FindBy(xpath = "//div[contains(@class,'pull-left')]")
   @FindBy(xpath = "//a[contains(@href,'navigation')]")
   private WebElement menuButton;

   @FindBy(xpath = "")
   private WebElement linkNameMenu;
   
   @FindBy(xpath = "//a[text()='Compressors']")
   private WebElement pageNavigationMenu;
   
   @FindBy(xpath = "//li/a[text()='Compressors']")
   private WebElement typeMenu;
   

   //Constructor
   public HomePage(WebDriver driver){
       this.driver=driver;
       driver.get(PAGE_URL);
       driver.manage().window().maximize();
       //Initialise Elements
       PageFactory.initElements(driver, this);
   }

   public void clickOnLoginButton(){

	   loginButton.click();

   }
   public void clickOnCloseButton(){

	   loginModalCloseButton.click();

   }

   public void clickOnPageNavigation(String linkName, String pageNavigation, String type){
	   System.out.println(linkName+" "+pageNavigation+" "+ type);
	   	menuButton.click();
	   	driver.findElement(By.xpath("(//a[text()='"+linkName+"'])[2]")).click();
	   	driver.findElement(By.xpath("(//a[@title='"+pageNavigation+"'])[2]")).click();
	  	driver.findElement(By.xpath("//li/a[text()='"+type+"']")).click();

   }
   
  
}