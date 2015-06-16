package com.capgemini.poc.microservice1.service;

public interface CipherService 
{
	public byte[] cipher(byte[] toCipher, int keyIndex) throws Exception;
	public byte[] decipher(byte[] toDeCipher, int keyIndex) throws Exception;
}
