package com.lennox.webpages;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CompressorPage {
	private WebDriver driver;

	// Locators

	@FindBy(xpath = "//ul[@class='pagination']/li")
	private List<WebElement> pages;

	@FindBy(xpath = "//ul[@class='pagination']/li[@class='next']/a")
	private WebElement nextPageButton;

//
//   @FindBy(xpath = "//div/text()[normalize-space()='10T46']/parent::div/parent::a/child::div")
//   private List<WebElement> productDataElement;
//
//   @FindBy(xpath = "//div/text()[normalize-space()='10T46']/parent::div/preceding-sibling::div/h2[@class='title']")
//   private WebElement productDescription;

//   @FindBy(xpath = "//div/text()[normalize-space()='10T46']/parent::div/following-sibling::div/div/div[2]/p")
//   private WebElement WebElementyourPrice;
//
//   @FindBy(xpath = "//div/text()[normalize-space()='10T46']/parent::div/following-sibling::div/div/p[2]")
//   private WebElement listPrice;
//
//   @FindBy(xpath = "//div/text()[normalize-space()='10T46']/parent::div/parent::a/following-sibling::div/div[@id='ship-to-67W85']/div[2]/input")
//   private WebElement shippingRadioButton;
//   
//   @FindBy(xpath = "//div/text()[normalize-space()='10T46']/parent::div/parent::a/following-sibling::div/div[@id='pickup-67W85']/div[2]/input")
//   private WebElement storeRadioButton;

	@FindBy(xpath = "//div[@class='description']/p")
	private WebElement descriptionText;

private WebDriverWait wait;

	// Constructor
	public CompressorPage(WebDriver driver, WebDriverWait wait) {
		this.driver = driver;
		this.wait = wait;
		// Initialise Elements
		PageFactory.initElements(driver, this);
	}

	public List<WebElement> getPages() {

		return pages;

	}

	public Map<String, String> getProductDescription(String catalog) throws InterruptedException {
		Map<String, String> productDescMap = new HashMap<String, String>();
		String productDescription = driver.findElement(By.xpath("//div/text()[normalize-space()='" + catalog
				+ "']/parent::div/preceding-sibling::div/h2[@class='title']")).getText();
		String yourPrice = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
				"//div/text()[normalize-space()='" + catalog + "']/parent::div/following-sibling::div/div/div[2]/p")))
				.getText();
		String listPrice = driver.findElement(By
				.xpath("//div/text()[normalize-space()='" + catalog + "']/parent::div/following-sibling::div/div/p[2]"))
				.getText().split(":")[1].trim();
		String model = driver.findElement(By.xpath("//div/text()[normalize-space()='" + catalog + "']/parent::div"))
				.getText();
		Boolean available = wait.until(ExpectedConditions.textMatches(
				By.xpath("//div[@id='ship-to-" + catalog + "']/div[3]"), Pattern.compile("[A-Z a-z0-9]+")));

		if (available) {
			String shippingAvailability = driver.findElement(By.xpath("//div[@id='ship-to-" + catalog + "']/div[3]"))
					.getText();
			String storeAvailability = driver.findElement(By.xpath("//div[@id='pickup-" + catalog + "']/div[3]"))
					.getText();
			String zip = driver
					.findElement(By.xpath("//div[@id='pickup-" + catalog + "']/div[4]/p/span[@class='zip-replace']"))
					.getText();
			productDescMap.put("Shipping Availability", shippingAvailability);
			productDescMap.put("Store Availability", storeAvailability);
			productDescMap.put("zip", zip);
		}
		String cartStatus = driver.findElement(By.xpath("//button[@id='product-button-" + catalog + "']"))
				.getAttribute("disabled");
		if (cartStatus == null)
			cartStatus = "Enabled";
		else
			cartStatus = "Disabled";
		productDescMap.put("ProductDescription", productDescription);
		productDescMap.put("YourPrice", yourPrice);
		productDescMap.put("ListPrice", listPrice);
		productDescMap.put("Cart Status", cartStatus);

		StringTokenizer st = new StringTokenizer(model, "\n");
		int i = 1;
		while (st.hasMoreTokens()) {
			String token = st.nextToken().split(":")[1].trim();
			if (i == 1)
				productDescMap.put("Catalog", token);
			else if (i == 2)
				productDescMap.put("Model", token);
			i++;
		}

		return productDescMap;

	}

	public List<WebElement> getProductData(String catalog) {
		List<WebElement> productData = driver.findElements(
				By.xpath("//div/text()[normalize-space()='" + catalog + "']/parent::div/parent::a/child::div"));
		return productData;

	}

	public String getDescription() {
		return descriptionText.getText();

	}

	public void goToNextPage() {
		nextPageButton.click();

	}

	public void scrollToEnd() {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("window.scrollTo(0, document.body.scrollHeight)");
	}

	public void scrollIntoProductView(WebElement webElement) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", webElement);
	}
}