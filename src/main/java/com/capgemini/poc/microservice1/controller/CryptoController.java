package com.capgemini.poc.microservice1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.poc.microservice1.model.Password;
import com.capgemini.poc.microservice1.service.CipherService;

@RestController
@RequestMapping("/crypto")
public class CryptoController 
{
	@Autowired
	private CipherService cipherService;
	
    @RequestMapping("/generaPassword")
    public Password generatePassword(@RequestParam(value = "key", defaultValue = "1") int key) throws Exception
    {
    	return cipherService.generateSignature(key);
    }
    
    @RequestMapping("/cifra")
    public String cifrar(@RequestParam(value = "key", defaultValue = "1") int key,
    					 @RequestParam(value = "texto", required = true) String texto) throws Exception
    {
        return cipherService.cipher(texto, key);
    }
    
    @RequestMapping("/descifra")
    public String descifrar(@RequestParam(value = "key", defaultValue = "1") int key,
    						@RequestParam(value = "texto", required = true) String texto) throws Exception
    {
    	return cipherService.decipher(texto, key);
    }
}
