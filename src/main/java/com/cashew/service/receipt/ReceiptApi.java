package com.cashew.service.receipt;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class ReceiptApi {

    private RemoteWebDriver driver;

    private final String address = "https://proverkacheka.com/";

    @PostConstruct
    private void init() {
        try {
            System.setProperty("webdriver.chrome.silentOutput", "true");
            System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY, "true");
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless", "--disable-logging");

            this.driver = new RemoteWebDriver(new URL("http://194.35.116.155:4444/wd/hub"), options);
        } catch (Exception e) {
            log.error("l(0)", e);
        }
    }

    public Receipt receiptByToken(String token) {

        Receipt receipt = new Receipt();

        WebElement table = getResultTable(token);
        if (table == null) { return null; }
        String result = table.getAttribute("innerHTML");

        List<Integer> trStarts = new ArrayList<>();
        List<Integer> trEnds = new ArrayList<>();

        int countRow = 0;
        int countProducts = 0;
        int countMetaProducts = 0;

        int index = 0;
        while (index >= 0) {
            index = result.indexOf("</tr>", index + 1);
            trEnds.add(index);
        }

        int index1 = 0;
        while (index1 >= 0) {
            index1 = result.indexOf("<tr", index1 + 1) ;
            trStarts.add(index1);
        }

        for (int i = 0; i < trStarts.size(); i++) {
            if (trStarts.get(i) == -1) { continue; }
            String row = result.substring(trStarts.get(i), trEnds.get(i) + 5);

            List<Integer> tdStarts = new ArrayList<>();
            List<Integer> tdEnds = new ArrayList<>();

            int indextd = 0;
            while (indextd >= 0) {
                indextd = row.indexOf("<td", indextd + 1);
                tdStarts.add(indextd);
            }

            int index1td = 0;
            while (index1td >= 0) {
                index1td = row.indexOf("</td>", index1td + 1);
                tdEnds.add(index1td);
            }

            if (tdStarts.size() == 2) {
                String text = postProcess(row.substring(tdStarts.get(0), tdEnds.get(0) + 5));
                switch (countRow) {
                    case 0: {
                        receipt.setCompany(text);
                    }
                    case 1: {
                        receipt.setAddress(text);
                        break;
                    }
                    case 2: {
                        receipt.setINN(text);
                        break;
                    }
                    case 4: {
                        receipt.setDate(text);
                        break;
                    }
                    case 5: {
                        receipt.setReceiptNumber(text);
                        break;
                    }
                    case 6: {
                        receipt.setShift(text);
                        break;
                    }
                    case 7: {
                        receipt.setCashier(text);
                        break;
                    }
                    case 9: {
                        receipt.setTaxation(text);
                        break;
                    }
                }
                countRow++;
            } else if (tdStarts.size() == 6) {
                if (countProducts == 0) {
                    countProducts++;
                    continue;
                }
                String id = postProcess(row.substring(tdStarts.get(0), tdEnds.get(0) + 5));
                String name = postProcess(row.substring(tdStarts.get(1), tdEnds.get(1) + 5));
                String price = postProcess(row.substring(tdStarts.get(2), tdEnds.get(2) + 5));
                String count = postProcess(row.substring(tdStarts.get(3), tdEnds.get(3) + 5));
                String sumPrice = postProcess(row.substring(tdStarts.get(4), tdEnds.get(4) + 5));

                Product product = new Product();
                product.setId(id);
                product.setName(name);
                product.setPrice(toBigDecimal(price));
                product.setCount(count);
                product.setSumPrice(toBigDecimal(sumPrice));
                receipt.getProducts().add(product);
            } else if (tdStarts.size() == 4) {
                String text = postProcess(row.substring(tdStarts.get(2), tdEnds.get(2) + 5));
                switch (countMetaProducts) {
                    case (0): {
                        receipt.setTotal(toBigDecimal(text));
                        break;
                    }
                    case (1): {
                        receipt.setCash(toBigDecimal(text));
                        break;
                    }
                    case (2): {
                        receipt.setCard(toBigDecimal(text));
                        break;
                    }
                    case (3): {
                        receipt.setVAT20(toBigDecimal(text));
                        break;
                    }
                    case (4): {
                        receipt.setVAT10(toBigDecimal(text));
                        break;
                    }
                }
                countMetaProducts++;
            }
        }

        System.out.println(receipt.toString());

        return receipt;
    }

    private BigDecimal toBigDecimal(String text) {
        return BigDecimal.valueOf(Double.parseDouble(text));
    }


    private String postProcess(String text) {
        int start = text.indexOf(">");
        int end = text.indexOf("</");
        return text.substring(start + 1, end).strip();
    }

    private WebElement getResultTable(String token) {
        driver.get(address);

        try {
            WebElement element2 = driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[1]/div[4]/div[2]/ul/li[4]/a"));

            JavascriptExecutor executor1 = (JavascriptExecutor) driver;
            executor1.executeScript("arguments[0].click();", element2);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(750));

            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("b-checkform_qrraw")));
            element.sendKeys(Keys.TAB);
            element.clear();
            element.sendKeys(token);

            WebElement element1 = driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[1]/div[4]/div[2]/div[1]/div[3]/div/div/div/form/div[2]/div/button"));
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].click();", element1);

            WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofMillis(750));
            return wait2.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div[2]/div[1]/div[4]/div[2]/div[2]/div/table")));
        } catch (Exception e) {
            return null;
        }
    }

}
