package com.es.phoneshop.service.product;

import com.es.phoneshop.model.product.Feedback;
import com.es.phoneshop.model.product.Product;

import java.util.List;

public interface FeedbackService {
    List<Feedback> getFeedbackList(List<Feedback> feedbackList, String sortFieldStr, String sortOrderStr);
}
