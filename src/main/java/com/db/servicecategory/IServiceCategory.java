package com.db.servicecategory;

import com.db.common.Response;

import java.util.List;

public interface IServiceCategory {
    Response addNewService (AddServiceCategoryRequest request);
    GetAllServices getAllServices();

    void deleteService(Long id);

    Response updateServiceCategory(UpdateServiceCategory request);
}
