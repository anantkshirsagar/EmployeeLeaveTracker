package com.elt.utils;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.common.reflect.TypeToken;

public class JsonType<T> {
	
	public Type getType() {
		return new TypeToken<T>() {
		}.getType();
	}
}
