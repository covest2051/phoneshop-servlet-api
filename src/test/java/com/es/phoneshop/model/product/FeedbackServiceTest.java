package com.es.phoneshop.model.product;

import com.es.phoneshop.service.product.FeedbackService;
import com.es.phoneshop.service.product.FeedbackServiceImpl;
import com.es.phoneshop.service.product.ProductService;
import com.es.phoneshop.service.product.ProductServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

public class FeedbackServiceTest {
    private ProductService productService;
    private FeedbackService feedbackService;
    @Before
    public void setup() {
        productService = new ProductServiceImpl();
        feedbackService = new FeedbackServiceImpl();
    }

    @Test
    public void testGetFeedbackListSortedByRatingASC() {
        Currency usd = Currency.getInstance("USD");
        Product product1 = new Product("test-product-code1", "Samsung Galaxy S I", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

        productService.save(product1);

        productService.addFeedback(product1, new Feedback("Отлично", 5.0));
        productService.addFeedback(product1, new Feedback("Плохо", 1.0));

        assertEquals(5.0, product1.getFeedbackList().get(0).getRating(), 0.001);
        List<Feedback> feedbackList = feedbackService.getFeedbackList(product1.getFeedbackList(),  FeedbackSortField.RATING.toString(), SortOrder.ASC.toString());
        assertEquals(1.0, feedbackList.get(0).getRating(), 0.001);
    }
}
