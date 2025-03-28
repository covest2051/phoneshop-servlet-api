package com.es.phoneshop.service.product;

import com.es.phoneshop.model.product.Feedback;
import com.es.phoneshop.model.product.FeedbackSortField;
import com.es.phoneshop.model.product.SortOrder;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FeedbackServiceImpl implements FeedbackService {
    private static final FeedbackServiceImpl FEEDBACK_SERVICE = new FeedbackServiceImpl();

    public synchronized static FeedbackServiceImpl getFeedbackService() {
        return FEEDBACK_SERVICE;
    }

    private final Map<FeedbackSortField, Comparator<Feedback>> comparators = Map.of(
            FeedbackSortField.RATING, Comparator.comparing(Feedback::getRating)
    );

    @Override
    public List<Feedback> getFeedbackList(List<Feedback> feedbackList, String sortFieldStr, String sortOrderStr) {
        Comparator<Feedback> comparator;
        FeedbackSortField sortField = Optional.ofNullable(sortFieldStr)
                .map(String::toUpperCase)
                .map(FeedbackSortField::valueOf)
                .orElse(null);
        SortOrder sortOrder = Optional.ofNullable(sortOrderStr)
                .map(String::toUpperCase)
                .map(SortOrder::valueOf)
                .orElse(null);

        if (sortField == null) {
            return feedbackList;
        }
        comparator = comparators.get(sortField);

        if (sortOrder == SortOrder.DESC) {
            comparator = comparator.reversed();
        }

        return feedbackList.stream()
                .sorted(comparator)
                .toList();
    }
}