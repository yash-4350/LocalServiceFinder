package com.db.servicecategory;

import com.db.common.Constants;
import com.db.common.Response;
import com.db.database.RepositoryFactory;
import com.db.database.entities.ServiceCategory;
import com.db.database.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ServiceCategoryImpl implements IServiceCategory {

    private final RepositoryFactory repositoryFactory;

    @Autowired
    public ServiceCategoryImpl(RepositoryFactory repositoryFactory) {
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public Response addNewService(AddServiceCategoryRequest request) {
        log.debug("Adding new service category: {}", request.getName());

        if (request.getName() == null || request.getDescription() == null) {
            return new Response(Constants.ERROR_CODE, "Name and description are required");
        }

        ServiceCategory category = new ServiceCategory();
        category.setName(request.getName().trim());
        category.setDescription(request.getDescription().trim());

        repositoryFactory.getServiceCategoryRepository().save(category);
        return new Response(Constants.SUCCESS_CODE, Constants.SUCCESS_MSG);
    }

    @Override
    public GetAllServices getAllServices() {
        GetAllServices resp = new GetAllServices();
        List<ServiceCategory> categories = repositoryFactory.getServiceCategoryRepository().findAll();

        if (categories.isEmpty()) {
            resp.setResponseCode(Constants.SUCCESS_CODE);
            resp.setResponseMessage("No categories found");
            return resp;
        }

        // Using Streams for cleaner mapping
        List<ServiceCategoryResponse> responsesList = categories.stream().map(service -> {
            ServiceCategoryResponse dto = new ServiceCategoryResponse();
            dto.setId(String.valueOf(service.getId()));
            dto.setName(service.getName());
            dto.setDescription(service.getDescription());
            return dto;
        }).toList();

        resp.setData(responsesList);
        resp.setResponseCode(Constants.SUCCESS_CODE);
        resp.setResponseMessage(Constants.SUCCESS_MSG);
        return resp;
    }

    @Override
    public void deleteService(Long id)
    {
        ServiceCategory  category=repositoryFactory.getServiceCategoryRepository().findById(id)
                .orElseThrow(()->new RuntimeException("Error: Service Category not found with ID:" +id));
        repositoryFactory.getServiceCategoryRepository().delete(category);
    }

    @Override
    public Response updateServiceCategory(UpdateServiceCategory request) {
        if (request.getId() == null) { // Fixed: was request.get()
            return new Response(Constants.ERROR_CODE, "ID is required");
        }

        return repositoryFactory.getServiceCategoryRepository().findById(request.getId())
                .map(category -> {
                    if (request.getName() != null && !request.getName().isBlank()) {
                        category.setName(request.getName().trim());
                    }
                    if (request.getDescription() != null && !request.getDescription().isBlank()) {
                        category.setDescription(request.getDescription().trim());
                    }
                    repositoryFactory.getServiceCategoryRepository().save(category);
                    return new Response(Constants.SUCCESS_CODE, "Updated successfully");
                })
                .orElse(new Response(Constants.ERROR_CODE, "Service category not found"));
    }
}