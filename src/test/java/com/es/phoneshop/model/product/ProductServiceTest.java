package com.es.phoneshop.model.product;

import com.es.phoneshop.service.product.ProductService;
import com.es.phoneshop.service.product.ProductServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class ProductServiceTest {
    private ProductService productService;
    private Currency usd = Currency.getInstance("USD");

    @Before
    public void setup() {
        productService = new ProductServiceImpl();
    }

    @Test
    public void testGetProduct() {
        Currency usd = Currency.getInstance("USD");
        Product newProduct = new Product("test-product-code", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

        productService.save(newProduct);

        Optional<Product> getProduct = productService.getProduct(newProduct.getId());
        assertTrue(getProduct.isPresent());
        assertEquals("test-product-code", getProduct.get().getCode());
    }

    @Test
    public void testUpdateProduct() {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("test-product-code", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

        productService.save(product);

        product.setDescription("Updated Description");

        productService.save(product);

        Optional<Product> updatedProduct = productService.getProduct(product.getId());
        assertTrue(updatedProduct.isPresent());
        assertEquals("Updated Description", updatedProduct.get().getDescription());
    }

    @Test
    public void testDeleteProduct() {
        Currency usd = Currency.getInstance("USD");
        Product productForSave = new Product("test-product-code", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

        productService.save(productForSave);

        Optional<Product> getProduct = productService.getProduct(productForSave.getId());
        assertTrue(getProduct.isPresent());

        productService.delete(productForSave.getId());

        Optional<Product> deletedProduct = productService.getProduct(productForSave.getId());
        assertFalse(deletedProduct.isPresent());
    }

    @Test
    public void testSaveAllProducts() {
        Currency usd = Currency.getInstance("USD");
        Product product1 = new Product("test-product-code1", "Samsung Galaxy S I", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        Product product2 = new Product("iphone6", "Apple iPhone 6", new BigDecimal(1000), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg");

        List<Product> productList = List.of(product1, product2);
        productService.saveAll(productList);

        Optional<Product> getProduct1 = productService.getProduct(product1.getId());
        Optional<Product> getProduct2 = productService.getProduct(product2.getId());
        assertTrue(getProduct1.isPresent());
        assertTrue(getProduct2.isPresent());
        assertEquals("Samsung Galaxy S I", getProduct1.get().getDescription());
        assertEquals("Apple iPhone 6", getProduct2.get().getDescription());
    }

    @Test
    public void testFindProductsSortedByPrice() {
        Currency usd = Currency.getInstance("USD");
        Product product1 = new Product("test-product-code1", "Samsung Galaxy S I", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        Product product2 = new Product("iphone6", "Apple iPhone 6", new BigDecimal(1000), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg");

        productService.save(product1);
        productService.save(product2);

        List<Product> products = productService.findProducts(null, SortField.PRICE.toString(), SortOrder.ASC.toString());
        assertFalse(products.isEmpty());

        assertEquals("test-product-code1", products.get(0).getCode());
        assertEquals("iphone6", products.get(1).getCode());
    }

    @Test
    public void testSearchProductsWithOrClause() {
        Currency usd = Currency.getInstance("USD");
        Product product1 = new Product("test-product-code1", "Samsung Galaxy S I", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        Product product2 = new Product("test-product-code2", "Samsung Galaxy S II", new BigDecimal(100), usd, 200, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

        productService.save(product1);
        productService.save(product2);

        List<Product> searchResults = productService.findProducts("Samsung Galaxy", null, null);

        assertFalse(searchResults.isEmpty());
        assertTrue(searchResults.contains(product1));
        assertTrue(searchResults.contains(product2));
    }
}
