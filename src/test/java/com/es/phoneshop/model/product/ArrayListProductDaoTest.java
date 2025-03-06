package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class ArrayListProductDaoTest {
    private ProductDao productDao;

    @Before
    public void setup() {
        productDao = ArrayListProductDao.getArrayListProductDao();
    }

    @Test
    public void testGetProduct() {
        Currency usd = Currency.getInstance("USD");
        Product newProduct = new Product("test-product-code", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

        productDao.save(newProduct);

        Optional<Product> getProduct = productDao.getProduct(newProduct.getId());

        assertTrue(getProduct.isPresent());
        assertEquals(newProduct.getId(), getProduct.get().getId());
        assertEquals("test-product-code", getProduct.get().getCode());
    }

    @Test
    public void testFindProductsNoResults() {
        assertFalse(productDao.findProducts().isEmpty());
    }

    @Test
    public void testSaveProduct() {
        Currency usd = Currency.getInstance("USD");
        Product newProduct = new Product("test-product-code", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(newProduct);

        Optional<Product> savedProduct = productDao.getProduct(newProduct.getId());

        assertTrue(savedProduct.isPresent());
        assertEquals("test-product-code", savedProduct.get().getCode());
    }

    @Test
    public void testDeleteProduct() {
        Currency usd = Currency.getInstance("USD");
        Product productForSave = new Product("test-product-code", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

        productDao.save(productForSave);

        Optional<Product> getProduct = productDao.getProduct(productForSave.getId());
        assertTrue(getProduct.isPresent());

        productDao.delete(productForSave.getId());

        Optional<Product> deletedProduct = productDao.getProduct(productForSave.getId());
        assertFalse(deletedProduct.isPresent());
    }

    @Test
    public void testUpdateProduct() {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("test-product-code", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);

        product.setDescription("Updated description");
        productDao.save(product);

        Optional<Product> updatedProduct = productDao.getProduct(product.getId());
        assertTrue(updatedProduct.isPresent());
        assertEquals("Updated description", updatedProduct.get().getDescription());
    }

    @Test
    public void testUniqueId() {
        Currency usd = Currency.getInstance("USD");
        Product product1 = new Product("test-product-code1", "Samsung Galaxy S I", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        Product product2 = new Product("test-product-code2", "Samsung Galaxy S II", new BigDecimal(100), usd, 200, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

        productDao.save(product1);
        productDao.save(product2);

        assertNotEquals(product1.getId(), product2.getId());
    }
}
