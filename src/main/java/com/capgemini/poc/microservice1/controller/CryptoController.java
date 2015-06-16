package com.capgemini.poc.microservice1.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.poc.microservice1.model.Password;
import com.capgemini.poc.microservice1.service.CipherService;
import com.capgemini.poc.microservice1.service.RandomizeService;

@RestController
@RequestMapping("/crypto")
public class CryptoController 
{
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Autowired
	private CipherService cipherService;
	
	@Autowired
	private RandomizeService randomService;
	
    @RequestMapping("/generaPassword")
    public Password generatePassword(@RequestParam(value = "key", defaultValue = "1") int key) throws Exception
    {
        byte [] cif = cipherService.cipher(randomService.getRandomString().getBytes(), key);
        Password pwd = new Password(org.apache.commons.codec.binary.Base64.encodeBase64URLSafeString(cif), key);
        rabbitTemplate.convertAndSend("microservices_arch","Password:Generation");
        
        return pwd;
    }
    
    @RequestMapping("/cifra")
    public String cifrar(@RequestParam(value = "key", defaultValue = "1") int key,
    					 @RequestParam(value = "texto", required = true) String texto) throws Exception
    {
        byte [] cif = cipherService.cipher(texto.getBytes(), key);
        return org.apache.commons.codec.binary.Base64.encodeBase64URLSafeString(cif);
    }
    
    @RequestMapping("/descifra")
    public String descifrar(@RequestParam(value = "key", defaultValue = "1") int key,
    						@RequestParam(value = "texto", required = true) String texto) throws Exception
    {
    	byte [] cif = org.apache.commons.codec.binary.Base64.decodeBase64(texto);
        String ret = new String(cipherService.decipher(cif, key));
        return ret;
    }
}
