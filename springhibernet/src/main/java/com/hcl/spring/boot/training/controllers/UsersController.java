package com.hcl.spring.boot.training.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.custom.logger.CustomLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.spring.boot.training.service.UserService;
import com.hcl.spring.model.User;

@RestController
@RequestMapping("/")
public class UsersController {
	private static final CustomLogger logger = CustomLogger.getLogger(UsersController.class);
	ObjectMapper objectMapper= new ObjectMapper(); 
	@Autowired
	private UserService userService;
	   
	@RequestMapping(value = "customer/create", method = RequestMethod.POST, consumes =  MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createUser(@RequestBody User user) throws JsonProcessingException {
		
		logger.info("Request-Date-Time:"+new Date(System.currentTimeMillis())+",operation:new-customer"+",input:"+objectMapper.writeValueAsString(user));
		return new ResponseEntity<User>(userService.createUser(user), new HttpHeaders(), HttpStatus.OK);
	}
	
	@RequestMapping(value ="customer/delete/{userId}", method = RequestMethod.DELETE)
	  public ResponseEntity<?> delete( @PathVariable("userId") long id) {
	    try {
	    	logger.info("Request-Date-Time:"+new Date(System.currentTimeMillis())+",operation:delete-customer"+",input:"+id);
	      User user = new User(id);
	      userService.delete(user);
	    }
	    catch (Exception ex) {
	      return new ResponseEntity<String>("Error deleting the user: " + ex.toString(),HttpStatus.BAD_REQUEST);
	    }
	    return new ResponseEntity<String>("User succesfully deleted!",HttpStatus.OK);
	  }
	@RequestMapping(value ="customer/by-email",method = RequestMethod.GET)
	  
	  public ResponseEntity<?> getByEmail(@RequestParam(value = "emailtest") String email) {
	    String userId;
	    try {
	    	logger.info("Request-Date-Time:"+new Date(System.currentTimeMillis())+",operation:fetch-customer"+",input:"+email);
	      User user = userService.findByEmail(email);
	      userId = String.valueOf(user.getId());
	    }
	    catch (Exception ex) {
	      return new ResponseEntity<String>("User not found"+ ex.toString(),HttpStatus.BAD_REQUEST);
	    }
	    return new ResponseEntity<String>("The user id is: " + userId,HttpStatus.OK);
	  }
	@RequestMapping(value="customer/update" ,method = RequestMethod.POST)
	  
	  public ResponseEntity<?> updateUser(@RequestBody User updateUser) {
	    try {
	    	logger.info("Request-Date-Time:"+new Date(System.currentTimeMillis())+",operation:update-customer"+",input:"+objectMapper.writeValueAsString(updateUser));
	      User user = userService.findOne(updateUser.getId());
	      user.setEmail(updateUser.getEmail());
	      userService.save(user);
	    }
	    catch (Exception ex) {
	    	return new ResponseEntity<String>("Error updating the user: " + ex.toString(),HttpStatus.BAD_REQUEST);
	     
	    }
	    return new ResponseEntity<String>( "User succesfully updated!",HttpStatus.OK);
	    
	  }
}
