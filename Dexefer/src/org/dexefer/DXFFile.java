package org.dexefer;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

import org.dexefer.annotations.DXFElementType;
import org.dexefer.annotations.DXFProperty;
import org.dexefer.annotations.DXFSubElements;
import org.dexefer.annotations.DXFTerminator;
import org.dexefer.sections.Entities;

/**
 * Allows defining the structure of a DXF file.
 * 
 * @author luisro
 */
public class DXFFile {
	
	Entities entities;
	
	/**
	 * Creates an empty (without entities) DXFFile instance.
	 */
	public DXFFile() {
		entities = new Entities();
	}
	
	/**
	 * Returns the entities contained in the DXF file.
	 * @return
	 */
	public Entities getEntities() {
		return entities;
	}
	
	public void write(OutputStream output) throws IOException {
		DXFWriter writer = new DXFWriter(output);
		
		writeElement(writer, entities);
		
		writer.writeEntry(0, "EOF");		
		writer.close();
	}
	
	private void writeElement(DXFWriter writer, DXFElement element) throws IOException {
		Class<? extends DXFElement> eClass = element.getClass();
		String className = eClass.getSimpleName().toUpperCase();
		DXFElementType elementTypeA = eClass.getAnnotation(DXFElementType.class);
		if(elementTypeA==null) {
			writer.writeEntry(0, className);	
		} else {
			writer.writeEntry(0, elementTypeA.value());
			writer.writeEntry(2, className);
		}	
		
		// We iterate the fields.
		for(Field f : element.getClass().getDeclaredFields()) {
			
			DXFProperty propertyA = f.getAnnotation(DXFProperty.class);
			DXFSubElements subElementsA = f.getAnnotation(DXFSubElements.class);
			
			if(propertyA!=null && subElementsA!=null) {
				// We can't have the both annotations.
				throw new IllegalStateException(String.format("Field '%s' in class '%s' cannot have annotations DXFProperty and DXFSubElements at the same time."));				
			} else  if(propertyA!=null) {
				// We write the property's value, if it's not null (optional).
				Object value =  getFieldValue(f, element);
				if(value!=null){
					int code = propertyA.value();
					writer.writeEntry(code,value.toString());
				}
			} else if (subElementsA!=null) {				
				Object collection = getFieldValue(f, element);
				if(!Iterable.class.isInstance(collection)){
					throw new IllegalStateException(String.format("The field '%s' is not Iterable so it cannot use the @DXFSubElements annotation."));
				}
				
				for(DXFElement subElement : (Iterable<? extends DXFElement>)collection){
					writeElement(writer, subElement);
				}
			}
		}
		
		DXFTerminator terminatorA = eClass.getAnnotation(DXFTerminator.class);
		if(terminatorA!=null) {
			// We write the element's terminator
			writer.writeEntry(0, terminatorA.value());
		}
	}
	
	private Object getFieldValue(Field field, DXFElement element) {
		
		try {
			return field.get(element);
		} catch (IllegalArgumentException e1) {
			throw new RuntimeException(e1);			
		} catch (IllegalAccessException e1) {
			// We do nothing.
		}
		
		String prefix = "get";		
		
		String getterName = prefix+ field.getName().substring(0, 1).toUpperCase()+field.getName().substring(1);
		Method method;
		try{
			method = element.getClass().getMethod(getterName);
		} catch(NoSuchMethodException e) {
			throw new IllegalStateException(
					String.format("Field '%s' is not public and has no getter method.", field.getName()), e);
		}
		
		Object value;
		try {
			value = method.invoke(element);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);			
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(
					String.format("There is no public getter for field '%s'.", field.getName()), e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
		
		return value;
	}
}
