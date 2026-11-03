package com.universidad.mediconnect2.catalog;

import java.util.List;
import java.util.UUID;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import com.universidad.mediconnect2.admin.*;

@RestController
@RequestMapping("/api/v1")
public class CatalogController {
    private final SpecialtyRepository specialties;
    private final SpecialtyMapper mapper;
    private final DoctorRepository doctors;
    private final OfficeRepository offices;
    public CatalogController(SpecialtyRepository specialties, DoctorRepository doctors, OfficeRepository offices, SpecialtyMapper mapper) {
        this.specialties=specialties; this.doctors=doctors; this.offices=offices; this.mapper=mapper;
    }
    public record SpecialtyView(UUID id,String name) {}
    public record DoctorView(UUID id,String firstName,String lastName,UUID specialtyId,String specialtyName) implements java.io.Serializable {}
    public record OfficeView(UUID id,String name,String address,String roomNumber,boolean active) {}

    @GetMapping("/specialties")
    public List<SpecialtyView> specialties() {
        return specialties.findAll().stream().map(mapper::toView).toList();
    }
    @GetMapping("/doctors")
    @Cacheable(cacheNames="doctorsBySpecialty", key="#specialtyId == null ? 'all' : #specialtyId.toString()")
    @Transactional(readOnly=true)
    public List<DoctorView> doctors(@RequestParam(required=false) UUID specialtyId) {
        List<Doctor> result=specialtyId==null?doctors.findAll():doctors.findBySpecialty_Id(specialtyId);
        return result.stream().map(d -> new DoctorView(d.getId(),d.getUser().getFirstName(),
                d.getUser().getLastName(),d.getSpecialty().getId(),d.getSpecialty().getName())).toList();
    }
    @GetMapping("/offices")
    public List<OfficeView> offices() {
        return offices.findAll().stream().map(o -> new OfficeView(o.getId(),o.getName(),
                o.getAddress(),o.getRoomNumber(),o.isActive())).toList();
    }
}
