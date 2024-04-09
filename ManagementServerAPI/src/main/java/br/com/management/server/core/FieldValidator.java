package br.com.management.server.core;

import java.lang.reflect.Field;

import br.com.management.server.exception.RequiredFieldException;

public class FieldValidator {

	public static void check(String[] fieldNames, Object vo) throws Exception {
		
		if(vo == null) {
			throw new RequiredFieldException("The request object can't be null");
		}
		
		for (String fieldName : fieldNames) {
			try {
				Field field = vo.getClass().getDeclaredField(fieldName);
				field.setAccessible(true);
				
				Object fieldValue = field.get(vo);
								
				if(fieldValue == null || fieldValue.toString().isEmpty() || fieldValue.toString().isBlank()) {
					throw new RequiredFieldException("The field " + fieldName + " is required");
				}
			} catch (Exception e) {
				throw e;
			}
		}
	}

}
