package com.capgemini.poc.microservice1.service.support;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.capgemini.poc.microservice1.service.CipherService;

@Service
public class RSACipher implements InitializingBean, CipherService
{
	private List<KeyPair> keys = new ArrayList<KeyPair>();

	@Override
	public void afterPropertiesSet() throws Exception 
	{
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(512);
        
        for(int i=0; i < 100; i++)
        	keys.add(keyGen.genKeyPair());
	}

	@Override
	public byte[] cipher(byte[] toCipher, int keyIndex) throws Exception
	{
		return doCipher(toCipher, keyIndex, Cipher.ENCRYPT_MODE);
	}
	
	@Override
	public byte[] decipher(byte[] toDeCipher, int keyIndex) throws Exception
	{
		return doCipher(toDeCipher, keyIndex, Cipher.DECRYPT_MODE);
	}
	
	private byte[] doCipher(byte[] data, int keyIndex, int mode) throws Exception
	{
		if(keyIndex > keys.size())
			return null;

		Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
		if(mode == Cipher.ENCRYPT_MODE)
			cipher.init(mode, keys.get(keyIndex - 1).getPublic());
		else			
			cipher.init(mode, keys.get(keyIndex - 1).getPrivate());
		byte [] cif = cipher.doFinal(data);
		
		return cif;
	}
}
