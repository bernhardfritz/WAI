package controllers;

import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

public class AdminSecured extends Security.Authenticator {
	@Override
	public String getUsername(Context arg0) {
        String username = arg0.session().get("username");

        if (username != null && username.equals("admin")) {
            return username;
        }
		return null;
	}
	
	@Override
	public Result onUnauthorized(Context arg0) {
		return redirect(routes.Application.logout());
	}
}
