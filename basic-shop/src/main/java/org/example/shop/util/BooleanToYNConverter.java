package org.example.shop.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class BooleanToYNConverter implements AttributeConverter<Boolean, String> {

  @Override
  public String convertToDatabaseColumn(Boolean attribute) {
    return attribute != null && attribute ? "Y" : "N";
  }

  @Override
  public Boolean convertToEntityAttribute(String dbData) {
    return "Y".equals(dbData);
  }
}

/*
모든 boolean 타입에 적용시키려면 autoApply = true 옵션 주자
해당 옵션 활성화 후에는 엔티티 필드에서 @Convert(converter = BooleanToYNConverter.class)
명시적으로 써두지 않아도 적용된다
 */