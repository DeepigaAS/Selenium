package com.lennox.webpages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {
   private WebDriver driver;

   @FindBy(xpath = "//input[@id='j_username']")
   private WebElement usernameTextBox;

   @FindBy(xpath = "//input[@id='j_password']")
   private WebElement passwordTextBox;

   @FindBy(xpath = "//button[@id='loginSubmit']")
   private WebElement loginButton;

   //Constructor
   public LoginPage (WebDriver driver){
       this.driver=driver;

       //Initialise Elements
       PageFactory.initElements(driver, this);
   }

  
   public void login(String email, String password){
	   usernameTextBox.sendKeys(email);
	   passwordTextBox.sendKeys(password);
	   loginButton.click();
   }


}