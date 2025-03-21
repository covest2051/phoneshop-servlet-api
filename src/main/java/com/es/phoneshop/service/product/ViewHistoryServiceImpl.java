package com.es.phoneshop.service.product;

import com.es.phoneshop.model.product.Product;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

public class ViewHistoryServiceImpl implements ViewHistoryService {
    private static final ViewHistoryServiceImpl VIEW_HISTORY_SERVICE = new ViewHistoryServiceImpl();

    public synchronized static ViewHistoryServiceImpl getViewHistoryService() {
        return VIEW_HISTORY_SERVICE;
    }

    @Override
    public void viewHistory(Product product, HttpServletRequest request) {
        HttpSession session = request.getSession();
        List<Product> viewHistory = (List<Product>) session.getAttribute("viewHistory");

        if (viewHistory == null) {
            viewHistory = new ArrayList<>();
            session.setAttribute("viewHistory", viewHistory);
        }
        if (!viewHistory.contains(product)) {
            if (viewHistory.size() < 3)
                viewHistory.add(product);
            else {
                viewHistory.remove(0);
                viewHistory.add(product);
            }
        }
    }
}
