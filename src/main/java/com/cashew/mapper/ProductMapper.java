package com.cashew.mapper;

import com.cashew.service.receipt.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper( ProductMapper.class );

    com.cashew.entity.Product productToProductEntity(Product product);

}
