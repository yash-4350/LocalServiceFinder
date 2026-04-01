package com.db.servicecategory;

import com.db.common.Response;
import lombok.Data;

import java.util.List;

@Data
public class GetAllServices extends Response {
    private List<ServiceCategoryResponse> data;

}
