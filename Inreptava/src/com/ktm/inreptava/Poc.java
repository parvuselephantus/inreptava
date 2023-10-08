//package com.ktm.inreptava;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//import java.lang.reflect.ParameterizedType;
//import java.lang.reflect.Type;
//import java.util.stream.Stream;
//
//import com.asecla.licenses.server.model.LicenseType;
//import com.asecla.licenses.server.webservice.api.LicenseTypeAPI;
//
//public class Poc {
//	private static boolean hasGetter(Class<?> clazz, Field field) {
//		String name = field.getName();
//		final String getterName = "get" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
//		return Stream.of(clazz.getMethods()).filter(m -> m.getName().equals(getterName) && m.getParameterCount() == 0 && m.getReturnType().equals(field.getType())).findAny().isPresent();
//	}
//	
//	private void printAPIClass(Class<?> clazz) {
//		System.out.println("public class " + clazz.getSimpleName() + "API {");
//		for (Field field : LicenseType.class.getDeclaredFields()) {
//			Type fieldType = field.getAnnotatedType().getType();
//			String typeName = null;
//			if (fieldType instanceof ParameterizedType) {
//				typeName = ((ParameterizedType) fieldType).getRawType().getTypeName();
//			} else {
//				typeName = field.getAnnotatedType().getType().getTypeName();
//			}
//			if (typeName.contains(".")) {
//				typeName = typeName.substring(typeName.lastIndexOf(".")+1);
//			}
//			
//			System.out.println(hasGetter(field, clazz) ? "Has getter" : "NO GETTER");
//			System.out.println(hasSetter(field, clazz) ? "Has setter" : "NO ETTER");
//			System.out.println("\tprivate " + typeName + " " + field.getName() + ";");
//		}
//	}
//	
//	private void checkAPIClass(Class<?> clazz, Class<?> targetClazz) {
//		for (Field field : clazz.getDeclaredFields()) {
//			Type fieldType = field.getAnnotatedType().getType();
//			String typeName = null;
//			if (fieldType instanceof ParameterizedType) {
//				typeName = ((ParameterizedType) fieldType).getRawType().getTypeName();
//			} else {
//				typeName = field.getAnnotatedType().getType().getTypeName();
//			}
//			if (typeName.contains(".")) {
//				typeName = typeName.substring(typeName.lastIndexOf(".")+1);
//			}
//			
//			Field targetField = null;
//			try {
//				targetField = targetClazz.getField(field.getName());
//				if (!targetField.getAnnotatedType().getType().getTypeName().equals(field.getAnnotatedType().getType().getTypeName())) {
//					targetField = null;
//				}
//			} catch (NoSuchFieldException | SecurityException e) {
//			}
//
//			
//			System.out.println("\tprivate " + typeName + " " + field.getName() + ";");
//		}
//	}
//	
//	private boolean hasGetter(Field targetField, Class<?> targetClazz) {
//		String getterName = ((targetField.getClass().getCanonicalName().equals("boolean") || targetField.getClass().getCanonicalName().equals("boolean")) ? "is" : "get")
//				+ Character.toUpperCase(targetField.getName().charAt(0)) + targetField.getName().substring(1);
//		for (Method method : targetClazz.getDeclaredMethods()) {
//			if (method.getName().equals(getterName)
//					&& method.getReturnType().equals(targetField.getType())
//					&& method.getParameterCount() == 0)
//				return true;
//		}
//		return false;
//	}
//
//	private boolean hasSetter(Field targetField, Class<?> targetClazz) {
//		String setterName = "set" + Character.toUpperCase(targetField.getName().charAt(0)) + targetField.getName().substring(1);
//		for (Method method : targetClazz.getDeclaredMethods()) {
//			if (method.getName().equals(setterName)
//					&& method.getReturnType().equals(void.class)
//					&& method.getParameterCount() == 1
//					&& method.getParameters()[0].getType().equals(targetField.getType()))
//				return true;
//		}
//		return false;
//	}
//	public static void main(String[] args) {
//		Poc poc = new Poc();
//		poc.printAPIClass(LicenseType.class);
////		poc.checkAPIClass(LicenseType.class, LicenseTypeAPI.class);
////		for (Field field : LicenseType.class.getDeclaredFields()) {
////		    field.setAccessible(true); // You might want to set modifier to public first.
////	        System.out.println(field.getAnnotatedType() + " " + field.getName());
////	        if (hasGetter(field.getDeclaringClass(), field)) {
////	        	System.out.println("Getter");
////	        }
////	        System.out.println();
////		}
//	}
//}
