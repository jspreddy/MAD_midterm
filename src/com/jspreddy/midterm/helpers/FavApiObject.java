package com.jspreddy.midterm.helpers;

import java.util.ArrayList;

public class FavApiObject {

	ErrorObject error;
	UserObject user;
	ArrayList<FavoriteObject> favorites;
	
	FavApiObject(){
		error = new ErrorObject();
		user = new UserObject();
		favorites = new ArrayList<FavoriteObject>();
	}

	public ErrorObject getError() {
		return error;
	}

	public UserObject getUser() {
		return user;
	}

	public ArrayList<FavoriteObject> getFavorites() {
		return favorites;
	}

	public void setError(ErrorObject error) {
		this.error = error;
	}

	public void setUser(UserObject user) {
		this.user = user;
	}

	public void addFavorite(FavoriteObject fav)
	{
		this.favorites.add(fav);
	}
}
