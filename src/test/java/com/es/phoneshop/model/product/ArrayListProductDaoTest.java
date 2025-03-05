package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest {
    private ProductDao productDao;

    @Before
    public void setup() throws ProductNotFoundException {
        productDao = new ArrayListProductDao();
    }

    @Test
    public void testGetProduct() throws ProductNotFoundException {

        Currency usd = Currency.getInstance("USD");
        Product newProduct = new Product("test-product-code", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(newProduct);

        Product getProduct = productDao.getProduct(newProduct.getId());

        assertNotNull(getProduct);
        assertEquals(newProduct.getId(), getProduct.getId());
        assertEquals("test-product-code", getProduct.getCode());
    }

    @Test
    public void testFindProductsNoResults() {
        assertFalse(productDao.findProducts().isEmpty());
    }

    @Test
    public void testSaveProduct() throws ProductNotFoundException {
        Currency usd = Currency.getInstance("USD");
        Product newProduct = new Product("test-product-code", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(newProduct);

        Product savedProduct = productDao.getProduct(newProduct.getId());

        assertNotNull(savedProduct);
        assertEquals("test-product-code", savedProduct.getCode());
    }

    @Test
    public void testDeleteProduct() throws ProductNotFoundException {
        Currency usd = Currency.getInstance("USD");
        Product productForSave = new Product("test-product-code", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

        productDao.save(productForSave);

        Product getProduct = productDao.getProduct(productForSave.getId());
        assertNotNull(getProduct);

        productDao.delete(productForSave.getId());

        try {
            productDao.getProduct(productForSave.getId());
            fail("Expected ProductNotFoundException");
        } catch (ProductNotFoundException e) {
            // Выброшено исключение => продукт удалён
        }
    }

    @Test
    public void testUpdateProduct() throws ProductNotFoundException {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("test-product-code", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);

        product.setDescription("Updated description");
        productDao.save(product);

        Product updatedProduct = productDao.getProduct(product.getId());
        assertEquals("Updated description", updatedProduct.getDescription());
    }

    @Test
    public void testUniqueId() throws ProductNotFoundException {
        Currency usd = Currency.getInstance("USD");
        Product product1 = new Product("test-product-code1", "Samsung Galaxy S I", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        Product product2 = new Product("test-product-code2", "Samsung Galaxy S II", new BigDecimal(100), usd, 200, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

        productDao.save(product1);
        productDao.save(product2);

        assertNotEquals(product1.getId(), product2.getId());
    }

}
