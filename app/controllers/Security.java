package controllers;

import models.MyUser;

public class Security extends Secure.Security {
	
	static boolean authenticate(String username, String password) {
		 MyUser admin = MyUser.connect(username, password);
		 if(admin != null)
			 return admin.isAdmin();
		 else
			 return false;
    }
}
