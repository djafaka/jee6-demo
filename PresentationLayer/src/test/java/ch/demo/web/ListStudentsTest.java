/**
 * 
 */
package ch.demo.web;

import java.io.File;

import junit.framework.Assert;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import ch.demo.web.helper.AbstractEmbeddedTomcatTest;

/**
 * Tests the page in which the student list is displayed.
 * 
 * @author hostettler
 */
public class ListStudentsTest extends AbstractEmbeddedTomcatTest {

	/**
	 * Test a simple workflow. It verifies that the student list does return something useful.
	 * 
	 * @throws Exception
	 *             if anything goes wrong
	 */
	@Test
	public void testListStudents() throws Exception {
		WebDriver driver = new HtmlUnitDriver();
		driver.get(getAppBaseURL());

		login(driver);
		Assert.assertTrue(driver.getPageSource().contains("Steve Hostettler"));
		WebElement element = driver.findElement(By.xpath("//button[contains(@id,'register')]"));
		Assert.assertNotNull(element);
		element.click();
		
		Assert.assertTrue(driver.getPageSource().contains("Enregistrer un "));
	}


	/**
	 * Test a simple workflow. Verifies that upon unsuccessful login the user is redirected to the login page.
	 * 
	 * @throws Exception
	 *             if anything goes wrong
	 */
	@Test
	public void testWrongLogin() throws Exception {
		WebDriver driver = new HtmlUnitDriver();
		driver.get(getAppBaseURL());

		WebElement username = driver.findElement(By.xpath("//input[contains(@id,'username')]"));
		username.sendKeys("foo");
		WebElement password = driver.findElement(By.xpath("//input[contains(@id,'password')]"));
		password.sendKeys("foo");
		WebElement login = driver.findElement(By.xpath("//button[contains(@id,'login')]"));
		login.click();
		Assert.assertTrue(driver.getPageSource().contains("username"));
		Assert.assertTrue(driver.getPageSource().contains("password"));
		Assert.assertTrue(driver.getPageSource().contains("login"));

	}
	
	/**
	 * Verifies that a normal user cannot add a student.
	 * 
	 * @throws Exception
	 *             if anything goes wrong
	 */
	@Test
	public void testNotEnoughRights() throws Exception {
		WebDriver driver = new HtmlUnitDriver();
		driver.get(getAppBaseURL());

		WebElement username = driver.findElement(By.xpath("//input[contains(@id,'username')]"));
		username.sendKeys("user");
		WebElement password = driver.findElement(By.xpath("//input[contains(@id,'password')]"));
		password.sendKeys("user");
		WebElement login = driver.findElement(By.xpath("//button[contains(@id,'login')]"));
		login.click();
		Assert.assertTrue(driver.getPageSource().contains("Steve Hostettler"));
		WebElement element = driver.findElement(By.xpath("//button[contains(@id,'register')]"));
		Assert.assertNotNull(element);
		Assert.assertFalse(element.isEnabled());
		
	}

	/**
	 * Logs into the web site.
	 * 
	 * @param driver
	 *            to apply the login
	 */
	private void login(final WebDriver driver) {
		WebElement username = driver.findElement(By.xpath("//input[contains(@id,'username')]"));
		username.sendKeys("admin");
		WebElement password = driver.findElement(By.xpath("//input[contains(@id,'password')]"));
		password.sendKeys("admin");
		WebElement login = driver.findElement(By.xpath("//button[contains(@id,'login')]"));
		login.click();
	}

	/** Where the web sources are. */
	private static final String WEBAPP_SRC = "src/main/webapp";

	/**
	 * Build a war to test a simple workflow.
	 * 
	 * @return a war
	 */
	protected WebArchive createWebArchive() {
		final WebArchive archive = ShrinkWrap.create(WebArchive.class, "test.war");
		archive.setWebXML(new File(WEBAPP_SRC, "WEB-INF/web.xml"))
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsWebInfResource(new File(WEBAPP_SRC, "WEB-INF/faces-config.xml"))
				.addPackage(java.lang.Package.getPackage("ch.demo.web"))
				.addAsWebResource(new File(WEBAPP_SRC, "login.xhtml"))
				.addAsWebResource(new File(WEBAPP_SRC, "index.jsp"))
				.addAsWebResource(new File(WEBAPP_SRC, "xhtml/listStudents.xhtml"), "xhtml/listStudents.xhtml")
				.addAsWebResource(new File(WEBAPP_SRC, "xhtml/index.xhtml"), "xhtml/index.xhtml")
				.addAsWebResource(new File(WEBAPP_SRC, "xhtml/registerStudent.xhtml"), "xhtml/registerStudent.xhtml")
				.addAsWebResource(new File(WEBAPP_SRC, "xhtml/templates/layout.xhtml"), "xhtml/templates/layout.xhtml")
				.addAsWebResource(new File(WEBAPP_SRC, "xhtml/templates/searchbox.xhtml"),
						"xhtml/templates/searchbox.xhtml")
				.addAsWebResource(new File(WEBAPP_SRC, "xhtml/templates/dialog.xhtml"), "xhtml/templates/dialog.xhtml");
		return archive;
	}

	@Override
	protected String getApplicationId() {
		return "test";
	}
}
