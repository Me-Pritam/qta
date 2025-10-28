package com.sendmessage;

import org.apache.poi.ss.usermodel.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileInputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class SendingMessagetoStudents {

    public static void main(String[] args) {
        try {
            List<Object[]> allData = SendingMessagetoStudents.getDataFromExcel("Batch_details");

            ChromeOptions option = new  ChromeOptions();

            option.addArguments("--disable-notifications");

            WebDriver driver = new ChromeDriver(option);

            driver.manage().window().maximize();

            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));

            driver.get("https://chat.qspiders.com/");

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));

            driver.findElement(By.xpath("//button[contains(text(),'User')]"))
                    .click();

            driver.findElement(By.xpath("//input[@name='username']"))
                    .sendKeys("Bongarala.b@qspiders.in");

            driver.findElement(By.xpath("//input[@name='password']"))
                    .sendKeys("1yb?Z-D3'1");

            driver.findElement(By.xpath("//button[text()='Login']"))
                    .click();

            for (Object[] row : allData) {
                String date = row[0].toString();

                if (row[1].toString().equals("null")) {
                    continue;
                } else {
                    String batchCode = row[1].toString();

                    String subject = row[2].toString();

                    String trainerName = row[3].toString();

                    String roomNo = row[4].toString();

                    String time = row[5].toString();


                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25));

                    wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='search']")))
                            .sendKeys(batchCode);


                    WebElement qtalkBatchCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='_user_container__1yvFq']/descendant::h1")));

                    if (qtalkBatchCode.isDisplayed())
                    {
                        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//div[@class='_user_container__1yvFq']/descendant::a"))))
                                .click();

                        if (wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='_chat_Head__v-gCN']/child::button"))).isDisplayed()) {
                            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='_chat_Head__v-gCN']/child::button")))
                                    .click();

                        }

                        if (driver.findElement(By.xpath("//p[text()='This is BroadCast Chat']")).isDisplayed()) {
                            Actions action = new Actions(driver);

                            action.click(driver.findElement(By.xpath("//input[@placeholder='write something...']")))
                                    .sendKeys(wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//input[@placeholder='write something...']")))),
                                            "Hi! Please note that on " + date + ", your " + subject + " class will be held in Room " + roomNo + " at " + time + ".")
//                                            "Hi this is an Automated message for Batch Updates/ Requirements/ Placement Reports/ Mock Updates/ Emergency Updates. From tomorrow onwards everyday you will be getting Updates. Stay Tuned...")
                                    .perform();

                            System.out.println(date+" "+time+" "+subject+" "+roomNo);


                            action.pause(Duration.ofSeconds(2))
                                    .sendKeys(Keys.ENTER)
                                    .perform();

                            Thread.sleep(1000);


                            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Batch List']")))
                                    .click();

                            Thread.sleep(1000);
                            driver.navigate().refresh();
                        }
                    } else {
                        //System.out.println(batchCode + " problem happened");
                        driver.navigate().refresh();
                        //continue;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Object[]> getDataFromExcel(String sheetName) {
        List<Object[]> dataList = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream("ExcelFile/NewBatchInfo.xlsx")) {
            Workbook wb = WorkbookFactory.create(fis);
            Sheet sheet = wb.getSheet(sheetName);

            int rowNum = sheet.getLastRowNum();

            for (int i = 1; i <= rowNum; i++) {  // assuming row 0 is header
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Object[] dataArr = new Object[6];
                dataArr[0] = row.getCell(0).toString();
                dataArr[1] = row.getCell(1).toString();
                dataArr[2] = row.getCell(2).toString();
                dataArr[3] = row.getCell(3).toString();
                dataArr[4] = row.getCell(4).toString();
                dataArr[5] = row.getCell(5).toString();

                dataList.add(dataArr);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataList;
    }
}
