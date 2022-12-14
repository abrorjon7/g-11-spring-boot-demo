package uz.pdp.springbootdemo.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.pdp.springbootdemo.dto.CarByIdDto;
import uz.pdp.springbootdemo.dto.CarDto;
import uz.pdp.springbootdemo.entity.Brand;
import uz.pdp.springbootdemo.entity.Car;
import uz.pdp.springbootdemo.projection.CarByIdProjection;
import uz.pdp.springbootdemo.repository.BrandRepo;
import uz.pdp.springbootdemo.repository.CarRepo;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepo carRepo;
    private final BrandRepo brandRepo;

    public List<Car> getAllCarsFromDb(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Car> carsPage;
        if (search != null)
            carsPage = carRepo.findByModelLikeIgnoreCase(pageable, search);
//            carsPage = carRepo.findAllByModel(pageable, search);
        else
            carsPage = carRepo.findAll(pageable);
        int totalPages = carsPage.getTotalPages();
        long totalElements = carsPage.getTotalElements();
        List<Car> carList = carsPage.getContent();
        return carList;
    }


//    public CarByIdDto getCarById(Integer id) {
//        Optional<Car> optionalCar = carRepo.findById(id);
//        if (optionalCar.isEmpty()) {
//            throw new IllegalStateException("Car not found!!!");
//        }
//
//        Car carFromDb = optionalCar.get();
//        CarByIdDto carByIdDto = CarByIdDto.builder()
//                .id(carFromDb.getId())
//                .model(carFromDb.getModel())
//                .brandId(carFromDb.getBrand().getId())
//                .brandName(carFromDb.getBrand().getName())
//                .build();
//
//        return carByIdDto;
//    }

    public CarByIdProjection getCarById(Integer id) {
        Optional<CarByIdProjection> optionalCar = carRepo.getCarById(id);
        if (optionalCar.isEmpty()) {
            throw new IllegalStateException("Car not found!!!");
        }
        return optionalCar.get();
    }


    public void saveCar(CarDto carDto) {
        Optional<Brand> optionalBrand = brandRepo.findById(carDto.getBrandId());
        if (optionalBrand.isEmpty()) {
            throw new IllegalStateException("Brand not found!!!");
        }
        carRepo.save(Car.builder()
                .id(carDto.getId())
                .model(carDto.getModel())
                .brand(optionalBrand.get())
                .description(carDto.getDescription())
                .build());
    }

    public void deleteCarById(int id) {
        carRepo.deleteById(id);
    }
}
