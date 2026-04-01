package com.db.servicecategory;

import com.db.common.Constants;
import com.db.common.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/services")
public class ServiceCategoryController {


    @Autowired private IServiceCategory serviceCategory;


    @PostMapping("/add")
    public ResponseEntity<Response> saveNewServiceCategory(@RequestBody AddServiceCategoryRequest request) {
        Response response = serviceCategory.addNewService(request);

        if (Constants.SUCCESS_CODE.equalsIgnoreCase(response.getResponseCode())) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<GetAllServices> getAllServiceCategories() {
        GetAllServices allServices = serviceCategory.getAllServices();
        return ResponseEntity.ok(allServices);
    }

    @PutMapping("/update")
    public ResponseEntity<Response> updateServiceCategory(@RequestBody UpdateServiceCategory request) {
        Response response = serviceCategory.updateServiceCategory(request);

        if (Constants.SUCCESS_CODE.equalsIgnoreCase(response.getResponseCode())) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteServiceCategory(@PathVariable Long id) {

        try{
            serviceCategory.deleteService(id);
            return new ResponseEntity<>("Category deleted Successfully",HttpStatus.OK);
        }
        catch (RuntimeException e)
        {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
}