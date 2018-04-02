package com.mooc.house.common.utils;

import java.nio.charset.Charset;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class HashUtils {
	public static final HashFunction FUNCTION=Hashing.md5();
	//加盐
	public static final String SALT = "mooc.com";
	
	public static final String encryPassword(String password) {
		HashCode hashCode= FUNCTION.hashString(password+SALT, Charset.forName("UTF-8"));
		return hashCode.toString();
	}
	
}
