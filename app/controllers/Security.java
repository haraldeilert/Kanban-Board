package controllers;

import models.User;

public class Security extends Secure.Security {
	
	static boolean authenticate(String username, String password) {
		 User admin = User.connect(username, password);
		 if(admin != null)
			 return admin.isAdmin();
		 else
			 return false;
    }
}
