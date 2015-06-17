package com.capgemini.poc.microservice1.service.support;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.poc.microservice1.model.Event;
import com.capgemini.poc.microservice1.model.Password;
import com.capgemini.poc.microservice1.service.CipherService;
import com.capgemini.poc.microservice1.service.RandomizeService;

@Service
public class RSACipher implements InitializingBean, CipherService
{
	private List<KeyPair> keys = new ArrayList<KeyPair>();

	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Autowired
	private RandomizeService randomService;
	
	private void queueMessage(int key, String password) throws Exception
	{
        Event ev = new Event("passwordGeneration");
        ev.addData("key", key);
        ev.addData("password", password);
        ev.addData("email", "jatarifa@gmail.com");
        ev.addData("subject", "Cambio de password");
        ev.addData("content", "Buenos dias, se ha solicitado una clave nueva : " + password + ",\ncon indice de clave : " + key + "\n");

        rabbitTemplate.convertAndSend(ev.toJSON());
	}
	
	@Override
	public void afterPropertiesSet() throws Exception 
	{
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(512);
        
        for(int i=0; i < 100; i++)
        	keys.add(keyGen.genKeyPair());
	}

	@Override
	public Password generateSignature(int keyIndex) throws Exception 
	{
        String cifrado = cipher(randomService.getRandomString(), keyIndex);
        Password pwd = new Password(cifrado, keyIndex);  
        
        queueMessage(keyIndex, pwd.getPassword());
        
		return pwd;
	}
	
	@Override
	public String cipher(String toCipher, int keyIndex) throws Exception
	{
		byte [] cif = doCipher(toCipher.getBytes(), keyIndex, Cipher.ENCRYPT_MODE);
		String cifrado = org.apache.commons.codec.binary.Base64.encodeBase64URLSafeString(cif);
		return cifrado;
	}
	
	@Override
	public String decipher(String toDeCipher, int keyIndex) throws Exception
	{
		byte [] cif = org.apache.commons.codec.binary.Base64.decodeBase64(toDeCipher);
		byte [] decif = doCipher(cif, keyIndex, Cipher.DECRYPT_MODE);
		String descifrado = org.apache.commons.codec.binary.Base64.encodeBase64URLSafeString(decif);
		return descifrado;
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
