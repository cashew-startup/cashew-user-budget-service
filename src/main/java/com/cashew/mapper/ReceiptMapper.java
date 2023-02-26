package com.cashew.mapper;

import com.cashew.service.receipt.Product;
import com.cashew.service.receipt.Receipt;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReceiptMapper {

    ReceiptMapper INSTANCE = Mappers.getMapper( ReceiptMapper.class );

    com.cashew.entity.Receipt receiptToReceiptEntity(Receipt receipt);

    com.cashew.entity.Product productToProductEntity(Product product);

}

