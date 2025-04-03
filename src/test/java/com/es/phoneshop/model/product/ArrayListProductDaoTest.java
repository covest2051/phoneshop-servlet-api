package com.es.phoneshop.model.product;

import com.es.phoneshop.dao.product.ArrayListProductDao;
import com.es.phoneshop.dao.product.ProductDao;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;
import java.util.List;
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
        Currency usd = Currency.getInstance("USD");
        Product newProduct = new Product("test-product-code", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(newProduct);

        String query = null;
        SortField sortField = SortField.PRICE;
        SortOrder sortOrder = SortOrder.ASC;
        assertFalse(productDao.findProducts(query, sortField, sortOrder).isEmpty());
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

    @Test
    public void testSearchProductsWithOrClause() {
        Currency usd = Currency.getInstance("USD");
        Product product1 = new Product("test-product-code1", "Samsung Galaxy S I", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        Product product2 = new Product("test-product-code2", "Samsung Galaxy S II", new BigDecimal(100), usd, 200, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product1);
        productDao.save(product2);

        List<Product> searchResults = productDao.findProducts("Samsung S", SortField.PRICE, SortOrder.ASC);

        assertFalse(searchResults.isEmpty());
        assertTrue(searchResults.contains(product1));
        assertTrue(searchResults.contains(product2));
    }

    @Test
    public void testSortProductsByDescriptionAscending() {
        Currency usd = Currency.getInstance("USD");
        Product product1 = new Product("test-product-code1", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        Product product2 = new Product("iphone6", "Apple iPhone 6", new BigDecimal(1000), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg");
        productDao.save(product1);
        productDao.save(product2);

        List<Product> searchResults = productDao.findProducts(null, SortField.DESCRIPTION, SortOrder.ASC);

        assertEquals("Apple iPhone 6", searchResults.get(0).getDescription());
        assertEquals("Samsung Galaxy S", searchResults.get(1).getDescription());
    }

    @Test
    public void testPriceHistoryPopup() {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("test-product-code1", "Samsung Galaxy S I", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");


        product.getPriceHistory().add(new PriceHistory(new Date(), new BigDecimal(150)));
        productDao.save(product);

        assertEquals(2, product.getPriceHistory().size());
        assertEquals(product.getPriceHistory().get(0).getPrice(), (new BigDecimal(100)));
        assertEquals(product.getPriceHistory().get(1).getPrice(), (new BigDecimal(150)));
    }


}
