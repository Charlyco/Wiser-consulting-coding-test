package com.wiser.consulting.ordering_service.dto;

import java.util.List;

public record SaveOrderModelForRecommendation (String customerEmail, List<SaveOrderItems> previouslyOrderedProducts){}
