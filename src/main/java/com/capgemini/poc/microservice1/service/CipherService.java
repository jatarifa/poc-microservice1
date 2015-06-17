package com.capgemini.poc.microservice1.service;

import com.capgemini.poc.microservice1.model.Password;

public interface CipherService 
{
	public Password generateSignature(int keyIndex) throws Exception;
	public String cipher(String toCipher, int keyIndex) throws Exception;
	public String decipher(String toDeCipher, int keyIndex) throws Exception;
}
