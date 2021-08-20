package com.lennox.webpages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
   private WebDriver driver;
   private WebDriverWait wait;
   @FindBy(xpath = "//input[@id='j_username']")
   private WebElement usernameTextBox;

   @FindBy(xpath = "//input[@id='j_password']")
   private WebElement passwordTextBox;

   @FindBy(xpath = "//button[@id='loginSubmit']")
   private WebElement loginButton;

   @FindBy(xpath= "//button[contains(text(),'Welcome')]")
   private WebElement loginStatus;


   //Constructor
   public LoginPage (WebDriver driver, WebDriverWait wait){
       this.driver=driver;
       this.wait = wait;
       //Initialise Elements
       PageFactory.initElements(driver, this);
   }

  
   public void login(String email, String password){
	   usernameTextBox.sendKeys(email);
	   passwordTextBox.sendKeys(password);
	   loginButton.click();
   }


public boolean checkLoginStatus() {
	try {
	return loginStatus.isDisplayed();
	}
	catch(NoSuchElementException e)
	{
	return false;}
}


}