package controllers;

import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

public class Secured extends Security.Authenticator {
	@Override
	public String getUsername(Context arg0) {
		return arg0.session().get("username");
	}
	
	@Override
	public Result onUnauthorized(Context arg0) {
		return redirect(routes.Application.login());
	}
}
