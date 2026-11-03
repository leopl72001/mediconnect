package com.universidad.mediconnect2.catalog;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SpecialtyMapper {
    CatalogController.SpecialtyView toView(Specialty specialty);
}
